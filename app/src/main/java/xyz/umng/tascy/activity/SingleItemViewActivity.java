package xyz.umng.tascy.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import xyz.umng.tascy.R;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_singleitemvieweitemview.xml
        setContentView(R.layout.activity_singleitemview);

        Intent i = getIntent();
        // Get the result of itemCategory
        itemCategory = i.getStringExtra("itemCategory");
        // Get the result of region
        region = i.getStringExtra("region");
        // Get the result of coverImage
        coverImage = i.getStringExtra("coverImage");

        // Locate the TextViews in activity_singleitemvieweitemview.xml
        TextView txtitemCategory = (TextView) findViewById(R.id.itemCategory);
        TextView txtregion = (TextView) findViewById(R.id.region);

        // Locate the ImageView in activity_singleitemvieweitemview.xml
        ImageView imgcoverImage = (ImageView) findViewById(R.id.coverImage);

        // Set results to the TextViews
        txtitemCategory.setText(itemCategory);
        txtregion.setText(region);
        new DownloadImageTask(imgcoverImage).execute(coverImage);
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