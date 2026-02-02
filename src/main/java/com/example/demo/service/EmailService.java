package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    // ================= OTP EMAIL =================
    public void sendOtp(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(toEmail);
        message.setSubject("Email Verification - OTP");
        message.setText(
                "Your OTP is: " + otp +
                "\n\nDo not share this OTP with anyone." +
                "\n\n- " + senderName
        );

        mailSender.send(message);
        System.out.println("✅ OTP mail sent to " + toEmail);
    }

    // ================= ORDER CONFIRMATION EMAIL =================
    public void sendOrderConfirmation(String toEmail, Order order, List<OrderItem> items) {

        StringBuilder body = new StringBuilder();
        body.append("Hello ").append(order.getName()).append(",\n\n");
        body.append("Your order has been confirmed!\n\n");

        for (OrderItem item : items) {
            body.append(item.getName())
                .append(" x ")
                .append(item.getQty())
                .append("\n");
        }

        body.append("\nTotal Amount: ₹").append(order.getTotalAmount());
        body.append("\n\nThank you for shopping with us!\n").append(senderName);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(toEmail);
        message.setSubject("Order #" + order.getId() + " Confirmed");
        message.setText(body.toString());

        mailSender.send(message);
        System.out.println("✅ Order mail sent to " + toEmail);
    }

    // ================= ADMIN ORDER NOTIFICATION =================
    public void sendAdminOrderNotification(Order order, List<OrderItem> items) {

        StringBuilder body = new StringBuilder();
        body.append("New Order Received!\n\n");
        body.append("Order ID: ").append(order.getId()).append("\n");
        body.append("Customer: ").append(order.getName()).append("\n");
        body.append("Total: ₹").append(order.getTotalAmount()).append("\n\n");

        for (OrderItem item : items) {
            body.append(item.getName())
                .append(" x ")
                .append(item.getQty())
                .append("\n");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(senderEmail); // admin mail
        message.setSubject("New Order #" + order.getId());
        message.setText(body.toString());

        mailSender.send(message);
        System.out.println("✅ Admin mail sent");
    }
}
