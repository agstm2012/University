package com;

import com.controller.CustomerGenerator;
import com.model.CustomerLine;

/**
 * Created with IntelliJ IDEA.
 * User: ivan
 * Date: 10/17/13
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        CustomerLine customers = new CustomerLine(10);
        CustomerGenerator generator = new CustomerGenerator(customers);
        generator.start();

    }
}
