package com.example.demo.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class OrderDto {

    @NotBlank(message = "User ID is required")
    @Size(min = 1, max = 100, message = "User ID must be between 1-100 characters")
    private String userId;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2-100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    @Size(max = 100, message = "Email too long")
    private String email;

    @NotBlank(message = "Mobile is required")
    @Size(min = 10, max = 15, message = "Mobile must be 10-15 digits")
    private String mobile;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 500, message = "Address must be 5-500 characters")
    private String address;

    @NotBlank(message = "Pincode is required")
    @Size(min = 6, max = 10, message = "Valid pincode required")
    private String pincode;

    @NotBlank(message = "Delivery type is required")
    @Size(min = 1, max = 50)
    private String deliveryType;

    @NotBlank(message = "Payment method is required")
    @Size(min = 1, max = 50)
    private String paymentMethod;

    @NotEmpty(message = "At least one item required")
    @Size(min = 1, max = 50, message = "1-50 items allowed")
    private List<OrderItemDto> items;

    // ✅ Default constructor
    public OrderDto() {}

    /* ================= GETTERS & SETTERS ================= */

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getDeliveryType() { return deliveryType; }
    public void setDeliveryType(String deliveryType) { this.deliveryType = deliveryType; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public List<OrderItemDto> getItems() { return items; }
    public void setItems(List<OrderItemDto> items) { this.items = items; }
}
