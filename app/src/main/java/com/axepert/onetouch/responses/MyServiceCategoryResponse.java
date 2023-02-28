package com.axepert.onetouch.responses;

import java.util.List;

public class MyServiceCategoryResponse {
    public String status;
    public int code;
    public List<Datum> data;

    public static class Datum {
        public String id;
        public String image;
        public String name;

        public String getId() {
            return id;
        }

        public String getImage() {
            return image;
        }

        public String getName() {
            return name;
        }
    }
}
