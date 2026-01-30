package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String toEmail, String otp) {
        if (toEmail == null || otp == null) return;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Email Verification - OTP");
        message.setText("Your OTP is: " + otp + "\n\nDo not share this OTP with anyone.");
        mailSender.send(message);
    }

    // 🔥 USER CONFIRMATION EMAIL - CLEAN & PROFESSIONAL
    public void sendOrderConfirmation(String toEmail, Order order, List<OrderItem> items) {
        System.out.println("📧 [USER EMAIL] Starting for Order #" + order.getId() + " → " + toEmail);
        
        try {
            StringBuilder body = new StringBuilder();
            body.append("🎉 Hello ").append(order.getName()).append("!\n\n");
            body.append("Thank you for your order at Bakery Shop!\n\n");
            
            body.append("📋 ORDER DETAILS\n");
            body.append("Order ID: #").append(order.getId()).append("\n");
            body.append("Mobile: ").append(order.getMobile()).append("\n");
            body.append("Address: ").append(order.getAddress()).append("\n");
            body.append("Pincode: ").append(order.getPincode()).append("\n");
            body.append("Delivery: ").append(order.getDeliveryType()).append("\n");
            body.append("Payment: ").append(order.getPaymentMethod()).append("\n\n");

            body.append("🛒 YOUR ORDER ITEMS:\n");
            double subtotal = 0;
            double gstTotal = 0;
            
            for (OrderItem item : items) {
                if (item == null) continue;
                
                double itemSubtotal = item.getPrice() * item.getQty();
                double itemGst = (itemSubtotal * item.getGst()) / 100;
                
                subtotal += itemSubtotal;
                gstTotal += itemGst;
                
                body.append("🍰 ").append(item.getName()).append("\n");
                body.append("   Quantity: ").append(item.getQty()).append("\n");
                body.append("   Price: ₹").append(String.format("%.2f", item.getPrice())).append("/unit\n");
                body.append("   Subtotal: ₹").append(String.format("%.2f", itemSubtotal)).append("\n");
                body.append("   GST (").append(item.getGst()).append("%): ₹").append(String.format("%.2f", itemGst)).append("\n\n");
            }
            
            body.append("💰 BILL SUMMARY:\n");
            body.append("Subtotal: ₹").append(String.format("%.2f", subtotal)).append("\n");
            body.append("GST Total: ₹").append(String.format("%.2f", gstTotal)).append("\n");
            body.append("------------------------\n");
            body.append("TOTAL: ₹").append(String.format("%.2f", order.getTotalAmount())).append("\n\n");
            
            body.append("✅ Your order is confirmed!\n");
            body.append("📦 We'll prepare and deliver soon.\n\n");
            body.append("Thank you for choosing Bakery Shop 🍰");

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("✅ Order #" + order.getId() + " Confirmed - Bakery Shop");
            message.setText(body.toString());
            
            mailSender.send(message);
            System.out.println("✅ [USER EMAIL] SUCCESS - Order #" + order.getId());
            
        } catch (Exception e) {
            System.err.println("❌ [USER EMAIL] FAILED Order #" + order.getId() + ": " + e.getMessage());
        }
    }

    // 🔥 ADMIN NOTIFICATION EMAIL - CLEAN & PROFESSIONAL
    public void sendAdminOrderNotification(Order order, List<OrderItem> items) {
        String ADMIN_EMAIL = "sivavkvs@gmail.com";
        System.out.println("🔔 [ADMIN EMAIL] Starting for Order #" + order.getId());
        
        try {
            StringBuilder body = new StringBuilder();
            body.append("🚨 NEW ORDER ALERT!\n\n");
            
            body.append("🆕 Order ID: #").append(order.getId()).append("\n");
            body.append("👤 Customer: ").append(order.getName()).append("\n");
            body.append("📧 Email: ").append(order.getEmail()).append("\n");
            body.append("📱 Mobile: ").append(order.getMobile()).append("\n");
            body.append("📍 Address: ").append(order.getAddress()).append(", ").append(order.getPincode()).append("\n\n");

            body.append("🛒 CUSTOMER ORDERED ITEMS:\n");
            double subtotal = 0;
            double gstTotal = 0;
            
            for (OrderItem item : items) {
                if (item == null) continue;
                
                double itemSubtotal = item.getPrice() * item.getQty();
                double itemGst = (itemSubtotal * item.getGst()) / 100;
                
                subtotal += itemSubtotal;
                gstTotal += itemGst;
                
                body.append("📦 ").append(item.getName()).append("\n");
                body.append("   Quantity: ").append(item.getQty()).append("\n");
                body.append("   Unit Price: ₹").append(String.format("%.2f", item.getPrice())).append("\n");
                body.append("   Item Total: ₹").append(String.format("%.2f", itemSubtotal)).append("\n");
                body.append("   GST (").append(item.getGst()).append("%): ₹").append(String.format("%.2f", itemGst)).append("\n\n");
            }
            
            body.append("💰 ORDER SUMMARY:\n");
            body.append("Subtotal: ₹").append(String.format("%.2f", subtotal)).append("\n");
            body.append("GST: ₹").append(String.format("%.2f", gstTotal)).append("\n");
            body.append("------------------------\n");
            body.append("TOTAL: ₹").append(String.format("%.2f", order.getTotalAmount())).append("\n\n");
            
            body.append("💳 Payment: ").append(order.getPaymentMethod()).append("\n");
            body.append("🚚 Delivery: ").append(order.getDeliveryType()).append("\n");
            body.append("📊 Status: ").append(order.getStatus()).append("\n\n");
            
            body.append("⚡ IMMEDIATE ACTION REQUIRED:\n");
            body.append("1. Check stock availability\n");
            body.append("2. Prepare order\n");
            body.append("3. Update status to 'PREPARING'\n\n");
            body.append("— Bakery Shop Admin Dashboard 🍰");

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(ADMIN_EMAIL);
            message.setSubject("🆕 NEW ORDER #" + order.getId() + " | ₹" + String.format("%.2f", order.getTotalAmount()) + " | " + order.getName());
            message.setText(body.toString());
            
            mailSender.send(message);
            System.out.println("✅ [ADMIN EMAIL] SUCCESS to " + ADMIN_EMAIL + " - Order #" + order.getId());
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN EMAIL] FAILED Order #" + order.getId() + ": " + e.getMessage());
        }
    }
}
