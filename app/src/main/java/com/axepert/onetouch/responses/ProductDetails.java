package com.axepert.onetouch.responses;

import java.util.List;

public class ProductDetails {

    public String id;
    public String vendor_url;
    public String vendor_id;
    public String name;
    public String slug;
    public String product_image;
    public String brand;
    public String description;
    public String manufacturer_description;
    public String return_duration;
    public String category;
    public String subcategory;
    public String discount;
    public String best_selling;
    public String mynew;
    public String cat_name;
    public List<Variants> provariant;

    public String getCat_name() {
        return cat_name;
    }

    public String getId() {
        return id;
    }

    public String getVendor_url() {
        return vendor_url;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public String getManufacturer_description() {
        return manufacturer_description;
    }

    public String getReturn_duration() {
        return return_duration;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getDiscount() {
        return discount;
    }

    public String getBest_selling() {
        return best_selling;
    }

    public String getMynew() {
        return mynew;
    }

    public List<Variants> getProvariant() {
        return provariant;
    }
}
