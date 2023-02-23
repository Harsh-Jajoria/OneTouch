package com.axepert.onetouch.requests;

public class BookServiceRequest {
    private String service_id, name, email, contact, address, query_date, time_slot, note;

    public BookServiceRequest(String service_id, String name, String email, String contact, String address, String query_date, String time_slot, String note) {
        this.service_id = service_id;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.query_date = query_date;
        this.time_slot = time_slot;
        this.note = note;
    }
}
