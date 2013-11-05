package com.model;

/**
 * Created with IntelliJ IDEA.
 * User: ivanPC
 * Date: 05.11.13
 * Time: 14:11
 * To change this template use File | Settings | File Templates.
 */
public class Teller extends Thread {
    private static int counter = 0;
    private final int id = counter++;
    private CustomerLine customers;

    public Teller(CustomerLine customers) {
        this.customers = customers;
    }

    public void run() {
        /*try {
            while (!Thread.interrupted()) {
                Customer customer = customers.take();
                TimeUnit.MILLISECONDS.sleep(customer.getServiceTime());
            }
        } catch (InterruptedException e) {
            System.out.println(this + " прерван");
        }                                              */
        System.out.println(this + " завершаеться");
    }
}
