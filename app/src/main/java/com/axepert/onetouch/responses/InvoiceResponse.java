package com.axepert.onetouch.responses;

import java.util.List;

public class InvoiceResponse {
    public String status;
    public int code;
    public List<Datum> data;

    public class Datum{
        public String id;
        public String customer_name;
        public String customer_email;
        public String customer_mobile;
        public String pin;
        public String priceperitem;
        public String order_id;
        public String txn_id;
        public String user_id;
        public String product_details;
        public String total_price;
        public String order_date;
        public String shipping_address;
        public String payment_status;
        public String payment_type;
        public String razorpay_payment_id;
        public String order_status;
        public String price;
        public String quantity;

        public String getId() {
            return id;
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

        public String getPin() {
            return pin;
        }

        public String getPriceperitem() {
            return priceperitem;
        }

        public String getOrder_id() {
            return order_id;
        }

        public String getTxn_id() {
            return txn_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getProduct_details() {
            return product_details;
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

        public String getPayment_type() {
            return payment_type;
        }

        public String getRazorpay_payment_id() {
            return razorpay_payment_id;
        }

        public String getOrder_status() {
            return order_status;
        }

        public String getPrice() {
            return price;
        }

        public String getQuantity() {
            return quantity;
        }
    }
}
