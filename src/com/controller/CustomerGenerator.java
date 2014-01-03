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
    private int customersCount;
    private long servedTime;
    private long saveTime;
    //TODO прикрутить ОТКАЗ т.е. процент отказа от всех клиентов он должен быть маленький пока без него α = 0.04
    public CustomerGenerator(CustomersList customers) {
        this.customers = customers;
        suspendFlag = false;
    }

    public void run() {
        time = -System.currentTimeMillis();
        try {
            while (!Thread.interrupted() && !suspendFlag) {
                TimeUnit.SECONDS.sleep(getRandom());
                int workTime = random.nextInt(Constants.MAX_SERVED_TIME);
                customers.put(new Customer(workTime));
                customersCount++;
                servedTime = servedTime + workTime;
                waitFlag();
                System.out.println("Customers size: " + customers.size());
            }
        } catch (InterruptedException e) {
            System.out.println("Customers interrupted");
        }
        System.out.println("CustomerGenerator terminating");
    }

    private int getRandom() {
        int randValue = random.nextInt(Constants.MAX_GENERIC_TIME);
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
        time += System.currentTimeMillis();
        saveTime = time;
        System.out.println("CustomerGenerator suspend");
    }

    public synchronized void resumeGenerator() {
        suspendFlag = false;
        notifyAll();
        time = -System.currentTimeMillis() + saveTime;
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

    public int getCustomersCount() {
        return customersCount;
    }

    public void setCustomersCount(int customersCount) {
        this.customersCount = customersCount;
    }

    public long getServedTime() {
        return servedTime;
    }

    public void setServedTime(long servedTime) {
        this.servedTime = servedTime;
    }
}
