package com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class NameGenerator {
    private static Random random = new Random();
    private static List<String> names = new ArrayList<String>();
    private static List<String> sonames = new ArrayList<String>();
    private static List<String> othernames = new ArrayList<String>();

    public NameGenerator() {
        names = getDataFromFile("files" + File.separator + "names.txt");
        sonames = getDataFromFile("files" + File.separator + "sonames.txt");
        othernames = getDataFromFile("files" + File.separator + "othernames.txt");
    }

    private List<String> getDataFromFile(String filePath) {
        List<String> data = new ArrayList<String>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(filePath), "utf-8");
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            assert scanner != null;
            scanner.close();
        }
        return data;
    }

    private static String getLastSymbols(String str, int size) {
        return str.substring(str.length() - size);
    }

    public synchronized static String generateName() {
        String name = names.get(random.nextInt(names.size()));
        String soname = sonames.get(random.nextInt(sonames.size()));
        String othername = othernames.get(random.nextInt(othernames.size()));
        if(getLastSymbols(name, 1).contains("а") || getLastSymbols(name, 1).contains("я") ||
                getLastSymbols(name, 2).contains("вь")) {
            if(!name.contains("Илья")) {
                soname += "a";
                othername = othername.replace("вич", "вна");
            }
        }
        return soname + " " + name + " " + othername;
    }
}
