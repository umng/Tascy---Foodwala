package xyz.umng.tascy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import xyz.umng.tascy.R;
import xyz.umng.tascy.activity.ItemDetailsViewActivity;
import xyz.umng.tascy.activity.ItemListViewActivity;
import xyz.umng.tascy.model.SearchItem;

/**
 * Created by umang on 25-04-2016.
 */
public class SearchItemListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    private List<SearchItem> ItemList = null;
    private ArrayList<SearchItem> arrayList;

    // Animation
    Animation animFadein;

    public SearchItemListViewAdapter(Context context,
                               List<SearchItem> ItemList) {
        this.context = context;
        this.ItemList = ItemList;
        inflater = LayoutInflater.from(context);
        this.arrayList = new ArrayList<SearchItem>();
        this.arrayList.addAll(ItemList);
    }

    public class ViewHolder {
        TextView searchItemName;
        TextView price;
        TextView getSearchItemCategory;
        ImageView searchItemImage;
    }

    @Override
    public int getCount() {
        return ItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return ItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.itemlistview_search_item, null);
            // Locate the TextViews in searchItemlistview_searchItem.xml
            holder.searchItemName = (TextView) view.findViewById(R.id.searchItemName);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.getSearchItemCategory = (TextView) view.findViewById(R.id.searchItemCategory);
            // Locate the ImageView in searchItemlistview_searchItem.xml
            holder.searchItemImage = (ImageView) view.findViewById(R.id.searchItemImage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.searchItemName.setText(ItemList.get(position).getItemName());
        holder.price.setText(ItemList.get(position).getPrice());
        holder.getSearchItemCategory.setText(ItemList.get(position).getItemCategory());

        //************************************************
        // show The Image in a ImageView
        new DownloadImageTask(holder.searchItemImage)
                .execute(ItemList.get(position).getItemImage());

        //************************************************

        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single searchItem click data to ItemListViewActivity Class
                Intent intent = new Intent(context, ItemDetailsViewActivity.class);
                // Pass all data searchItemName
                intent.putExtra("ITEM_NAME",
                        (ItemList.get(position).getItemName()));
                // Pass all data price
                intent.putExtra("PRICE",
                        (ItemList.get(position).getPrice()));
                intent.putExtra("ITEM_IMAGE",
                        (ItemList.get(position).getItemImage()));
                intent.putExtra("ITEM_CATEGORY",
                        (ItemList.get(position).getItemCategory()));
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