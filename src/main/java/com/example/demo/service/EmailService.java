package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class EmailService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";
    private final String API_KEY = System.getenv("BREVO_API_KEY");

    private void sendEmail(String toEmail, String subject, String content) {

        if (API_KEY == null) {
            System.err.println("BREVO_API_KEY missing. Email skipped.");
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", API_KEY);

        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of("name", "Bakery Shop", "email", "no-reply@bakery.com"));
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

    // OTP
    public void sendOtp(String toEmail, String otp) {
        String body = "Your OTP is: " + otp + "\n\nDo not share this OTP.";
        sendEmail(toEmail, "Email Verification - OTP", body);
    }

    // User Order Email
    public void sendOrderConfirmation(String toEmail, Order order, List<OrderItem> items) {

        StringBuilder body = new StringBuilder();
        body.append("Hello ").append(order.getName()).append("\n\nOrder Confirmed!\n\n");

        for (OrderItem item : items) {
            body.append(item.getName())
                .append(" x ")
                .append(item.getQty())
                .append("\n");
        }

        body.append("\nTotal: ₹").append(order.getTotalAmount());

        sendEmail(toEmail,
                "Order #" + order.getId() + " Confirmed",
                body.toString());
    }

    // Admin Email
    public void sendAdminOrderNotification(Order order, List<OrderItem> items) {

        String admin = "sivavkvs@gmail.com";

        StringBuilder body = new StringBuilder();
        body.append("New Order #").append(order.getId()).append("\n");
        body.append("Customer: ").append(order.getName()).append("\n");
        body.append("Total: ₹").append(order.getTotalAmount()).append("\n");

        sendEmail(admin,
                "New Order #" + order.getId(),
                body.toString());
    }
}
