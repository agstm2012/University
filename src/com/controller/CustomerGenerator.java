package com.controller;


import com.model.Customer;
import com.model.CustomersList;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CustomerGenerator extends Thread {

    private CustomersList customers;
    private Random random = new Random();
    private boolean suspendFlag;
    private long time;

    public CustomerGenerator(CustomersList customers) {
        this.customers = customers;
        suspendFlag = false;
    }

    public void run() {
        //Todo добавить генерацию имен кассиров, фамилия имя и отчество
        time = -System.currentTimeMillis();
        try {
            while (!Thread.interrupted()) {
                TimeUnit.SECONDS.sleep(getRandom());
                //TimeUnit.SECONDS.sleep((long) 1);
                customers.put(new Customer(random.nextInt(10)));

                waitFlag();
                System.out.println("Customers size: " + customers.size());

                //Todo написать это все в teller manager
//                if(customers.size() == 10) {
//                    time += System.currentTimeMillis();
//                    System.out.println(time/1000 + " секунд");
//                    System.out.println(customers.size());
//                    suspendGenerator();
//                    for(Customer customer : customers) {
//                        System.out.println(customer);
//                    }
//                }
            }
        } catch (InterruptedException e) {
            System.out.println("Customers interrupted");
        }
        System.out.println("CustomerGenerator terminating");
    }

    private int getRandom() {
        int randValue = random.nextInt(3);
        if (randValue == 0)
            return 1;
        return randValue;
    }

    private void waitFlag() throws InterruptedException {
        synchronized (this) {
            while (suspendFlag) {
                wait();
            }
        }
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
