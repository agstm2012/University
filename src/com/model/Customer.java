package com.model;

/**
 * Created with IntelliJ IDEA.
 * User: ivan
 * Date: 10/19/13
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Customer {
    final private int serviceTime;

    public Customer(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public String toString() {
        return "[" + serviceTime + "]";
    }
}
