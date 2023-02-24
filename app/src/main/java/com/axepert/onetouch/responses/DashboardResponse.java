package com.axepert.onetouch.responses;

public class DashboardResponse {
    public String status;
    public int code;
    public Data data;

    public static class Data{
        public String category_count;
        public String subcategory_count;
        public String review_count;

        public String getCategory_count() {
            return category_count;
        }

        public String getSubcategory_count() {
            return subcategory_count;
        }

        public String getReview_count() {
            return review_count;
        }
    }
}
