package com.caixp.bean;

import static org.junit.Assert.assertNotEquals;

import com.sean.init.bean.Customer;
import com.sean.init.bean.Order;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

public class CopyConstructorTest {

    @Test
    public void testCopyConstructor() {

        Order order = new Order("12345", 100.45, "In Progress");
        Customer customer = new Customer("Test", "CUstomer", order);
        Customer customerCopy = new Customer(customer);

        order.setOrderStatus("Shipped");
        assertNotEquals(customer.getOrder().getOrderStatus(), customerCopy.getOrder().getOrderStatus());
    }

    @Test
    public void testCloneable(){

        Order order = new Order("12345", 100.45, "In Progress");
        Customer customer = new Customer("Test", "CUstomer", order);
        Customer customerCopy = customer.clone();

        order.setOrderStatus("Shipped");
        assertNotEquals(customer.getOrder().getOrderStatus(), customerCopy.getOrder().getOrderStatus());
    }

    @Test
    public void testDeepClone(){

        Order order = new Order("12345", 100.45, "In Progress");
        Customer customer = new Customer("Test", "Customer", order);
        Customer cloneCustomer = SerializationUtils.clone(customer);

        order.setOrderStatus("Shipped");
        assertNotEquals(customer.getOrder().getOrderStatus(), cloneCustomer.getOrder().getOrderStatus());
    }

}