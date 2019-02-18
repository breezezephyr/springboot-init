package com.sean.init.bean;

import java.io.Serializable;
import lombok.Data;

@Data
public class Order implements Serializable, Cloneable {

    private String orderNumber;
    private double orderAmount;
    private String orderStatus;

    public Order(Order order) {
        this(order.getOrderNumber(), order.getOrderAmount(), order.getOrderStatus());
    }

    public Order(String orderNumber, double orderAmount, String orderStatus) {
        this.orderNumber = orderNumber;
        this.orderAmount = orderAmount;
        this.orderStatus = orderStatus;
    }
    @Override
    public Order clone(){
        try {
            return (Order) super.clone();
        }catch (CloneNotSupportedException e) {
            return new Order(this.orderNumber,this.orderAmount,this.orderStatus);
        }
    }
}


