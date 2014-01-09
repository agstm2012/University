package com;

import com.controller.Calculator;
import com.controller.CustomerGenerator;
import com.controller.NameGenerator;
import com.controller.TellerManager;
import com.interfaces.Constants;
import com.model.CustomersList;
import com.view.MainWindow;

import javax.swing.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class App {
    public static void main(String[] args) throws InterruptedException {
        //init name generator
        NameGenerator nameGenerator = new NameGenerator();

        CustomersList customers = new CustomersList(Constants.CUSTOMERS_MAX_SIZE);
        final CustomerGenerator generator = new CustomerGenerator(customers);
        //init calculator
        Calculator calculator = new Calculator(generator);

        Executor exec = Executors.newCachedThreadPool();
        exec.execute(generator);

        final TellerManager tellerManager = new TellerManager(exec, customers, generator);
        exec.execute(tellerManager);

        SwingUtilities.invokeLater(new Runnable() {   //this is use for concurrency-safe mode
            @Override
            public void run() {
                MainWindow window = new MainWindow(tellerManager, generator);
                window.setVisible(true);
            }
        });
    }
}
