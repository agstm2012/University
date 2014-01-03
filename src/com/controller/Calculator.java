package com.controller;

import com.interfaces.Constants;

public class Calculator {
    private static CustomerGenerator generator;

    public Calculator(CustomerGenerator generator) {
        Calculator.generator = generator;
    }

    private static int fact(int num) {
        return (num == 0) ? 1 : num * fact(num - 1);
    }

    public synchronized static float round(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }

    //λ
    public static synchronized double getIntensity() {
        return (double) (generator.getTime() / 1000) / generator.getCustomersCount();
    }

    //t
    public static synchronized double getMiddleWorkTime() {
        return (double) (generator.getServedTime() / generator.getCustomersCount());
    }

    //μ
    public static synchronized double getIntensityServed() {
        return (double) 1 / getMiddleWorkTime();
    }

    //ρ
    public static synchronized double getIntensityWork() {
        return (double) getIntensity() * getMiddleWorkTime();
    }

    //ρ0
    public static synchronized double getIntensityFree() {
        double sum = 0;
        for (int i = 1; i <= Constants.TELLERS_MAX_SIZE; i++) {
            sum = sum + Math.pow(getIntensityWork(), i) / fact(i);
        }
        sum = sum + 1;
        return (double) 1 / sum;
    }

    //tпр
    public static synchronized double getTimeFree() {
        return (double) 60 * getIntensityFree();
    }

    //ρ для каждой кассы
    public static synchronized double getTimeFreeTeller(int number) {
        return (double) (Math.pow(getIntensityWork(), number) / fact(number)) * getIntensityFree();
    }

    //ρотк p(pow)n/n! * p0
    public static synchronized double getIntensityFailed() {
        return (double) (Math.pow(getIntensityWork(), Constants.TELLERS_MAX_SIZE) / fact(Constants.TELLERS_MAX_SIZE) *
                getIntensityFree());  //* 100 процент не обслужанных
    }
    //Относительная пропускная способность: Q = pобс
    public static synchronized double getIntensityQuality() {
        return (double) 1 - getIntensityFailed();   // процент обслужанных
    }
    //Среднее число занятых линий связи nз = p * pобс
    public static synchronized double getTellersNotifyLine() {
        return (double) getIntensityWork() * getIntensityQuality();
    }
    //Среднее число простаивающих каналов.nпр SIZE - nз
    public static synchronized double getTellersWaitLine() {
        return (double) Constants.TELLERS_MAX_SIZE - getTellersNotifyLine();
    }
    //Коэффициент занятости каналов обслуживанием.K3
    public static synchronized double kpdWaitLine() {
        return (double) getTellersWaitLine() / Constants.TELLERS_MAX_SIZE;    //* 10 и будет процент
    }
    //Абсолютная пропускная способность. A
    public static synchronized double absolute() {
        return (double) getIntensityQuality() * getIntensity();
    }
    //Среднее время простоя СМО. tпр = pотк • tобс = 0.0425 • 3.5 = 0.15 мин.
    public static synchronized double getTimeWaitSMO() {
        return (double) getIntensityFailed() * getMiddleWorkTime();
    }
    //12. Среднее число обслуживаемых заявок. Lобс = ρ • Q = 2.1 • 0.96 = 2.01 ед.
    public static synchronized double getMiddleCountServedCustomer() {
        return (double) getIntensityWork() * getIntensityQuality();
    }

    /*
    public synchronized static float calculateProbabilityOfFailure() {
        float sum = 0;
        float probabilityOfFailure;
        for(int i = 0; i <= Constants.TELLERS_MAX_SIZE; i++) {
            sum = (float) (sum + (Math.pow(calculateIntensity(), i)/ fact(i)));
        }
        probabilityOfFailure = (float) 1 / sum;
        if(probabilityOfFailure > 0 && probabilityOfFailure < 0.1f)
            return 0.1f;
        return round(probabilityOfFailure, 2);
    }

    public synchronized static float calculateProbabilityByChanel(int number) {
        return (float) (Math.pow(calculateIntensity(), number) / fact(number));
    }

    public synchronized static float calculateCustomerProbabilityOfFailure() {
        return (float) (Math.pow(calculateIntensity(), Constants.TELLERS_MAX_SIZE) /
                fact(Constants.TELLERS_MAX_SIZE)) * calculateProbabilityOfFailure();
    }

    public synchronized static float calculateCustomerServedOfFailure() {
        return (float) Math.abs(1 - calculateCustomerProbabilityOfFailure()) / 1000;
    }

    public synchronized static float middleCountChanelServed() {
        return (float) (calculateCustomerServedOfFailure() * (calculateIntensity()));
    }

    public synchronized static float middleCountChanelWait() {
        return (float) Math.abs(1 - middleCountChanelServed());
    }

    public synchronized static float kidServedChannels() {
        return (float) middleCountChanelServed() / Constants.TELLERS_MAX_SIZE;
    }

    public synchronized static float absolutionProbability() {
        return (float) (calculateCustomerServedOfFailure() * (calculateIntensity()));
    }

    public synchronized static double calculateIntensityLoad() {   //u - количество кассиров
        return (double) (calculateIntensity() / Constants.TELLERS_MAX_SIZE);
    }

    public synchronized static double middleTimeWaitSMO() {
        return (double) (calculateCustomerProbabilityOfFailure() * generator.getTime());
    }

    public synchronized static double middleCountServedCustomer() {
        return (double) (calculateIntensity() * calculateCustomerServedOfFailure());
    }

    public synchronized static double calculateIntensity() {
        //return (double) ((generator.getTime() / 1000) / generator.getCustomersCount()) * 3600;
        return (double) (generator.getTime() / 1000) / generator.getCustomersCount() * 60;
    } */
}
