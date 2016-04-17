package xyz.umng.tascy.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Umang on 2/28/2016.
 */
@ParseClassName("Item")
public class Item extends ParseObject {

    public String getItemName(){
        return getString("itemName");
    }

    public void setItemName(String itemName){
        put("itemName", itemName);
    }

    public String getCategoryName(){
        return getString("categoryName");
    }

    public void setCategoryName(String categoryName){
        put("categoryName", categoryName);
    }

    public ParseFile getItemImage(){
        return getParseFile("itemImage");
    }

    public void setItemImage(ParseFile itemImage){
        put("itemImage", itemImage);
    }

    public String getObjectID(){
        return getObjectId();
    }

    public void setObjectID(String objectID){
        put("objectId", objectID);
    }

    public static ParseQuery<Item> getQuery(){
        return ParseQuery.getQuery(Item.class);
    }

    @Override
    public String toString(){
        return getString("itemName") + "\n" + getString("itemCategory");
    }
}
