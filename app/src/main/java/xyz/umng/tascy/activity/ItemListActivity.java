package xyz.umng.tascy.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import xyz.umng.tascy.R;

public class ItemListActivity extends FragmentActivity {
    ViewPager pager;
    ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        getActionBar().setTitle("Tascy");

        pager = (ViewPager)findViewById(R.id.pager);
        final ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        categories = new ArrayList<>();

        ParseQuery<ParseObject> query = new ParseQuery<>("Category");
        query.addAscendingOrder("itemCategory");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject type : parseObjects) {
                        String itemCategory = type.getString("itemCategory");
                        categories.add(itemCategory);
                    }
                    pager.setAdapter(adapter);
                }
            }
        });

    }

    private class ViewPagerFragmentAdapter extends FragmentPagerAdapter{
        public ViewPagerFragmentAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position){
            return ItemListFragment.create(position+1, categories.get(position).toString());
        }

        @Override
        public int getCount(){
            return categories.size();
        }

        @Override
        public CharSequence getPageTitle(int position){
            return categories.get(position).toString();
        }
    }
}
