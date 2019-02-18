package com.sean.init.bean;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer implements Serializable, Cloneable {

    private String firstName;
    private String lastName;
    private Order order;

    public Customer(Customer customer) {
        this(customer.getFirstName(), customer.getLastName(), new Order(customer.getOrder()));
    }

    @Override
    public Customer clone() {
        Customer customer = null;
        try {
            customer = (Customer) super.clone();
        } catch (CloneNotSupportedException e) {
            customer = new Customer(this.firstName, this.lastName, this.order);
        }
        customer.order = this.order.clone();
        return customer;
    }
}
