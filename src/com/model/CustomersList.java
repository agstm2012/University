package com.model;

import java.util.concurrent.ArrayBlockingQueue;

public class CustomersList extends ArrayBlockingQueue<Customer> {
    public CustomersList(int maxLineSize) {
        super(maxLineSize);
    }

    public String toString() {
        if(this.size() == 0)
            return "[Пусто]";
        StringBuilder sb = new StringBuilder();
        for(Customer customer:this) {
            sb.append(customer);
        }
        return sb.toString();
    }
}