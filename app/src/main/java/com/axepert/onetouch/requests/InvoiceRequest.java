package com.axepert.onetouch.requests;

public class InvoiceRequest {
    private String url, order_id;

    public InvoiceRequest(String url, String order_id) {
        this.url = url;
        this.order_id = order_id;
    }
}
