package com.axepert.onetouch.requests;

public class EditDealerProfileRequest {
    private String id, name, contact, shop_name, cat_id, subcat_id, shop_address, description, gstin, image;

    public EditDealerProfileRequest(String id, String name, String contact, String shop_name, String cat_id, String subcat_id, String shop_address, String description, String gstin, String image) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.shop_name = shop_name;
        this.cat_id = cat_id;
        this.subcat_id = subcat_id;
        this.shop_address = shop_address;
        this.description = description;
        this.gstin = gstin;
        this.image = image;
    }
}
