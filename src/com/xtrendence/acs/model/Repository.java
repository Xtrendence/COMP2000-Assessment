package com.xtrendence.acs.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xtrendence.acs.controller.IRepository;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Part of the Repository design pattern.
public class Repository implements IRepository {
    // Depending on the OS, the file separator can be different (usually either / or \).
    private static String fs = System.getProperty("file.separator");

    // The resources folder contains the icons and JSON files used in the application. Since JAR files are read-only, the resources folder would be right outside it, hence why the getResource() method isn't being used.
    public static String resourcesFolder = System.getProperty("user.dir") + fs + "resources" + fs;
    public static String accountsFile = resourcesFolder + "accounts.json";
    public static String stockFile = resourcesFolder + "stock.json";

    /* Generates the required files for the application to function. The files are only generated if they don't already exist.
    *  @return Nothing.
    */
    public static void create() {
        File resources = new File(Repository.resourcesFolder);
        File stock = new File(Repository.stockFile);
        File accounts = new File(Repository.accountsFile);

        resources.mkdir();

        if(!stock.exists()) {
            try {
                stock.createNewFile();

                String defaultStock = "{\"1\": {\"code\": \"pc00001\", \"name\": \"Milk\", \"price\": \"1.20\", \"quantity\": \"5\"}, \"2\": {\"code\": \"pc00002\", \"name\": \"Bread\", \"price\": \"1.10\", \"quantity\": \"10\"}, \"3\": {\"code\": \"pc00003\", \"name\": \"Eggs\", \"price\": \"0.70\", \"quantity\": \"3\"}, \"4\": {\"code\": \"pc00004\", \"name\": \"Beef\", \"price\": \"3.00\", \"quantity\": \"4\"}, \"5\": {\"code\": \"pc00005\", \"name\": \"Chicken\", \"price\": \"2.50\", \"quantity\": \"9\"}, \"6\": {\"code\": \"pc00006\", \"name\": \"Bacon\", \"price\": \"1.50\", \"quantity\": \"12\"}, \"7\": {\"code\": \"pc00007\", \"name\": \"Bananas\", \"price\": \"1.25\", \"quantity\": \"3\"}, \"8\": {\"code\": \"pc00008\", \"name\": \"Apples\", \"price\": \"1.35\", \"quantity\": \"2\"}, \"9\": {\"code\": \"pc00009\", \"name\": \"Peaches\", \"price\": \"1.20\", \"quantity\": \"1\"}, \"10\": {\"code\": \"pc00010\", \"name\": \"Chocolate\", \"price\": \"3.25\", \"quantity\": \"2\"}, \"11\": {\"code\": \"pc00011\", \"name\": \"Strawberries\", \"price\": \"2.75\", \"quantity\": \"4\"}, \"12\": {\"code\": \"pc00012\", \"name\": \"Chips\", \"price\": \"1.75\", \"quantity\": \"5\"}, \"13\": {\"code\": \"pc00013\", \"name\": \"Olives\", \"price\": \"0.85\", \"quantity\": \"6\"}, \"14\": {\"code\": \"pc00014\", \"name\": \"Oranges\", \"price\": \"0.50\", \"quantity\": \"8\"}, \"15\": {\"code\": \"pc00015\", \"name\": \"Red Bull\", \"price\": \"1.25\", \"quantity\": \"9\"}, \"16\": {\"code\": \"pc00016\", \"name\": \"Salmon\", \"price\": \"4.00\", \"quantity\": \"5\"}, \"17\": {\"code\": \"pc00017\", \"name\": \"Dignity\", \"price\": \"0.01\", \"quantity\": \"1\"}, \"18\": {\"code\": \"pc00018\", \"name\": \"Coffee\", \"price\": \"2.00\", \"quantity\": \"3\"}, \"19\": {\"code\": \"pc00019\", \"name\": \"Tea\", \"price\": \"1.50\", \"quantity\": \"7\"}, \"20\": {\"code\": \"pc00020\", \"name\": \"Nando's Sauce\", \"price\": \"3.00\", \"quantity\": \"99\"}}";

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Map map = gson.fromJson(defaultStock, Map.class);
                String json = gson.toJson(map);

                Repository.update(stockFile, json);
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        if(!accounts.exists()) {
            try {
                accounts.createNewFile();

                Map<String, String> map = new HashMap<>();

                map.put("Temp", "$2a$12$eRCYLwKDTEKjVg8PzNdrs.1BjUBTqkt3uDErPQ25xvFO5Z.bUlv2a");
                map.put("Xtrendence", "$2a$12$dSD3oMqS6vDCSTJ..KSPJunABiXda6z1fpHDswt228p74XKTw4W02");
                map.put("James", "$2a$12$8NVY5i/.tKE6ufjR7MImROLevix0A6sZAqdQHJFtE1LuBrkGQm6LS");
                map.put("Hannibal", "$2a$12$wH6FGLmvKJ.ao98RfEo9XudGtVcvMKEWfNAVPIZ7MxPNXcm94RV.2");

                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                Repository.update(accountsFile, gson.toJson(map));
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    /* Reads and returns the content of a file.
    *  @param filePath Path to a file.
    *  @return String The content of the file.
    */
    public static String read(String filePath) {
        try {
            StringBuilder builder = new StringBuilder();
            FileReader reader = new FileReader(filePath);
            Scanner scanner = new Scanner(reader);
            while(scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            scanner.close();
            return builder.toString();
        } catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /* Updates the content of an existing file.
    *  @param filePath Path to the file.
    *  @param data The data to be written into the file.
    *  @return Nothing.
    */
    public static void update(String filePath, String data) {
        try {
            FileWriter writer = new FileWriter(filePath, false);
            writer.write(data);
            writer.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    /* Deletes a file.
    *  @param filePath Path to the file.
    *  @return Nothing.
    */
    public static void delete(String filePath) {
        try {
            File file = new File(filePath);
            file.delete();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
