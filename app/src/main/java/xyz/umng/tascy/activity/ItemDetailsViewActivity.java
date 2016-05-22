package xyz.umng.tascy.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.InputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.umng.tascy.R;
import xyz.umng.tascy.adapter.ItemListViewAdapter;
import xyz.umng.tascy.model.Item;

public class ItemDetailsViewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressDialog mProgressDialog;
    private Animation animFadein;

    private String item_name = "";

    @Bind(R.id.itemImage) ImageView _itemImage;
    @Bind(R.id.itemDetailsName) TextView _itemName;
    @Bind(R.id.itemPrice) TextView _itemPrice;
    @Bind(R.id.itemDescription) TextView _itemDescription;
    @Bind(R.id.itemDescriptionTitle) TextView itemIngredientsTitle;
    @Bind(R.id.itemIngredients) TextView _itemIngredients;
    @Bind(R.id.progressBar) ProgressBar _bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_view);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tascy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i = getIntent();
        item_name = i.getStringExtra("ITEM_NAME");
//        i.getStringExtra("ITEM_CATEGORY");

        _itemName.setText(item_name);
        _itemPrice.setText(i.getStringExtra("PRICE"));

        new DownloadImageTask(_itemImage).execute(i.getStringExtra("ITEM_IMAGE"));

        new RemoteDataTask().execute();

    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(ItemDetailsViewActivity.this, R.style.AppTheme_Dark_Dialog);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();

            _bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Item");
                query.whereEqualTo("itemName", item_name.trim());
                query.orderByAscending("rank");
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        _itemDescription.setText( parseObject.getString("description"));
                        itemIngredientsTitle.setText("IngredieIngredients");
                        _itemIngredients.setText( parseObject.getString("Ingredients"));
                    }
                });
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            _bar.setVisibility(View.GONE);
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
            animFadein = AnimationUtils.loadAnimation(ItemDetailsViewActivity.this,
                    R.anim.fade_in);
            bmImage.startAnimation(animFadein);
        }
    }
    //******************************************************
}
