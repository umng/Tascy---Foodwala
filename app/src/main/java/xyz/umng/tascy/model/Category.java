package xyz.umng.tascy.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Umang on 2/22/2016.
 */
@ParseClassName("Category")
public class Category extends ParseObject {

    public String getCategoryName(){
        return getString("categoryName");
    }

    public void setCategoryName(String categoryName){
        put("categoryName", categoryName);
    }

    public String getRegion(){
        return getString("region");
    }

    public void setRegion(String region){
        put("region", region);
    }

    @Override
    public String toString() {
        return getString("categoryName") + "\n" + getString("region");
    }
}
