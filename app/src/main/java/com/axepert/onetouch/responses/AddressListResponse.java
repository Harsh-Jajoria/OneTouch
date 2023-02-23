package com.axepert.onetouch.responses;

import java.io.Serializable;
import java.util.List;

public class AddressListResponse {
    public String status;
    public int code;
    public List<Datum> data;

    public static class Datum implements Serializable {
        public String id;
        public String landmark;
        public String name;
        public String email;
        public String mobile;
        public String address;
        public String city;
        public String state;
        public String pincode;
        public String make_primary;
        public String user_id;

        public String getId() {
            return id;
        }

        public String getLandmark() {
            return landmark;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getMobile() {
            return mobile;
        }

        public String getAddress() {
            return address;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String getPincode() {
            return pincode;
        }

        public String getMake_primary() {
            return make_primary;
        }

        public String getUser_id() {
            return user_id;
        }
    }
}
