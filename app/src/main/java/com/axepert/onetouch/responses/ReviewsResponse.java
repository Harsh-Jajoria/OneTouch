package com.axepert.onetouch.responses;

import java.util.List;

public class ReviewsResponse {
    public String status;
    public int code;
    public List<Datum> data;

    public static class Datum{
        public String name;
        public String review;

        public String getName() {
            return name;
        }

        public String getReview() {
            return review;
        }
    }

}
