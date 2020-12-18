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
        String defaultStock = "{\"pc00001\": {\"Name\": \"Milk\", \"Price\": \"1.20\", \"Quantity\": \"5\"}, \"pc00002\": {\"name\": \"Bread\", \"price\": \"1.10\", \"quantity\": \"10\"}, \"pc00003\": {\"name\": \"Eggs\", \"price\": \"0.70\", \"quantity\": \"3\"}, \"pc00004\": {\"name\": \"Beef\", \"price\": \"3.00\", \"quantity\": \"4\"}, \"pc00005\": {\"name\": \"Chicken\", \"price\": \"2.50\", \"quantity\": \"9\"}, \"pc00006\": {\"name\": \"Bacon\", \"price\": \"1.50\", \"quantity\": \"12\"}, \"pc00007\": {\"name\": \"Bananas\", \"price\": \"1.25\", \"quantity\": \"3\"}, \"pc00008\": {\"name\": \"Apples\", \"price\": \"1.35\", \"quantity\": \"2\"}, \"pc00009\": {\"name\": \"Peaches\", \"price\": \"1.20\", \"quantity\": \"1\"}, \"pc00010\": {\"name\": \"Chocolate\", \"price\": \"3.25\", \"quantity\": \"2\"}, \"pc00011\": {\"name\": \"Strawberries\", \"price\": \"2.75\", \"quantity\": \"4\"}, \"pc00012\": {\"name\": \"Chips\", \"price\": \"1.75\", \"quantity\": \"5\"}, \"pc00013\": {\"name\": \"Olives\", \"price\": \"0.85\", \"quantity\": \"6\"}, \"pc00014\": {\"name\": \"Oranges\", \"price\": \"0.50\", \"quantity\": \"8\"}, \"pc00015\": {\"name\": \"Red Bull\", \"price\": \"1.25\", \"quantity\": \"9\"}, \"pc00016\": {\"name\": \"Salmon\", \"price\": \"4.00\", \"quantity\": \"5\"}, \"pc00017\": {\"name\": \"Dignity\", \"price\": \"0.01\", \"quantity\": \"1\"}, \"pc00018\": {\"name\": \"Coffee\", \"price\": \"2.00\", \"quantity\": \"3\"}, \"pc00019\": {\"name\": \"Tea\", \"price\": \"1.50\", \"quantity\": \"7\"}, \"pc00020\": {\"name\": \"Nando's Sauce\", \"price\": \"3.00\", \"quantity\": \"99\"}}";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map map = gson.fromJson(defaultStock, Map.class);
        String json = gson.toJson(map);
        DataAccess dataAccess = new DataAccess();
        DataAccess.writeFile(dataAccess.stockFile, json);
    }
}
