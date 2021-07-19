package com.example.order.repository.impl;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.order.model.Order;
import com.example.order.model.Order.State;
import com.example.order.repository.OrderRepository;

import org.springframework.stereotype.Repository;

/**
 * A mocked implementation of an orders repository
 * @author dpescatore
 */
@Repository
public class MockOrderRepositoryImpl implements OrderRepository {
    
    private List<Order> dbTable;

    public MockOrderRepositoryImpl() {
        dbTable=new ArrayList<Order>();
    }

    public List<Order> findAll() {
        return dbTable;
    }

    public Optional<Order> findById(Long orderId) {

        return dbTable.stream().filter(order -> orderId.equals(order.getId())).findAny();
    }

    public Order save(Order order) {
        
        order.setCreationDate(new Date());
        order.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);        
        order.setState(order.getDateTime()==null? State.UNSCHEDULED:State.PENDING);
        dbTable.add(order);
        return order;
    }

    public Order update(Order order) {
        
        dbTable = dbTable.stream()
            .map(dbOrder -> dbOrder.getId() == order.getId() ? order : dbOrder)
            .collect(Collectors.toList());
        return order;
    }

    public boolean delete(Order order) {
        return dbTable.removeIf(orderDb -> order.getId().equals(orderDb.getId()));
    }
    
}
