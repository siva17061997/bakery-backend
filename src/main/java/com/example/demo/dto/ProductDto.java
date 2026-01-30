// ProductDto.java
package com.example.demo.dto;

import java.io.Serializable;

public class ProductDto implements Serializable {
    
    private Long id;
    private String name;
    private String imageUrl;
    private String category;
    private String subcategory;
    private Double price;
    private Double mrp;
    private Integer discount;
    private Integer gst;
    private String qtyType;
    private Integer quantity;
    private boolean available;
    
    // Default Constructor
    public ProductDto() {}
    
    // Parameterized Constructor
    public ProductDto(Long id, String name, String imageUrl, String category, String subcategory, 
                      Double price, Double mrp, Integer discount, Integer gst, 
                      String qtyType, Integer quantity, boolean available) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
        this.subcategory = subcategory;
        this.price = price;
        this.mrp = mrp;
        this.discount = discount;
        this.gst = gst;
        this.qtyType = qtyType;
        this.quantity = quantity;
        this.available = available;
    }
    
    // ========== GETTERS ==========
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public String getCategory() {
        return category;
    }
    
    public String getSubcategory() {
        return subcategory;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public Double getMrp() {
        return mrp;
    }
    
    public Integer getDiscount() {
        return discount;
    }
    
    public Integer getGst() {
        return gst;
    }
    
    public String getQtyType() {
        return qtyType;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    // ========== SETTERS ==========
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }
    
    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
    
    public void setGst(Integer gst) {
        this.gst = gst;
    }
    
    public void setQtyType(String qtyType) {
        this.qtyType = qtyType;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
