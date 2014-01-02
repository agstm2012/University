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
    public synchronized double getIntensity() {
        return (double) (generator.getTime() / 1000) / generator.getCustomersCount();
    }
    //t
    public synchronized double getMiddleWorkTime() {
        return (double) (generator.getServedTime() / generator.getCustomersCount());
    }
    //μ
    public synchronized double getIntensityServed() {
        return (double) 1 / getMiddleWorkTime();
    }
    //ρ
    public synchronized double getIntensityWork() {
        return (double) getIntensity() * getMiddleWorkTime();
    }
    //ρ0
    public synchronized double getIntensityFree() {
        double sum = 0;
        for(int i = 1; i <= Constants.TELLERS_MAX_SIZE; i++) {
            sum = sum + Math.pow(getIntensityWork(), i) / fact(i);
        }
        sum = sum + 1;
        return (double) 1 / sum;
    }
    //tпр
    public synchronized double getTimeFree() {
        return (double) 60 * getIntensityFree();
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
