package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class EmailService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    // ================= COMMON EMAIL METHOD =================
    private void sendEmail(String toEmail, String subject, String content) {

        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("❌ BREVO API KEY missing. Email skipped.");
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey.trim());

        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of(
                "name", senderName,
                "email", senderEmail
        ));
        body.put("to", List.of(Map.of("email", toEmail)));
        body.put("subject", subject);
        body.put("textContent", content);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(BREVO_URL, request, String.class);
            System.out.println("✅ Email sent to " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Email failed: " + e.getMessage());
        }
    }

    // ================= OTP EMAIL =================
    public void sendOtp(String toEmail, String otp) {
        String body = "Your OTP is: " + otp +
                "\n\nDo not share this OTP with anyone." +
                "\n\n- " + senderName;
        sendEmail(toEmail, "Email Verification - OTP", body);
    }

    // ================= USER ORDER EMAIL =================
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

        sendEmail(toEmail,
                "Order #" + order.getId() + " Confirmed",
                body.toString());
    }

    // ================= ADMIN ORDER EMAIL =================
    public void sendAdminOrderNotification(Order order, List<OrderItem> items) {

        StringBuilder body = new StringBuilder();
        body.append("New Order Received!\n\n");
        body.append("Order ID: ").append(order.getId()).append("\n");
        body.append("Customer: ").append(order.getName()).append("\n");
        body.append("Total: ₹").append(order.getTotalAmount()).append("\n\n");

        body.append("Items:\n");
        for (OrderItem item : items) {
            body.append(item.getName())
                .append(" x ")
                .append(item.getQty())
                .append("\n");
        }

        sendEmail(senderEmail,
                "New Order #" + order.getId(),
                body.toString());
    }
}
