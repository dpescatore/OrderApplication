package com.example.order.exception;

public class OrderException extends Exception {
    //Each exception message will be held here
    private String message;
 
    public OrderException(String msg)
    {
        this.message = msg;
    }
    //Message can be retrieved using this accessor method
    public String getMessage() {
        return message;
    }
}
