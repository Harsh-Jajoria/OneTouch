package com.axepert.onetouch.responses;

public class Category {
    public String id;
    public String category_name;
    public String category_description;
    public String category_icon;
    public String category_slug;
    public int product_count;

    public String getId() {
        return id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCategory_description() {
        return category_description;
    }

    public String getCategory_icon() {
        return category_icon;
    }

    public String getCategory_slug() {
        return category_slug;
    }

    public int getProduct_count() {
        return product_count;
    }
}
