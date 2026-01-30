package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class OrderItemDto {

    @Min(value = 1, message = "Product ID must be positive")
    private Long productId;

    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Product name too long")
    private String name;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 100, message = "Quantity cannot exceed 100")
    private int qty;

    @Min(value = 0, message = "Price cannot be negative")
    private double price;

    @Min(value = 0, message = "GST cannot be negative")
    @Max(value = 50, message = "GST cannot exceed 50%")
    private int gst;

    // Default constructor
    public OrderItemDto() {}

    // Getters & Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getGst() { return gst; }
    public void setGst(int gst) { this.gst = gst; }
}
