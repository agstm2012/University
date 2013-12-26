package com.model;

public class Customer {
    private int serviceTime;
    private String name;

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
