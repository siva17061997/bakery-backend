package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String userId;

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 100)
    @NotBlank
    @Email
    private String email;

    @Column(name = "mobile", nullable = false, length = 15)
    @NotBlank
    @Size(min = 10, max = 15)
    private String mobile;

    @Column(name = "address", nullable = false, length = 500)
    @NotBlank
    @Size(min = 5, max = 500)
    private String address;

    @Column(name = "pincode", nullable = false, length = 10)
    @NotBlank
    @Size(min = 6, max = 10)
    private String pincode;

    @Column(name = "delivery_type", nullable = false)
    @NotBlank
    private String deliveryType;

    @Column(name = "payment_method", nullable = false)
    @NotBlank
    private String paymentMethod;

    @Column(name = "total_amount", nullable = false)
    @Min(value = 0)
    private double totalAmount;

    @Column(name = "status")  // ✅ SAFE: No nullable=false - fixes migration
    private String status = "PENDING";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    // ✅ Constructors
    public Order() {}

    /* ================= GETTERS & SETTERS ================= */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { 
        this.items = items != null ? items : new ArrayList<>(); 
    }
}
