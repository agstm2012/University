package com;

import com.controller.CustomerGenerator;
import com.controller.TellerManager;
import com.interfaces.Constants;
import com.model.CustomersList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class App {
    public static void main(String[] args) throws InterruptedException {
        CustomersList customers = new CustomersList(Constants.CUSTOMERS_MAX_SIZE);
        CustomerGenerator generator = new CustomerGenerator(customers);
        Executor exec = Executors.newCachedThreadPool();
        exec.execute(generator);

        TellerManager tellerManager = new TellerManager(exec, customers, generator);
        exec.execute(tellerManager);
    }
}
