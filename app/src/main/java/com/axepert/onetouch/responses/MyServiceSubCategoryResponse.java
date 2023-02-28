package com.axepert.onetouch.responses;

import java.util.List;

public class MyServiceSubCategoryResponse {
    public String status;
    public int code;
    public List<Datum> data;

    public static class Datum{
        public String image;
        public String name;
        public CatData cat_data;

        public String getImage() {
            return image;
        }

        public String getName() {
            return name;
        }

        public CatData getCat_data() {
            return cat_data;
        }

        public static class CatData{
            public String cat_name;
            public String cat_image;

            public String getCat_name() {
                return cat_name;
            }

            public String getCat_image() {
                return cat_image;
            }
        }

    }
}
