package com.example.order.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.order.model.Order;
import com.example.order.service.OrderService;
import com.example.order.exception.OrderException;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Get all orders list.
     *
     * @return the list
     */
    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }

    /**
     * Gets orders by id.
     *
     * @param orderId the order id
     * @return the orders by id
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrdersById(@PathVariable(value = "id") Long orderId)
    // throws ResourceNotFoundException
    {
        Order order;
        try {
            order = orderService.findById(orderId);
        } catch (OrderException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(order);
    }

    /**
     * Create order order.
     *
     * @param order the order
     * @return the order
     */
    @PostMapping("/orders")
    public Order createOrder(@Valid @RequestBody Order order) {
        return orderService.save(order);
    }

    
    /**
     * Update order response entity.
     *
     * @param orderId      the order id
     * @param orderDetails the order details
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PutMapping("/orders/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable(value = "id") Long orderId,
            @Valid @RequestBody Order orderDetails) {
        Order updatedOrder = null;
        try {

            updatedOrder = orderService.update(orderId, orderDetails);
        } catch (OrderException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Delete order.
     *
     * @param orderId the order id
     * @return the map
     * @throws Exception the exception
     */
    @DeleteMapping("/order/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable(value = "id") Long orderId) throws Exception {

        boolean ret = false;
        try {

            ret = orderService.delete(orderId);
        } catch (OrderException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", ret);
        return ResponseEntity.ok(response);
    }

    /**
     * 
     * @param orderId  the id of the order to schedule
     * @param dateTime the selected dateTime when schedule order
     * @return
     */
    @PostMapping("/orders/{id}/schedule")
    public ResponseEntity<?> scheduleOrder(@PathVariable(value = "id") Long orderId, @RequestBody Date dateTime) {

        Order order = null;
        try {
            order = orderService.schedule(orderId, dateTime);
        } catch (OrderException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(order);
    }

    /**
     * Assign a specific order to a photographer
     * @param orderId the id of the order to assign
     * @return the order assigned or a bad request
     */
    @PostMapping("/orders/{id}/assign")
    public ResponseEntity<?> assignOrder(@PathVariable(value = "id") Long orderId) {

        Order order = null;
        try {
            order = orderService.assign(orderId);
        } catch (OrderException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(order);
    }

    /**
     * Upload a base64zip to an order
     * @param orderId the Id of the order to update
     * @param base64Files the base64 of a zip file containing photos
     * @return the updated order or a bad request
     */
    @PostMapping("/orders/{id}/upload")
    public ResponseEntity<?> uploadPhotos(@PathVariable(value = "id") Long orderId, @RequestBody String base64Files) {

        Order order = null;
        try {
            order = orderService.upload(orderId, base64Files);
        } catch (OrderException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(order);
    }

    /**
     * Accept an order in UPLOADED state
     * @param orderId the id of the order to accept
     * @return the order accepted
     */
    @PostMapping("/orders/{id}/accept")
    public ResponseEntity<?> acceptPhotos(@PathVariable(value = "id") Long orderId) {

        Order order = null;
        try {
            order = orderService.accept(orderId);
        } catch (OrderException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(order);
    }

    /**
     * Reject an order in UPLOADED state
     * @param orderId the id of the order to reject
     * @return the order rejected
     */
    @PostMapping("/orders/{id}/reject")
    public ResponseEntity<?> rejectPhotos(@PathVariable(value = "id") Long orderId) {

        Order order = null;
        try {
            order = orderService.reject(orderId);
        } catch (OrderException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(order);
    }

    /**
     * Cancel an order
     * @param orderId id of the order to cancel
     * @return canceled order
     */
    @PostMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable(value = "id") Long orderId) {
        Order order = null;
        try {
            order = orderService.cancel(orderId);
        } catch (OrderException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(order);

    }

}
