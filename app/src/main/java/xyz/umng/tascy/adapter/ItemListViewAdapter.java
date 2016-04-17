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
import xyz.umng.tascy.activity.SingleItemViewActivity;
import xyz.umng.tascy.model.Item;

/**
 * Created by umang on 18-04-2016.
 */
public class ItemListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    private List<Item> ItemList = null;
    private ArrayList<Item> arrayList;

    // Animation
    Animation animFadein;

    public ItemListViewAdapter(Context context,
                           List<Item> ItemList) {
        this.context = context;
        this.ItemList = ItemList;
        inflater = LayoutInflater.from(context);
        this.arrayList = new ArrayList<Item>();
        this.arrayList.addAll(ItemList);
    }

    public class ViewHolder {
        TextView itemName;
        TextView price;
        ImageView itemImage;
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
            view = inflater.inflate(R.layout.itemlistview_item, null);
            // Locate the TextViews in itemlistview_item.xml
            holder.itemName = (TextView) view.findViewById(R.id.itemName);
            holder.price = (TextView) view.findViewById(R.id.price);
            // Locate the ImageView in itemlistview_item.xml
            holder.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.itemName.setText(ItemList.get(position).getItemName());
        holder.price.setText(ItemList.get(position).getPrice());

        //************************************************
        // show The Image in a ImageView
        new DownloadImageTask(holder.itemImage)
                .execute(ItemList.get(position).getItemImage());

        //************************************************

        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemViewActivity Class
                Intent intent = new Intent(context, SingleItemViewActivity.class);
                // Pass all data itemName
                intent.putExtra("itemName",
                        (ItemList.get(position).getItemName()));
                // Pass all data price
                intent.putExtra("price",
                        (ItemList.get(position).getPrice()));
                intent.putExtra("itemImage",
                        (ItemList.get(position).getItemImage()));
                // Start SingleItemViewActivity Class
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