package com.xtrendence.acs;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class DataAccess {
    static String separator = System.getProperty("file.separator");
    static String resourcesFolder = System.getProperty("user.dir") + separator + "resources" + separator;
    static String accountsFile = resourcesFolder + "accounts.json";
    static String stockFile = resourcesFolder + "stock.json";

    static boolean writeFile(String filePath, String data) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(data);
            writer.close();
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    static String readFile(String filePath) {
        try {
            StringBuilder builder = new StringBuilder();
            FileReader reader = new FileReader(filePath);
            Scanner scanner = new Scanner(reader);
            while(scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            scanner.close();
            return builder.toString();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    static void generateFiles() {
        File resources = new File(DataAccess.resourcesFolder);
        File stock = new File(DataAccess.stockFile);
        File accounts = new File(DataAccess.accountsFile);
        resources.mkdir();
        if(!stock.exists()) {
            try {
                stock.createNewFile();
                String defaultStock = "{\"1\": {\"code\": \"pc00001\", \"name\": \"Milk\", \"price\": \"1.20\", \"quantity\": \"5\"}, \"2\": {\"code\": \"pc00002\", \"name\": \"Bread\", \"price\": \"1.10\", \"quantity\": \"10\"}, \"3\": {\"code\": \"pc00003\", \"name\": \"Eggs\", \"price\": \"0.70\", \"quantity\": \"3\"}, \"4\": {\"code\": \"pc00004\", \"name\": \"Beef\", \"price\": \"3.00\", \"quantity\": \"4\"}, \"5\": {\"code\": \"pc00005\", \"name\": \"Chicken\", \"price\": \"2.50\", \"quantity\": \"9\"}, \"6\": {\"code\": \"pc00006\", \"name\": \"Bacon\", \"price\": \"1.50\", \"quantity\": \"12\"}, \"7\": {\"code\": \"pc00007\", \"name\": \"Bananas\", \"price\": \"1.25\", \"quantity\": \"3\"}, \"8\": {\"code\": \"pc00008\", \"name\": \"Apples\", \"price\": \"1.35\", \"quantity\": \"2\"}, \"9\": {\"code\": \"pc00009\", \"name\": \"Peaches\", \"price\": \"1.20\", \"quantity\": \"1\"}, \"10\": {\"code\": \"pc00010\", \"name\": \"Chocolate\", \"price\": \"3.25\", \"quantity\": \"2\"}, \"11\": {\"code\": \"pc00011\", \"name\": \"Strawberries\", \"price\": \"2.75\", \"quantity\": \"4\"}, \"12\": {\"code\": \"pc00012\", \"name\": \"Chips\", \"price\": \"1.75\", \"quantity\": \"5\"}, \"13\": {\"code\": \"pc00013\", \"name\": \"Olives\", \"price\": \"0.85\", \"quantity\": \"6\"}, \"14\": {\"code\": \"pc00014\", \"name\": \"Oranges\", \"price\": \"0.50\", \"quantity\": \"8\"}, \"15\": {\"code\": \"pc00015\", \"name\": \"Red Bull\", \"price\": \"1.25\", \"quantity\": \"9\"}, \"16\": {\"code\": \"pc00016\", \"name\": \"Salmon\", \"price\": \"4.00\", \"quantity\": \"5\"}, \"17\": {\"code\": \"pc00017\", \"name\": \"Dignity\", \"price\": \"0.01\", \"quantity\": \"1\"}, \"18\": {\"code\": \"pc00018\", \"name\": \"Coffee\", \"price\": \"2.00\", \"quantity\": \"3\"}, \"19\": {\"code\": \"pc00019\", \"name\": \"Tea\", \"price\": \"1.50\", \"quantity\": \"7\"}, \"20\": {\"code\": \"pc00020\", \"name\": \"Nando's Sauce\", \"price\": \"3.00\", \"quantity\": \"99\"}}";
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Map map = gson.fromJson(defaultStock, Map.class);
                String json = gson.toJson(map);
                DataAccess.writeFile(stockFile, json);
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        if(!accounts.exists()) {
            try {
                Map<String, String> map = new HashMap<>();
                map.put("Xtrendence", "$2a$12$En.Y.4iXKapw5Eaio6YTqO.QJpnIhTiqEMbsHcy.PqWNrC5zKMCMy");
                map.put("James", "$2a$12$4yfvr1blOvtCdgT4ZVGlJuC6PTUzGBQtO3LeTpyL5dMcWinwobRYK");
                map.put("Hannibal", "$2a$12$egWDTPaYxBZ9PjRgxcGv5OP/XLXmL2R5jBpgifuH6eHk72hHbhVJm");
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                DataAccess.writeFile(accountsFile, gson.toJson(map));
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }
}
