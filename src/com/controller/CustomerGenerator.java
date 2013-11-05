package com.controller;


import com.model.Customer;
import com.model.CustomerLine;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CustomerGenerator extends Thread {
    CustomerLine customers;
    private static Random random = new Random();
    /*
    <p>This boolean flag description interrupted state thread.</p>
     */
    boolean suspendFlag;

    public CustomerGenerator(CustomerLine customers) {
        this.customers = customers;
        suspendFlag = false;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                int randValue = random.nextInt(3);
                if (randValue == 0)
                    TimeUnit.SECONDS.sleep(1);
                else
                    TimeUnit.SECONDS.sleep(randValue);
                customers.put(new Customer(random.nextInt(10)));
                synchronized (this) {
                    while (suspendFlag) {
                        wait();
                    }
                }
                System.out.println("Customers size: " + customers.size());
            }
        } catch (InterruptedException e) {
            System.out.println("Customers interrupted");
        }
        System.out.println("CustomerGenerator terminating");
    }

    public synchronized void suspendGenerator() {
        suspendFlag = true;
        System.out.println("CustomerGenerator suspend");
    }

    public synchronized void resumeGenerator() {
        suspendFlag = false;
        notifyAll();
        System.out.println("CustomerGenerator resume");
    }
}
