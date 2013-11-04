package com.model;

import java.util.concurrent.ArrayBlockingQueue;

public class CustomerLine extends ArrayBlockingQueue<Customer> {
    public CustomerLine(int maxLineSize) {
        super(maxLineSize);
    }

    public String toString() {
        if(this.size() == 0)
            return "[Пусто]";
        StringBuilder result = new StringBuilder();
        for(Customer customer:this) {
            result.append(customer);
        }
        return result.toString();
    }
}