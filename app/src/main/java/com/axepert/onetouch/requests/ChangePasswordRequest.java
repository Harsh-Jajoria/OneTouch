package com.axepert.onetouch.requests;

public class ChangePasswordRequest {
    private String email;
    private String current_password;
    private String password;

    public ChangePasswordRequest(String email, String current_password, String password) {
        this.email = email;
        this.current_password = current_password;
        this.password = password;
    }
}
