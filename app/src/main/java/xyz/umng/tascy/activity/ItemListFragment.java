package xyz.umng.tascy.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import xyz.umng.tascy.R;
import xyz.umng.tascy.model.Category;
import xyz.umng.tascy.model.Item;

public class ItemListFragment extends ListFragment {
    private CustomParseQueryAdapter mAdapter;
    public static final String ARG_PAGE = "ARG_PAGE";
    private String category;

    public static ItemListFragment create(int page, String type){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString("type", type);
        ItemListFragment fragment = new ItemListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        category = getArguments().getString("type");
        this.setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);
        mAdapter = new CustomParseQueryAdapter(getActivity());
        setListAdapter(mAdapter);
        mAdapter.loadObjects();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    public class CustomParseQueryAdapter extends ParseQueryAdapter<Item> {
        public CustomParseQueryAdapter(Context context){
            super(context, new QueryFactory<Item>(){
                public ParseQuery create(){
                    ParseQuery query = new ParseQuery("Item");
                    query.whereEqualTo("itemCategory", category);
                    return query;
                }
            });
        }

        @Override
        public View getItemView(Item item, View v, ViewGroup parent){
            if (v == null){
                v = View.inflate(getContext(), R.layout.item_list_view, null);
            }

            super.getItemView(item, v, parent);

            ParseImageView petImage = (ParseImageView)v.findViewById(R.id.itemImage);
            ParseFile imageFile = item.getItemImage();
            if (imageFile != null){
                petImage.setParseFile(imageFile);
                petImage.loadInBackground();
            }


            TextView petName = (TextView)v.findViewById(R.id.itemName);
            petName.setText(item.getItemName());


            TextView petBreed = (TextView)v.findViewById(R.id.itemCategory);
            petBreed.setText(item.getCategoryName());

            return v;
        }
    }
}
