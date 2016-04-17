package xyz.umng.tascy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.umng.tascy.R;
import xyz.umng.tascy.model.DataItem;

/**
 * Created by Umang on 2/12/2016.
 */
public class CustomAdapter extends BaseAdapter {

    ArrayList<DataItem> data;
    Context context;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context context, ArrayList<DataItem> data)
    {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null)
        {
            view = inflater.inflate(R.layout.list_view_category, null);
        }
        TextView categoryNameText = (TextView) view.findViewById(R.id.categoryValue);
        TextView regionText = (TextView) view.findViewById(R.id.regionValue);
        DataItem item = data.get(position);
        categoryNameText.setText(item.categoryName);
        regionText.setText(item.region);
        return view;
    }
}
