package xyz.umng.tascy.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import xyz.umng.tascy.R;
import xyz.umng.tascy.adapter.CustomAdapter;
import xyz.umng.tascy.model.Category;
import xyz.umng.tascy.model.DataItem;

/**
 * Created by Umang on 2/15/2016.
 */
public class MainActivity extends ActionBarActivity {

    @Bind(R.id.view_items_btn)
    Button _viewItemsButton;

    private static final String TAG = "MainActivity";

    List<Category> categories = new ArrayList<>();

    ParseUser user;

    private Toolbar toolbar;

    private ProgressDialog progressDialog;

    private ArrayList<DataItem> data;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user = new ParseUser();

        _viewItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ItemListActivity.class));
            }
        });

//        ParseQuery<Category> query = new ParseQuery<>("Category");
//        query.findInBackground(new FindCallback<Category>() {
//            @Override
//            public void done(List<Category> list, ParseException e) {
//                if(e == null)
//                {
//                    for(Category category : list){
//                        Category newCategory = new Category();
//                        newCategory.setCategoryName(category.getCategoryName());
//                        newCategory.setRegion(category.getRegion());
//                        categories.add(newCategory);
//                    }
//
//                    ArrayAdapter<Category> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, categories);
//                    setListAdapter(adapter);
//                }
//                else {
//                    Toast.makeText(getBaseContext(), "Error : getting Categories", Toast.LENGTH_SHORT);
//                }
//            }
//        });

        progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        data = new ArrayList<>();
//        this.data.add(new DataItem("Umang Patel", "8195875260"));
//        this.data.add(new DataItem("Shyam Javia", "9898989898"));
//        this.data.add(new DataItem("Nikunj Patel", "9586565226"));

        adapter = new CustomAdapter(this, data);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        ParseQuery orderQuery = new ParseQuery("Category");
        orderQuery.orderByDescending("createdAt");
        orderQuery.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> content, ParseException pEx) {
                if (pEx == null && content != null) {
                    if (!(content.isEmpty())) {
                        for (ParseObject orderObject : content) {
                            data.add(new DataItem(orderObject.getString("categoryName"), orderObject.getString("region").toString()));
                            adapter.notifyDataSetChanged();
                        }
                        progressDialog.dismiss();
                    }
                }
            }
        });

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
