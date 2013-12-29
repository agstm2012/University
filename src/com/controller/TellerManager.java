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

                if (customers.size() == Constants.CUSTOMERS_MAX_SIZE) {
                    suspendBlock();
                    printBlock();
                    exitBlock();
                }
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

    public void reloadBlock() {
        suspendBlock();
        try {
            System.out.println("Идет перезагрузка. Подождите 10 секунд");
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customers = new CustomersList(Constants.CUSTOMERS_MAX_SIZE);
        generator.setCustomers(customers);
        generator.setTime(0);
        generator.setCustomersCount(0);
        for (Teller teller : workingTellers)
            teller.interrupt(); //Todo здесь надо завершить поток теллера.
        workingTellers = new PriorityQueue<Teller>();
        tellersDoingOtherThings = new LinkedList<Teller>();
        addTeller();
        resumeBlock();
    }

    public void printBlock() {
        for (Customer customer : customers) {
            System.out.println(customer);
        }

        for (Teller teller : workingTellers) {
            System.out.println(teller);
        }

        System.out.println("Интенсивность потока h " + calculateIntensity() + " чел./сек.");
        System.out.println("Интенсивность нагрузки p " + calculateIntensityLoad() + " на каждого кассира");
        System.out.println("Вероятность того что канал не занят p0 " + calculateProbabilityOfFailure());
    }

    private int fact(int num) {
        return (num == 0) ? 1 : num * fact(num - 1);
    }

    private static float round(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }

    private float calculateProbabilityOfFailure() {
        float sum = 0;
        float probabilityOfFailure;
        for(int i = 0; i <= Constants.TELLERS_MAX_SIZE; i++) {
            sum = (float) (sum + (Math.pow(calculateIntensity() * 60, i)/ fact(i)));
        }
        probabilityOfFailure = (float) 1 / sum;
        if(probabilityOfFailure > 0 && probabilityOfFailure < 0.1f)
            return 0.1f;
        return round(probabilityOfFailure, 2);
    }

    //Todo может создать класс calculator чтобы не было вычислений в самом потоке
    private double calculateIntensityLoad() {   //u - количество кассиров
        return (double) (calculateIntensity() / Constants.TELLERS_MAX_SIZE);
    }

    private double calculateIntensity() {
        //return (double) ((generator.getTime() / 1000) / generator.getCustomersCount()) * 3600;
        return (double) (generator.getTime() / 1000) / generator.getCustomersCount();
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
