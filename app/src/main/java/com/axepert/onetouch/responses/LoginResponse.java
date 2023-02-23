package com.axepert.onetouch.responses;

public class LoginResponse {
    public String status;
    public int code;
    public Data data;

    public class Data{
        public String id;
        public String first_name;
        public String email;
        public String password;
        public String contact;
        public String image;
        public String cat_id;
        public String subcat_id;
        public String gstin;
        public String shop_name;
        public String shop_address;
        public String rating;
        public String description;
        public String seller_video;

        public String getId() {
            return id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getContact() {
            return contact;
        }

        public String getImage() {
            return image;
        }

        public String getCat_id() {
            return cat_id;
        }

        public String getSubcat_id() {
            return subcat_id;
        }

        public String getGstin() {
            return gstin;
        }

        public String getShop_name() {
            return shop_name;
        }

        public String getShop_address() {
            return shop_address;
        }

        public String getRating() {
            return rating;
        }

        public String getDescription() {
            return description;
        }

        public String getSeller_video() {
            return seller_video;
        }
    }
}
