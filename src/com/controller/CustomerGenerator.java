package com.controller;


import com.interfaces.Constants;
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
            while (!Thread.interrupted() && !suspendFlag) {
                TimeUnit.SECONDS.sleep(getRandom());
                customers.put(new Customer(random.nextInt(10)));
                waitFlag();
                System.out.println("Customers size: " + customers.size());
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

    public CustomersList getCustomers() {
        return customers;
    }

    public boolean isSuspendFlag() {
        return suspendFlag;
    }

    public long getTime() {
        return time;
    }

    public void setCustomers(CustomersList customers) {
        this.customers = customers;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setSuspendFlag(boolean suspendFlag) {
        this.suspendFlag = suspendFlag;
    }
}
