package com.xtrendence.acs.tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xtrendence.acs.data.Repository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.*;

public class Testing {
    // Depending on the OS, the file separator can be different (usually either / or \).
    private static String fs = System.getProperty("file.separator");

    // A different folder is used to test the application's CRUD functionality.
    public String testFolder = Repository.resourcesFolder + "tests" + fs;

    public String testAccountsFile = testFolder + "accounts.json";
    public String testStockFile = testFolder + "stock.json";

    public String testTextFile = testFolder + "test.txt";

    // Ensuring that the test conditions are always the same at the start.
    public String defaultStock = "{\"1\": {\"code\": \"pc00001\", \"name\": \"Milk\", \"price\": \"1.20\", \"quantity\": \"5\"}, \"2\": {\"code\": \"pc00002\", \"name\": \"Bread\", \"price\": \"1.10\", \"quantity\": \"10\"}, \"3\": {\"code\": \"pc00003\", \"name\": \"Eggs\", \"price\": \"0.70\", \"quantity\": \"3\"}, \"4\": {\"code\": \"pc00004\", \"name\": \"Beef\", \"price\": \"3.00\", \"quantity\": \"4\"}, \"5\": {\"code\": \"pc00005\", \"name\": \"Chicken\", \"price\": \"2.50\", \"quantity\": \"9\"}, \"6\": {\"code\": \"pc00006\", \"name\": \"Bacon\", \"price\": \"1.50\", \"quantity\": \"12\"}, \"7\": {\"code\": \"pc00007\", \"name\": \"Bananas\", \"price\": \"1.25\", \"quantity\": \"3\"}, \"8\": {\"code\": \"pc00008\", \"name\": \"Apples\", \"price\": \"1.35\", \"quantity\": \"2\"}, \"9\": {\"code\": \"pc00009\", \"name\": \"Peaches\", \"price\": \"1.20\", \"quantity\": \"1\"}, \"10\": {\"code\": \"pc00010\", \"name\": \"Chocolate\", \"price\": \"3.25\", \"quantity\": \"2\"}, \"11\": {\"code\": \"pc00011\", \"name\": \"Strawberries\", \"price\": \"2.75\", \"quantity\": \"4\"}, \"12\": {\"code\": \"pc00012\", \"name\": \"Chips\", \"price\": \"1.75\", \"quantity\": \"5\"}, \"13\": {\"code\": \"pc00013\", \"name\": \"Olives\", \"price\": \"0.85\", \"quantity\": \"6\"}, \"14\": {\"code\": \"pc00014\", \"name\": \"Oranges\", \"price\": \"0.50\", \"quantity\": \"8\"}, \"15\": {\"code\": \"pc00015\", \"name\": \"Red Bull\", \"price\": \"1.25\", \"quantity\": \"9\"}, \"16\": {\"code\": \"pc00016\", \"name\": \"Salmon\", \"price\": \"4.00\", \"quantity\": \"5\"}, \"17\": {\"code\": \"pc00017\", \"name\": \"Dignity\", \"price\": \"0.01\", \"quantity\": \"1\"}, \"18\": {\"code\": \"pc00018\", \"name\": \"Coffee\", \"price\": \"2.00\", \"quantity\": \"3\"}, \"19\": {\"code\": \"pc00019\", \"name\": \"Tea\", \"price\": \"1.50\", \"quantity\": \"7\"}, \"20\": {\"code\": \"pc00020\", \"name\": \"Nando's Sauce\", \"price\": \"3.00\", \"quantity\": \"99\"}}";

    public Map<String, String> defaultAccounts = new HashMap<>();

    public Testing() {
        defaultAccounts.put("Temp", "$2a$12$eRCYLwKDTEKjVg8PzNdrs.1BjUBTqkt3uDErPQ25xvFO5Z.bUlv2a");
        defaultAccounts.put("Xtrendence", "$2a$12$dSD3oMqS6vDCSTJ..KSPJunABiXda6z1fpHDswt228p74XKTw4W02");
        defaultAccounts.put("James", "$2a$12$8NVY5i/.tKE6ufjR7MImROLevix0A6sZAqdQHJFtE1LuBrkGQm6LS");
        defaultAccounts.put("Hannibal", "$2a$12$wH6FGLmvKJ.ao98RfEo9XudGtVcvMKEWfNAVPIZ7MxPNXcm94RV.2");
    }

    public void generateTestFiles() {
        File resources = new File(testFolder);
        File stock = new File(testStockFile);
        File accounts = new File(testAccountsFile);
        File test = new File(testTextFile);

        resources.mkdir();

        // Start the test without the files already existing.
        stock.delete();
        accounts.delete();
        test.delete();

        if(!stock.exists()) {
            try {
                stock.createNewFile();

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Map map = gson.fromJson(defaultStock, Map.class);
                String json = gson.toJson(map);

                FileWriter writer = new FileWriter(stock, false);
                writer.write(json);
                writer.close();

                // Ensure that the stock file has been created.
                assertTrue("Test stock file should be created.", stock.exists());
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        if(!accounts.exists()) {
            try {
                accounts.createNewFile();

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(defaultAccounts);

                FileWriter writer = new FileWriter(accounts, false);
                writer.write(json);
                writer.close();

                assertTrue("Test accounts file should be created.", accounts.exists());
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        if(!test.exists()) {
            try {
                test.createNewFile();

                FileWriter writer = new FileWriter(test, false);
                writer.write("default");
                writer.close();

                assertTrue("Test text file should be created.", test.exists());
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    public void testAll() {
        generateTestFiles();
        testCreate();
        testRead();
        testUpdate();
        testDelete();
    }

    public void testCreate() {
        System.out.println("Testing Repository.create()");

        File stock = new File(Repository.stockFile);
        File accounts = new File(Repository.accountsFile);

        stock.delete();
        accounts.delete();

        assertFalse("Stock file should be deleted.", stock.exists());
        assertFalse("Accounts file should be deleted.", accounts.exists());

        Repository.create();

        // To test that the stock.json file is created, and that the expected data is written to it.
        try {
            StringBuilder builder = new StringBuilder();
            FileReader reader = new FileReader(stock);
            Scanner scanner = new Scanner(reader);
            while(scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            scanner.close();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Map defaultStockMap = gson.fromJson(defaultStock, Map.class);
            String defaultStockJSON = gson.toJson(defaultStockMap);

            Map createdStockMap = gson.fromJson(builder.toString(), Map.class);
            String createdStockJSON = gson.toJson(createdStockMap);

            assertEquals("The stock file should be created with the expected data.", defaultStockJSON, createdStockJSON);
        } catch(Exception e) {
            System.out.println(e);
        }

        // To test that the accounts.json file is created, and that the expected data is written to it.
        try {
            StringBuilder builder = new StringBuilder();
            FileReader reader = new FileReader(accounts);
            Scanner scanner = new Scanner(reader);
            while(scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            scanner.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String defaultAccountsJSON = gson.toJson(defaultAccounts);

            Map createdAccountsMap = gson.fromJson(builder.toString(), Map.class);
            String createdAccountsJSON = gson.toJson(createdAccountsMap);

            assertEquals("The accounts file should be created with the expected data.", defaultAccountsJSON, createdAccountsJSON);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void testRead() {
        System.out.println("Testing Repository.read()");

        generateTestFiles();

        String content = Repository.read(testTextFile);

        assertEquals("The content of the text file should be \"default\".", "default", content);
    }

    public void testUpdate() {
        System.out.println("Testing Repository.update()");

        generateTestFiles();

        Repository.update(testTextFile, "updated");

        String content = Repository.read(testTextFile);

        assertEquals("The content of the text file should be \"updated\".", "updated", content);
    }

    public void testDelete() {
        System.out.println("Testing Repository.delete()");

        generateTestFiles();

        Repository.delete(testTextFile);

        assertFalse("The text file should not exist.", new File(testTextFile).exists());
    }
}
