package com.axepert.onetouch.responses;

import java.util.List;

public class RelatedProductResponse {
    public String status;
    public int code;
    public Data data;

    public class Data{
        public int count;
        public List<TrendingProduct> products;

        public int getCount() {
            return count;
        }

        public List<TrendingProduct> getProducts() {
            return products;
        }
    }

}
