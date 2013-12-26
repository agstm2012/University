package com.controller;

import com.model.CustomersList;
import com.model.Teller;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class TellerManager extends Thread {
    private Executor exec;
    private CustomersList customers;
    private PriorityQueue<Teller> workingTellers = new PriorityQueue<Teller>();
    private Queue<Teller> tellersDoingOtherThings = new LinkedList<Teller>();
    private CustomerGenerator generator;
    private boolean suspendFlag;
    private long time;
    //Todo: нужно научиться также вводить выводить из состояния ожидания

    public TellerManager(Executor exec, CustomersList customers, CustomerGenerator generator) {
        this.generator = generator;
        this.customers = customers;
        this.exec = exec;
        suspendFlag = false;

        initBlock();
    }

    private void initBlock() {
        //начнем с одного кассира
        Teller teller = new Teller(customers);
        exec.execute(teller);
    }


    public void operationBlock() {

    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                //time = -System.currentTimeMillis();
                operationBlock();
                //TimeUnit.SECONDS.sleep(5);
                //time += System.currentTimeMillis();
                //System.out.println(time/1000 + " секунд");

//                generator.suspendGenerator();
//                suspendTellerManager();
                waitFlag();

                if(customers.size() == 10) {
                    //Todo: надо бы еще научиться останавливать executor как то
                    suspendTellerManager();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Customers interrupted");
        }
        System.out.println("TellerManager terminating");
    }
    /**
     * <p>Добавить нового кассира, если есть свободный возьмем, если нет то купим.</p>
     */
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

    /**
     * <p>удалить из рабочих добавить в ожидающих</p>
     */
    private void reassignOneTeller() {
        Teller teller = workingTellers.poll();
        tellersDoingOtherThings.offer(teller);
        System.out.println("Этот кассир лишний: " + teller);
    }

    public synchronized void suspendTellerManager() {
        suspendFlag = true;
        System.out.println("TellerManager suspend");
    }

    public synchronized void resumeTellerManager() {
        suspendFlag = false;
        notifyAll();
        System.out.println("TellerManager resume");
    }

    private void waitFlag() throws InterruptedException {
        synchronized (this) {
            while (suspendFlag) {
                wait();
            }
        }
    }
}
