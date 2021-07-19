package com.example.order.service;

import java.util.Date;
import java.util.List;

import com.example.order.model.Order;
import com.example.order.exception.OrderException;

public interface OrderService {

    public Order findById(Long orderId) throws OrderException;

    public List<Order> findAll();

    public Order save(Order order);

    public Order schedule(Long orderId, Date dateTime) throws OrderException;

    public Order cancel(Long orderId) throws OrderException;

    public Order update(Long orderId, Order orderDetails) throws OrderException;

    public boolean delete(Long orderId) throws OrderException;

    public Order assign(Long orderId) throws OrderException;

    public Order upload(Long orderId, String base64Files) throws OrderException;

    public Order accept(Long orderId) throws OrderException;

    public Order reject(Long orderId) throws OrderException;
}
