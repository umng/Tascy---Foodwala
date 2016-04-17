package xyz.umng.tascy.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import xyz.umng.tascy.R;
import xyz.umng.tascy.adapter.ListViewAdapter;

/**
 * Created by Umang on 4/7/2016.
 */

public class SingleItemViewActivity extends Activity {
    // Declare Variables
    String rank;
    String country;
    String population;
    String flag;
    String position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.singleitemview);

        Intent i = getIntent();
        // Get the result of rank
        rank = i.getStringExtra("rank");
        // Get the result of country
        country = i.getStringExtra("country");
        // Get the result of population
        population = i.getStringExtra("population");
        // Get the result of flag
        flag = i.getStringExtra("flag");

        // Locate the TextViews in singleitemview.xml
        TextView txtrank = (TextView) findViewById(R.id.rank);
        TextView txtcountry = (TextView) findViewById(R.id.country);
        TextView txtpopulation = (TextView) findViewById(R.id.population);

        // Locate the ImageView in singleitemview.xml
        ImageView imgflag = (ImageView) findViewById(R.id.flag);

        // Set results to the TextViews
        txtrank.setText(rank);
        txtcountry.setText(country);
        txtpopulation.setText(population);
        new DownloadImageTask(imgflag).execute(flag);
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
        }
    }
    //******************************************************

}