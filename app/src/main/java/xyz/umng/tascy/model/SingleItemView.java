package xyz.umng.tascy.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import xyz.umng.tascy.R;

/**
 * Created by Umang on 4/7/2016.
 */

public class SingleItemView extends Activity {
    // Declare Variables
    String rank;
    String country;
    String population;
    String flag;
    String position;
    ImageLoader imageLoader = new ImageLoader(this);

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

        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        imageLoader.DisplayImage(flag, imgflag);
    }
}