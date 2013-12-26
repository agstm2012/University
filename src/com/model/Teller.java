package com.model;

import java.util.concurrent.TimeUnit;

public class Teller extends Thread {
    private static int counter = 0;
    private int id = counter++;
    private CustomersList customers;
    private boolean suspendFlag;

    public Teller(CustomersList customers) {
        this.customers = customers;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                Customer customer = customers.take();
                System.out.println("Обслуживаеться " + customer + " время " + customer.getServiceTime());
                TimeUnit.SECONDS.sleep(customer.getServiceTime());
                waitFlag();
            }
        } catch (InterruptedException e) {
            System.out.println(this + " прерван");
        }
        System.out.println(this + " завершаеться");
    }

    private void waitFlag() throws InterruptedException {
        synchronized (this) {
            while (suspendFlag) {
                wait();
            }
        }
    }

    public synchronized void suspendTeller() {
        suspendFlag = true;
        System.out.println("CustomerGenerator suspend");
    }

    public synchronized void resumeTeller() {
        suspendFlag = false;
        notifyAll();
        System.out.println("CustomerGenerator resume");
    }
}
