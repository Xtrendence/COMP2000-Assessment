package com.xtrendence.acs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class Tests {
    public void resetAccounts() {
        Map<String, String> map = new HashMap<>();
        map.put("Xtrendence", "passw0rd");
        map.put("James", "admin");
        map.put("Hannibal", "fav4beans");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        DataAccess dataAccess = new DataAccess();
        DataAccess.writeFile(dataAccess.accountsFile, gson.toJson(map));
    }
    public void resetStock() {
        String defaultStock = "{\"1\": {\"code\": \"pc00001\", \"name\": \"Milk\", \"price\": \"1.20\", \"quantity\": \"5\"}, \"2\": {\"code\": \"pc00002\", \"name\": \"Bread\", \"price\": \"1.10\", \"quantity\": \"10\"}, \"3\": {\"code\": \"pc00003\", \"name\": \"Eggs\", \"price\": \"0.70\", \"quantity\": \"3\"}, \"4\": {\"code\": \"pc00004\", \"name\": \"Beef\", \"price\": \"3.00\", \"quantity\": \"4\"}, \"5\": {\"code\": \"pc00005\", \"name\": \"Chicken\", \"price\": \"2.50\", \"quantity\": \"9\"}, \"6\": {\"code\": \"pc00006\", \"name\": \"Bacon\", \"price\": \"1.50\", \"quantity\": \"12\"}, \"7\": {\"code\": \"pc00007\", \"name\": \"Bananas\", \"price\": \"1.25\", \"quantity\": \"3\"}, \"8\": {\"code\": \"pc00008\", \"name\": \"Apples\", \"price\": \"1.35\", \"quantity\": \"2\"}, \"9\": {\"code\": \"pc00009\", \"name\": \"Peaches\", \"price\": \"1.20\", \"quantity\": \"1\"}, \"10\": {\"code\": \"pc00010\", \"name\": \"Chocolate\", \"price\": \"3.25\", \"quantity\": \"2\"}, \"11\": {\"code\": \"pc00011\", \"name\": \"Strawberries\", \"price\": \"2.75\", \"quantity\": \"4\"}, \"12\": {\"code\": \"pc00012\", \"name\": \"Chips\", \"price\": \"1.75\", \"quantity\": \"5\"}, \"13\": {\"code\": \"pc00013\", \"name\": \"Olives\", \"price\": \"0.85\", \"quantity\": \"6\"}, \"14\": {\"code\": \"pc00014\", \"name\": \"Oranges\", \"price\": \"0.50\", \"quantity\": \"8\"}, \"15\": {\"code\": \"pc00015\", \"name\": \"Red Bull\", \"price\": \"1.25\", \"quantity\": \"9\"}, \"16\": {\"code\": \"pc00016\", \"name\": \"Salmon\", \"price\": \"4.00\", \"quantity\": \"5\"}, \"17\": {\"code\": \"pc00017\", \"name\": \"Dignity\", \"price\": \"0.01\", \"quantity\": \"1\"}, \"18\": {\"code\": \"pc00018\", \"name\": \"Coffee\", \"price\": \"2.00\", \"quantity\": \"3\"}, \"19\": {\"code\": \"pc00019\", \"name\": \"Tea\", \"price\": \"1.50\", \"quantity\": \"7\"}, \"20\": {\"code\": \"pc00020\", \"name\": \"Nando's Sauce\", \"price\": \"3.00\", \"quantity\": \"99\"}}";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map map = gson.fromJson(defaultStock, Map.class);
        String json = gson.toJson(map);
        DataAccess dataAccess = new DataAccess();
        DataAccess.writeFile(dataAccess.stockFile, json);
    }
}
