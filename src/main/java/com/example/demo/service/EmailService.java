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

    private final JavaMailSender mailSender;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // ================= COMMON METHOD =================
    private void sendMail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("✅ Mail sent to " + to);

        } catch (Exception e) {
            System.err.println("❌ Mail failed: " + e.getMessage());
        }
    }

    // ================= OTP EMAIL =================
    public void sendOtp(String toEmail, String otp) {
        String body =
                "Your OTP is: " + otp +
                "\n\nDo not share this OTP with anyone." +
                "\n\n- " + senderName;

        sendMail(toEmail, "Email Verification - OTP", body);
    }

    // ================= ORDER CONFIRMATION =================
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

        sendMail(toEmail,
                "Order #" + order.getId() + " Confirmed",
                body.toString());
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

        sendMail(senderEmail,
                "New Order #" + order.getId(),
                body.toString());
    }
}
