package com.example.demo.service;

import com.example.demo.dto.OrderDto;
import com.example.demo.dto.OrderItemDto;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final EmailService emailService;

    public OrderService(OrderRepository orderRepository,
                        EmailService emailService) {
        this.orderRepository = orderRepository;
        this.emailService = emailService;
    }

    @Transactional
    public Order placeOrder(OrderDto dto) {

        Order order = new Order();
        order.setUserId(dto.getEmail()); // 🔥 EMAIL
        order.setName(dto.getName());
        order.setEmail(dto.getEmail());
        order.setMobile(dto.getMobile());
        order.setAddress(dto.getAddress());
        order.setPincode(dto.getPincode());
        order.setDeliveryType(dto.getDeliveryType());
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setStatus("PENDING");

        List<OrderItem> items = new ArrayList<>();
        double subtotal = 0;

        for (OrderItemDto itemDto : dto.getItems()) {
            OrderItem item = new OrderItem();
            item.setProductId(itemDto.getProductId());
            item.setName(itemDto.getName());
            item.setQty(itemDto.getQty());
            item.setPrice(itemDto.getPrice());
            item.setGst(itemDto.getGst());
            item.setOrder(order);

            subtotal += itemDto.getPrice() * itemDto.getQty();
            items.add(item);
        }

        order.setItems(items);
        order.setTotalAmount(subtotal * 1.05);

        Order saved = orderRepository.save(order);

        emailService.sendOrderConfirmation(saved.getEmail(), saved, items);
        emailService.sendAdminOrderNotification(saved, items);

        return saved;
    }

    public List<Order> getUserOrdersByEmail(String email) {
        List<Order> orders =
                orderRepository.findAllByUserIdOrderByCreatedAtDesc(email);
        orders.forEach(o -> o.getItems().size());
        return orders;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAllRecentOrders();
        orders.forEach(o -> o.getItems().size());
        return orders;
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found: " + orderId));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Order getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found: " + orderId));
        order.getItems().size();
        return order;
    }
}
