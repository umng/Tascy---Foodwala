package xyz.umng.tascy.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import xyz.umng.tascy.R;
import xyz.umng.tascy.adapter.ItemListViewAdapter;
import xyz.umng.tascy.adapter.SearchItemListViewAdapter;
import xyz.umng.tascy.model.Item;
import xyz.umng.tascy.model.SearchItem;

public class SearchResultsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private MenuItem mSearchAction;
    private boolean isSearchOpened = true;
    private EditText edtSeach;

    private String searchText;

    ListView itemListview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    SearchItemListViewAdapter adapter;
    private List<SearchItem> itemList = null;
    String tempSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        searchText = intent.getStringExtra("SEARCH_TEXT");
        handleMenuSearch();
        doSearch();

    }

    private void doSearch() {
        // Execute RemoteDataTask AsyncTask
        tempSearch = edtSeach.getText().toString().trim();
        new RemoteDataTask().execute();
    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(SearchResultsActivity.this, R.style.AppTheme_Dark_Dialog);
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
            itemList = new ArrayList<>();
            try {
                // Locate the class table named "Item" in Parse.com
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "Item");
                // Locate the column named "ranknum" in Parse.com and order list
                // by ascending
                query.whereContains("itemName", tempSearch.substring(0, 0).toUpperCase().trim() + tempSearch.substring(1));
                query.orderByAscending("rank");
                ob = query.find();
                for (ParseObject search : ob) {
                    // Locate images in flag column
                    ParseFile image = (ParseFile) search.get("itemImage");

                    SearchItem item = new SearchItem();
                    item.setItemName((String) search.get("itemName"));
                    item.setPrice((String) search.get("price") + " INR");
                    item.setItemCategory((String) search.get("itemCategory"));
                    if(image.getUrl() != null)
                    {
                        item.setItemImage(image.getUrl());
                    }
                    itemList.add(item);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the itemListview in itemListview_main.xml
            itemListview = (ListView) findViewById(R.id.searchlistview);
            // Pass the results into itemListviewAdapter.java
            adapter = new SearchItemListViewAdapter(SearchResultsActivity.this,
                    itemList);
            // Binds the Adapter to the itemListview
            itemListview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        action.setDisplayShowCustomEnabled(true); //enable it to display a
        // custom view in the action bar.
        action.setCustomView(R.layout.search_bar);//add the custom view
        action.setDisplayShowTitleEnabled(false); //hide the title

        edtSeach = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor
        edtSeach.setText(searchText);

        //this is a listener to do a search when the user clicks on search button
        edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchText = edtSeach.getText().toString().trim();
                    doSearch();
                    return true;
                }
                return false;
            }
        });


        edtSeach.requestFocus();

        //open the keyboard focused in the edtSearch
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        handleMenuSearch();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            handleMenuSearch();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
