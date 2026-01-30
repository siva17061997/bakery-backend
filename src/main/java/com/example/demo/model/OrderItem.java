package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    @Min(value = 1)
    private Long productId;

    @Column(name = "name", nullable = false, length = 200)
    @NotBlank
    @Size(max = 200)
    private String name;

    @Column(name = "qty", nullable = false)
    @Min(value = 1)
    @Max(value = 100)
    private int qty;

    @Column(name = "price", nullable = false)
    @Min(value = 0)
    private double price;

    @Column(name = "gst", nullable = false)
    @Min(value = 0)
    @Max(value = 50)
    private int gst;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    // ✅ Constructors
    public OrderItem() {}

    /* ================= GETTERS & SETTERS ================= */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
}
