package com.model;

import com.controller.NameGenerator;

import java.util.concurrent.TimeUnit;

public class Teller extends Thread implements Comparable {
    private static int counter = 0;
    private int id = counter++;
    private CustomersList customers;
    private boolean suspendFlag;
    private String name;
    private int servedCustomerCount;
    private long servedTime;

    public Teller(CustomersList customers) {
        this.customers = customers;
        this.name = NameGenerator.generateName();
        this.servedCustomerCount = 0;
        this.servedTime = 0;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                Customer customer = customers.take();
                System.out.println("Обслуживаеться " + customer + " время " + customer.getServiceTime());
                TimeUnit.SECONDS.sleep(customer.getServiceTime());
                servedCustomerCount++;
                servedTime += customer.getServiceTime();
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
        System.out.println("Teller suspend");
    }

    public synchronized void resumeTeller() {
        suspendFlag = false;
        notifyAll();
        System.out.println("Teller resume");
    }

    public String toString() {
        return "Кассир: " + name + "\nВремя работы: " + servedTime + "\nКоличество обслужанных клиентов:" +
                servedCustomerCount + "\nСреднее время обслуживания клиента: " + ((double) servedTime / servedCustomerCount);
    }

    public String getTellerName() {
        return name;
    }

    public int getServedCustomerCount() {
        return servedCustomerCount;
    }

    public long getServedTime() {
        return servedTime;
    }

    public long getMiddleServedTime() {
        return servedTime / servedCustomerCount;
    }

    @Override
    public int compareTo(Object o) {
        Teller tmp = (Teller) o;
        if (this.id != tmp.id) {
            return -1;
        } else
            return 1;
    }
}
