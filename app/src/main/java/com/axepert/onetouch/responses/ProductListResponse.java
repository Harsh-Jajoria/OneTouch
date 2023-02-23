package com.axepert.onetouch.responses;

import java.util.List;

public class ProductListResponse {
    public String status;
    public int code;
    public List<Datum> data;
    public int count;

    public static class Datum{
        public String id;
        public String name;
        public String brand_name;
        public String slug;
        public String product_image;
        public String category_name;
        public String sub_category;
        public String sku_code;
        public String hsn_code;
        public String pv_sku_code;
        public String varients;
        public String product_heading;
        public String quantity;
        public String mrp;
        public String varient_id;
        public String price;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getBrand_name() {
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

        public String getSub_category() {
            return sub_category;
        }

        public String getSku_code() {
            return sku_code;
        }

        public String getHsn_code() {
            return hsn_code;
        }

        public String getPv_sku_code() {
            return pv_sku_code;
        }

        public String getVarients() {
            return varients;
        }

        public String getProduct_heading() {
            return product_heading;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getMrp() {
            return mrp;
        }

        public String getVarient_id() {
            return varient_id;
        }

        public String getPrice() {
            return price;
        }
    }

}
