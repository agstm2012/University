package com.controller;

import com.model.CustomerLine;
import com.model.Teller;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 * User: ivanPC
 * Date: 05.11.13
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 */
public class TellerManager extends Thread {
    private ExecutorService exec;
    private CustomerLine customers;
    private PriorityQueue<Teller> workingTellers = new PriorityQueue<Teller>();
    private Queue<Teller> tellersDoingOtherThings = new LinkedList<Teller>();
    private CustomerGenerator customerGenerator;
    private final int ITERATION_SIZE = 2;

    public TellerManager(ExecutorService exec, CustomerLine customers, CustomerGenerator customerGenerator) {
        this.customerGenerator = customerGenerator;
        this.customers = customers;
        this.exec = exec;
    }

    public void adjustTellerNumber() {

    }

    private void addOneTeller() {
        System.out.println("Обработка данных");
        if (tellersDoingOtherThings.size() > 0) {
            Teller teller = tellersDoingOtherThings.remove();
            workingTellers.offer(teller);
            System.out.println("Берем кассира не из занятых: " + teller);
            return;
        }
        Teller teller = new Teller(customers);
        exec.execute(teller);
        workingTellers.add(teller);
        System.out.println("Нанимаем кассира: " + teller);
    }

    private void reassignOneTeller() {
        Teller teller = workingTellers.poll();
        tellersDoingOtherThings.offer(teller);
        System.out.println("Этот кассир лишний: " + teller);
    }

    public void run() {
    }
}
