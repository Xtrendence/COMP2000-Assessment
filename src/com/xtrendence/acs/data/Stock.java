package com.xtrendence.acs.data;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

public class Stock {
    public static List<IObserver> observers = new ArrayList<>();
    public static List<Item> items;

    public static void attach(IObserver observer) {
        observers.add(observer);
    }

    public static void notifyAllObservers() {
        for(IObserver observer : observers) {
            observer.updateTables();
        }
    }

    public static Item getItem(String code) {
        for(Item item : items) {
            if(code.equals(item.getCode())) {
                return item;
            }
        }
        return null;
    }

    public static void setItem(Item updatedItem) {
        for(Item item : items) {
            if(updatedItem.getCode().equals(item.getCode())) {
                items.remove(item);
                items.add(updatedItem);
                notifyAllObservers();
            }
        }
    }

    public static void getStock() {
        String content = Repository.read(Repository.stockFile);
        if(content != null) {
            try {
                Gson gson = new Gson();
                Type mapType = new TypeToken<TreeMap<String,TreeMap<String, String>>>() {}.getType();
                TreeMap<String, TreeMap<String, String>> map = gson.fromJson(content, mapType);

                int count = map.size();
                items = new ArrayList<>();

                for(int i = 1; i <= count; i++) {
                    TreeMap<String, String> entry = map.get(String.valueOf(i));
                    Item item = new Item(entry.get("code"), entry.get("name"), Float.parseFloat(entry.get("price")), Integer.parseInt(entry.get("quantity")));
                    items.add(item);
                }

                notifyAllObservers();
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    public static void setStock(List<Item> updatedItems) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        TreeMap<Integer, TreeMap<String, String>> map = new TreeMap<>();
        for(int i = 0; i < updatedItems.size(); i++) {
            Item item = updatedItems.get(i);

            int id = i + 1;
            String code = item.getCode();
            String name = item.getName();
            String price = String.format("%.2f", item.getPrice());
            String quantity = String.valueOf(item.getQuantity());

            TreeMap<String, String> properties = new TreeMap<>();

            properties.put("code", code);
            properties.put("name", name);
            properties.put("price", price);
            properties.put("quantity", quantity);

            map.put(id, properties);
        }
        String json = gson.toJson(map);
        Repository.update(Repository.stockFile, json);
        notifyAllObservers();
    }
}
