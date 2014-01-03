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
        /*
        for (Customer customer : customers) {
            MainWindow.printOutputText(customer.toString());
        }
        for (Teller teller : workingTellers) {
            MainWindow.printOutputText(teller.toString());
        }*/
        printCalculation();
    }

    private void printCalculation() {
        MainWindow.printOutputText("Интенсивность потока λ " + Calculator.getIntensity() + " чел./мин.");
        MainWindow.printOutputText("Среднее время обработки заявки составляет t " + Calculator.getMiddleWorkTime());
        MainWindow.printOutputText("Интенсивность потока обслуживания μ " + Calculator.getIntensityServed());
        MainWindow.printOutputText("Интенсивность нагрузки ρ " + Calculator.getIntensityWork());
        MainWindow.printOutputText("Вероятность, что канал свободен (доля времени простоя каналов) ρ0 " + Calculator.getIntensityFree());
        MainWindow.printOutputText("Время простоя равно канала tпр_к " + Calculator.getTimeFree());

        StringBuilder sb = new StringBuilder();
        sb.append("Вероятность того что обслуживанием заняты:\n");
        for (int i = 1; i <= Constants.TELLERS_MAX_SIZE; i++) {
            sb.append("ρ").append(i).append(" канал ").append(Calculator.getTimeFreeTeller(i)).append("\n");
        }
        MainWindow.printOutputText(sb.toString());

        MainWindow.printOutputText("Доля заявок, получивших отказ ρотк " + Calculator.getIntensityFailed());
        MainWindow.printOutputText("Относительная пропускная способность Q " + Calculator.getIntensityQuality());
        MainWindow.printOutputText("Среднее число занятых линий связи nз " + Calculator.getTellersNotifyLine());
        MainWindow.printOutputText("Среднее число простаивающих каналов nпр " + Calculator.getTellersWaitLine());
        MainWindow.printOutputText("Коэффициент занятости каналов обслуживанием K3 " + Calculator.kpdWaitLine());
        MainWindow.printOutputText("Абсолютная пропускная способность A " + Calculator.absolute());
        MainWindow.printOutputText("Среднее время простоя СМО tпр " + Calculator.getTimeWaitSMO());
        MainWindow.printOutputText("Среднее число обслуживаемых заявок Lобс " + Calculator.getMiddleCountServedCustomer());

        createDataSendTable(sb.toString());
    }

    private void createDataSendTable(String sbStr) {
        String[] data = new String[]{str(Calculator.getIntensity()), str(Calculator.getMiddleWorkTime()),
                str(Calculator.getIntensityServed()), str(Calculator.getIntensityWork()), str(Calculator.getIntensityFree()),
                str(Calculator.getTimeFree()), sbStr,str(Calculator.getIntensityFailed()), str(Calculator.getIntensityQuality()),
                str(Calculator.getTellersNotifyLine()), str(Calculator.getTellersWaitLine()), str(Calculator.kpdWaitLine()),
                str(Calculator.absolute()), str(Calculator.getTimeWaitSMO()), str(Calculator.getMiddleCountServedCustomer())};
        MainWindow.addTableRow(data);
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