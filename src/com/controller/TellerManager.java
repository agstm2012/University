package com.controller;

import com.interfaces.Constants;
import com.model.Customer;
import com.model.CustomersList;
import com.model.Teller;
import com.view.MainWindow;

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
    //Todo прикрутить окно настройки к форме и менюху с настройками, возможно читать из xml - ника ширину окна высоту
    //Todo разобраться как просчитывать атрибуты и какие атрибуты нужны
    //Todo 1 минунда у меня равн, одной минуте
    //Todo доделать SWING

    public TellerManager(Executor exec, CustomersList customers, CustomerGenerator generator) {
        this.generator = generator;
        this.customers = customers;
        this.exec = exec;
        suspendFlag = false;
        initBlock();
    }

    private void initBlock() {
        for(int i = 1; i <= Constants.TELLERS_MAX_SIZE; i++) {
            addTeller();
        }
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
        for (Teller teller : workingTellers)
            teller.resumeTeller();
    }

    public void reloadBlock() {
        suspendBlock();
        //Todo заменить это на что то другое, а то подвисает GUI
//        try {
//            System.out.println("Идет перезагрузка. Подождите 10 минунд");
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        customers = new CustomersList(Constants.CUSTOMERS_MAX_SIZE);
        generator.setCustomers(customers);
        generator.setTime(0);
        generator.setCustomersCount(0);
        generator.setServedTime(0);
        for (Teller teller : workingTellers)
            teller.interrupt();
        workingTellers = new PriorityQueue<Teller>();
        tellersDoingOtherThings = new LinkedList<Teller>();
        for(int i = 1; i <= Constants.TELLERS_MAX_SIZE; i++) {
            addTeller();
        }
        resumeBlock();
    }

    public void printBlock() {
        for (Customer customer : customers) {
            MainWindow.printOutputText(customer.toString());
        }
        for (Teller teller : workingTellers) {
            MainWindow.printOutputText(teller.toString());
        }
        printCalculation();
    }

    private void printCalculation() {
//        MainWindow.printOutputText("Интенсивность потока h " + Calculator.calculateIntensity() + " чел./мин.");
//        MainWindow.printOutputText("Интенсивность нагрузки p " + Calculator.calculateIntensityLoad() + " на каждого кассира");
//        MainWindow.printOutputText("Вероятность того что канал не занят p0 " + Calculator.calculateProbabilityOfFailure());
//        for (int i = 1; i <= Constants.TELLERS_MAX_SIZE; i++) {
//            MainWindow.printOutputText("Вероятность того, что обслуживанием занят p" + i + " канал: " +
//                    Calculator.calculateProbabilityByChanel(i));
//        }
//        MainWindow.printOutputText("Доля заявок, получивших отказ p_otk " + Calculator.calculateCustomerProbabilityOfFailure());
//        MainWindow.printOutputText("Относительная пропускная способность p_obs " + Calculator.calculateCustomerServedOfFailure());
//        MainWindow.printOutputText("Среднее число каналов, занятых обслуживанием n_z " + Calculator.middleCountChanelServed());
//        MainWindow.printOutputText("Среднее число простаивающих каналов n_sr " + Calculator.middleCountChanelWait());
//        MainWindow.printOutputText("Коэффициент занятости каналов обслуживанием K_z " + Calculator.kidServedChannels());
//        MainWindow.printOutputText("Абсолютная пропускная способность A " + Calculator.absolutionProbability());
//        MainWindow.printOutputText("Среднее время простоя СМО t_pr " + Calculator.middleTimeWaitSMO());
//        MainWindow.printOutputText("Среднее число обслуживаемых заявок l_obs " + Calculator.middleCountServedCustomer());
//
//        createDataSendTable();
    }

//    private void createDataSendTable() {
//        String[] data = new String[]{str(Calculator.calculateIntensity()), str(Calculator.calculateIntensityLoad()),
//                str(Calculator.calculateProbabilityOfFailure()), "ДЛЯ КАЖДОГО", str(Calculator.calculateCustomerProbabilityOfFailure()),
//                str(Calculator.calculateCustomerServedOfFailure()), str(Calculator.middleCountChanelServed()),
//                str(Calculator.middleCountChanelWait()), str(Calculator.kidServedChannels()), str(Calculator.absolutionProbability()),
//                str(Calculator.middleTimeWaitSMO()), str(Calculator.middleCountServedCustomer())};
//        MainWindow.addTableRow(data);
//    }

    private String str(float value) {
        return String.valueOf(value);
    }

    private String str(double value) {
        return String.valueOf(value);
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









/*
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

    private double calculateIntensityLoad() {   //u - количество кассиров
        return (double) (calculateIntensity() / Constants.TELLERS_MAX_SIZE);
    }

    private double calculateIntensity() {
        //return (double) ((generator.getTime() / 1000) / generator.getCustomersCount()) * 3600;
        return (double) (generator.getTime() / 1000) / generator.getCustomersCount();
    }
    */