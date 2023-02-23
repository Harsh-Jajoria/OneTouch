package com.axepert.onetouch.responses;

import java.util.List;

public class Variants {
    public String id;
    public String product_id;
    public String varients;
    public String quantity;
    public String mrp;
    public String price;
    public String specification;
    public List<Object> procolor;
    public List<ProductImage> proimage;
    public boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getId() {
        return id;
    }

    public String getProduct_id() {
        return product_id;
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

    public String getSpecification() {
        return specification;
    }

    public List<Object> getProcolor() {
        return procolor;
    }

    public List<ProductImage> getProimage() {
        return proimage;
    }
}
