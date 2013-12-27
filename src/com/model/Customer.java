package com.model;

import com.controller.NameGenerator;

public class Customer {
    private int serviceTime;
    private String name;

    public Customer(int serviceTime) {
        this.serviceTime = serviceTime;
        this.name = NameGenerator.generateName();
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public String toString() {
        return "[" + name + "]";
    }
}
