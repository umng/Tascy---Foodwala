package xyz.umng.tascy.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import xyz.umng.tascy.R;
import xyz.umng.tascy.adapter.ListViewAdapter;
import xyz.umng.tascy.model.WorldPopulation;

/**
 * Created by Umang on 2/15/2016.
 */
public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    private Toolbar toolbar;

    ParseUser user;

    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ListViewAdapter adapter;
    private List<WorldPopulation> worldpopulationlist = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user = new ParseUser();

        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();
    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
            // Set progressdialog title
            //mProgressDialog.setTitle("Parse.com Custom ListView Tutorial");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
            worldpopulationlist = new ArrayList<>();
            try {
                // Locate the class table named "Country" in Parse.com
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "Country");
                // Locate the column named "ranknum" in Parse.com and order list
                // by ascending
                query.orderByAscending("ranknum");
                ob = query.find();
                for (ParseObject country : ob) {
                    // Locate images in flag column
                    ParseFile image = (ParseFile) country.get("flag");

                    WorldPopulation map = new WorldPopulation();
                    map.setRank((String) country.get("rank"));
                    map.setCountry((String) country.get("country"));
                    map.setPopulation((String) country.get("population"));
                    map.setFlag(image.getUrl());
                    worldpopulationlist.add(map);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listview);
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapter(MainActivity.this,
                    worldpopulationlist);
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }

    private void logout()
    {
        Log.d(TAG, "Logout");
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging out...");
        progressDialog.show();

        user.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null)
                {
                    progressDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_LONG).show();
                }
                else
                {
                    progressDialog.dismiss();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    Toast.makeText(MainActivity.this, "Can't connect to the internet\n Try again later", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void gotoSettings()
    {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void gotoHelp()
    {
        startActivity(new Intent(this, HelpActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        else if (id == R.id.action_help) {
            gotoHelp();
            return true;
        }
        else if (id == R.id.action_settings) {
            gotoSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
