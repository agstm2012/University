package com.controller;

import com.interfaces.Constants;
import com.model.Customer;
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
    //Todo просчитывать количество клиентов обслужанных каждым кассиров
    //Todo обнулять потом все, посчитывать время до остановки
    //Todo Просчитывать среднее время обслуживания у каждого кассира

    //Todo просчитывать прочие атрибуты(возьми из лекций и тестовых проектов)

    //Todo продумать SWING сделать заготовку

    public TellerManager(Executor exec, CustomersList customers, CustomerGenerator generator) {
        this.generator = generator;
        this.customers = customers;
        this.exec = exec;
        suspendFlag = false;
        initBlock();
    }

    private void initBlock() {
        addTeller();
    }


    public void operationBlock() {

    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                operationBlock();
                waitFlag();

//                if(customers.size() == 5) {
//                    suspendBlock();
//                    TimeUnit.SECONDS.sleep(5);
//                    resumeBlock();
//                }
//
//                if (customers.size() == Constants.CUSTOMERS_MAX_SIZE) {
//                    suspendBlock();
//                    printBlock();
//
//                    //exitBlock();
//                }
            }
        } catch (InterruptedException e) {
            System.out.println("Customers interrupted");
        }
        System.out.println("TellerManager terminating");
    }

    public void suspendBlock() {
        suspendTellerManager();
        generator.suspendGenerator();
        for (Teller teller : workingTellers)
            teller.suspendTeller();
    }

    public void resumeBlock() {
        resumeTellerManager();
        generator.resumeGenerator();
        for(Teller teller : workingTellers)
            teller.resumeTeller();
    }

    private void printBlock() {
        for (Customer customer : customers) {
            System.out.println(customer);
        }

        for (Teller teller : workingTellers) {
            System.out.println(teller);
        }
    }

    private void exitBlock() {
        System.exit(0);
    }

    /**
     * <p>Добавить нового кассира, если есть свободный возьмем, если нет то купим.</p>
     */
    private void addTeller() {
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
        System.out.println("Нанимаем кассира: " + teller.getTellerName());
    }

    /**
     * <p>удалить из рабочих добавить в ожидающих</p>
     */
    private void reassignTeller() {
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
