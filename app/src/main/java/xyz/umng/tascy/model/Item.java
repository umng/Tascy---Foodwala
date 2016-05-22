package xyz.umng.tascy.model;

/**
 * Created by umang on 18-04-2016.
 */
public class Item {
    private String itemName;
    private String price;
    private String itemImage;
    private String description;
    private String ingredients;
    private String itemCategory;

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemCategory() {

        return itemCategory;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}

