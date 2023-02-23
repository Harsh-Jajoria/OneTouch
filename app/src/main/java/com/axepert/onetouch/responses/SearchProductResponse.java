package com.axepert.onetouch.responses;

import java.util.List;

public class SearchProductResponse {
    public String status;
    public int code;
    public List<Datum> data;

    public class Datum{
        public String id;
        public String name;
        public String slug;
        public String key;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getSlug() {
            return slug;
        }

        public String getKey() {
            return key;
        }
    }

}
