package com.axepert.onetouch.requests;

public class RegisterRequest {
    private String name, email,contact, password, confirm_password;

    public RegisterRequest(String name, String email, String contact, String password, String confirm_password) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.password = password;
        this.confirm_password = confirm_password;
    }

}
