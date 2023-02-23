package com.axepert.onetouch.responses;

import java.util.List;

public class OrdersResponse {
    public String status;
    public int code;
    public List<Datum> data;

    public class Datum{
        public String id;
        public String product_image;
        public String vendor_url;
        public String order_id;
        public String txn_id;
        public String customer_name;
        public String customer_email;
        public String customer_mobile;
        public String product_id;
        public String product_details;
        public String priceperitem;
        public String quantity;
        public String total_price;
        public String order_date;
        public String shipping_address;
        public String payment_status;
        public String order_status;
        public String payment_type;

        public String getId() {
            return id;
        }

        public String getProduct_image() {
            return product_image;
        }

        public String getVendor_url() {
            return vendor_url;
        }

        public String getOrder_id() {
            return order_id;
        }

        public String getTxn_id() {
            return txn_id;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public String getCustomer_email() {
            return customer_email;
        }

        public String getCustomer_mobile() {
            return customer_mobile;
        }

        public String getProduct_id() {
            return product_id;
        }

        public String getProduct_details() {
            return product_details;
        }

        public String getPriceperitem() {
            return priceperitem;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getTotal_price() {
            return total_price;
        }

        public String getOrder_date() {
            return order_date;
        }

        public String getShipping_address() {
            return shipping_address;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public String getOrder_status() {
            return order_status;
        }

        public String getPayment_type() {
            return payment_type;
        }
    }

}
