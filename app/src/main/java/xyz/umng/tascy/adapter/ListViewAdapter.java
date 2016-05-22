package xyz.umng.tascy.adapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import xyz.umng.tascy.R;
import xyz.umng.tascy.activity.ItemListViewActivity;
import xyz.umng.tascy.model.Category;

/**
 * Created by Umang on 4/7/2016.
 */
public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    private List<Category> categoryList = null;
    private ArrayList<Category> arrayList;

    // Animation
    Animation animFadein;

    public ListViewAdapter(Context context,
                           List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
        inflater = LayoutInflater.from(context);
        this.arrayList = new ArrayList<Category>();
        this.arrayList.addAll(categoryList);
    }

    public class ViewHolder {
        TextView itemCategory;
        TextView region;
        TextView population;
        ImageView coverImage;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.itemCategory = (TextView) view.findViewById(R.id.itemCategory);
            holder.region = (TextView) view.findViewById(R.id.region);
            // Locate the ImageView in listview_item.xml
            holder.coverImage = (ImageView) view.findViewById(R.id.coverImage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.itemCategory.setText(categoryList.get(position).getItemCategory());
        holder.region.setText(categoryList.get(position).getRegion());

        //************************************************
        // show The Image in a ImageView
        new DownloadImageTask(holder.coverImage)
                .execute(categoryList.get(position).getCoverImage());

        //************************************************

        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to ItemListViewActivity Class
                Intent intent = new Intent(context, ItemListViewActivity.class);
                // Pass all data itemCategory
                intent.putExtra("itemCategory",
                        (categoryList.get(position).getItemCategory()));
                // Pass all data region
                intent.putExtra("region",
                        (categoryList.get(position).getRegion()));
                intent.putExtra("coverImage",
                        (categoryList.get(position).getCoverImage()));
                // Start ItemListViewActivity Class
                context.startActivity(intent);
            }
        });
        return view;
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
            animFadein = AnimationUtils.loadAnimation(context,
                    R.anim.fade_in);
            bmImage.startAnimation(animFadein);
        }
    }
    //******************************************************

}