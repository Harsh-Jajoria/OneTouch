package com.axepert.onetouch.requests;

public class OrderCodRequest {
    private String email;
    private String url;
    private String name;
    private String mobile;
    private String pro_id;
    private String qty;
    private String priceperitem;
    private String price;
    private String total;
    private String order_date;
    private String address;
    private String payment_type;
    private String state;
    private String city;
    private String pincode;
    private String user_id;
    private String product_details;
    private String order_id;
    private String txn_id;

    public OrderCodRequest(String email, String url, String name, String mobile, String pro_id, String qty, String priceperitem, String price, String total, String order_date, String address, String payment_type, String state, String city, String pincode, String user_id, String product_details, String order_id, String txn_id) {
        this.email = email;
        this.url = url;
        this.name = name;
        this.mobile = mobile;
        this.pro_id = pro_id;
        this.qty = qty;
        this.priceperitem = priceperitem;
        this.price = price;
        this.total = total;
        this.order_date = order_date;
        this.address = address;
        this.payment_type = payment_type;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
        this.user_id = user_id;
        this.product_details = product_details;
        this.order_id = order_id;
        this.txn_id = txn_id;
    }
}
