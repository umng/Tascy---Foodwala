package xyz.umng.tascy.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import xyz.umng.tascy.R;
import xyz.umng.tascy.adapter.ItemListViewAdapter;
import xyz.umng.tascy.model.Item;

/**
 * Created by Umang on 4/7/2016.
 */

public class SingleItemViewActivity extends Activity {
    // Declare Variables
    String itemCategory;
    String region;
    String coverImage;
    String position;
    // Animation
    Animation animFadein;

    ListView itemListview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ItemListViewAdapter adapter;
    private List<Item> itemList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_singleitemvieweitemview.xml
        setContentView(R.layout.activity_singleitemview);

        Intent i = getIntent();
        // Get the result of itemItem
        itemCategory = i.getStringExtra("itemCategory");
        // Get the result of region
        region = i.getStringExtra("region");
        // Get the result of coverImage
        coverImage = i.getStringExtra("coverImage");

        // Locate the TextViews in activity_singleitemview.xml
        TextView txtItemCategory = (TextView) findViewById(R.id.itemCategory);
        TextView txtRegion = (TextView) findViewById(R.id.region);

        // Locate the ImageView in activity_singleitemview.xml
        ImageView imgCoverImage = (ImageView) findViewById(R.id.coverImage);

        // Set results to the TextViews
        txtItemCategory.setText(itemCategory);
        txtRegion.setText(region);
        new DownloadImageTask(imgCoverImage).execute(coverImage);



        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();
    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(SingleItemViewActivity.this, R.style.AppTheme_Dark_Dialog);
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
                query.whereEqualTo("itemCategory", itemCategory);
                query.orderByAscending("rank");
                ob = query.find();
                for (ParseObject category : ob) {
                    // Locate images in flag column
                    ParseFile image = (ParseFile) category.get("itemImage");

                    Item item = new Item();
                    item.setItemName((String) category.get("itemName"));
                    item.setPrice((String) category.get("price") + " INR");
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
            itemListview = (ListView) findViewById(R.id.itemListView);
            // Pass the results into itemListviewAdapter.java
            adapter = new ItemListViewAdapter(SingleItemViewActivity.this,
                    itemList);
            // Binds the Adapter to the itemListview
            itemListview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }


    //******************************************************
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);

            // load the animation
            animFadein = AnimationUtils.loadAnimation(SingleItemViewActivity.this,
                    R.anim.fade_in);
            bmImage.startAnimation(animFadein);
        }
    }
    //******************************************************

}