package com.example.order.repository;

import java.util.List;
import java.util.Optional;

import com.example.order.model.Order;
/**
 * Interface representing a generic repository for Order element
 * @author dpescatore
 */
public interface OrderRepository {
    public List<Order> findAll();

    public Optional<Order> findById(Long orderId);

    public Order save(Order order);

    public Order update(Order order);

    public boolean delete(Order order);

}
