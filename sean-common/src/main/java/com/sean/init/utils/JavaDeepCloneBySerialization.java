package com.sean.init.utils;

import com.sean.init.bean.Customer;
import com.sean.init.bean.Order;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JavaDeepCloneBySerialization<T> {

    public static void main(String[] args) {

        Order order = new Order("12345", 100.45, "In Progress");
        Customer customer = new Customer("Test", "CUstomer", order);
        JavaDeepCloneBySerialization<Customer> customerJavaDeepCloneBySerialization = new JavaDeepCloneBySerialization<>();

        Customer cloneCustomer = customerJavaDeepCloneBySerialization.deepClone(customer);
        order.setOrderStatus("Shipped");
        System.out.println(cloneCustomer.getOrder().getOrderStatus());

    }

    public T deepClone(T object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(bais);
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}