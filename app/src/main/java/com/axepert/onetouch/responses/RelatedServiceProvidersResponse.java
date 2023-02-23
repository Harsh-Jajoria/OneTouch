package com.axepert.onetouch.responses;

import java.util.ArrayList;
import java.util.List;

public class RelatedServiceProvidersResponse {
    public String status;
    public int code;
    public List<Datum> data;

    public class Datum {
        public String id;
        public String service_id;
        public String name;
        public String email;
        public String mobile;
        public String address;
        public String aadhar_id;
        public String man_image;

        public String getId() {
            return id;
        }

        public String getService_id() {
            return service_id;
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

        public String getAadhar_id() {
            return aadhar_id;
        }

        public String getMan_image() {
            return man_image;
        }
    }

}
