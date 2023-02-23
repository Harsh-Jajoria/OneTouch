package com.axepert.onetouch.responses;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "products")
public class TrendingProduct implements Serializable {

    @NonNull
    @PrimaryKey
    public String id;

    public String name;
    public String brand_name;
    public String slug;
    public String product_image;
    public String category_name;
    public String varients;
    public String quantity;
    public String mrp;
    public String price;
    public String varient_id;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Object getBrand_name() {
        return brand_name;
    }

    public String getSlug() {
        return slug;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getVarients() {
        return varients;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getMrp() {
        return mrp;
    }

    public String getPrice() {
        return price;
    }

    public String getVarient_id() {
        return varient_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setVarients(String varients) {
        this.varients = varients;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setVarient_id(String varient_id) {
        this.varient_id = varient_id;
    }
}
