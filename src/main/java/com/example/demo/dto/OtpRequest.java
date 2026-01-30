package com.example.demo.dto;

public class OtpRequest {

    private String email;
    private String otp;

    /* ========== GETTERS & SETTERS ========== */

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getOtp() {
        return otp;
    }
 
    public void setOtp(String otp) {
        this.otp = otp;
    }
}
