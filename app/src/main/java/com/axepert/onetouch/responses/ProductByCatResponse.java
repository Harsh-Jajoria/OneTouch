package com.axepert.onetouch.responses;

import java.util.List;

public class ProductByCatResponse {
    public String status;
    public int code;
    public List<Datum> data;

    public class Datum{
        public String slug;
        public String brand_name;
        public String brand;
        public String id;
        public String sub_category;
        public String varients;
        public String quantity;
        public String mrp;
        public String price;
        public String product_image;
        public String category_slug;
        public String category_name;
        public String flag;
        public String name;
        public String phsn;
        public String product_heading;
        public String psku;
        public String pvsku;
        public String varient_id;
        public String color_name;

        public String getSlug() {
            return slug;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public String getBrand() {
            return brand;
        }

        public String getId() {
            return id;
        }

        public String getSub_category() {
            return sub_category;
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

        public String getProduct_image() {
            return product_image;
        }

        public String getCategory_slug() {
            return category_slug;
        }

        public String getCategory_name() {
            return category_name;
        }

        public String getFlag() {
            return flag;
        }

        public String getName() {
            return name;
        }

        public String getPhsn() {
            return phsn;
        }

        public String getProduct_heading() {
            return product_heading;
        }

        public String getPsku() {
            return psku;
        }

        public String getPvsku() {
            return pvsku;
        }

        public String getVarient_id() {
            return varient_id;
        }

        public String getColor_name() {
            return color_name;
        }
    }

}
