package com.xtrendence.acs.controller;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.xtrendence.acs.annotations.Initialize;
import com.xtrendence.acs.annotations.JSONElement;
import com.xtrendence.acs.annotations.JSONSerializable;
import com.xtrendence.acs.model.IObserver;
import com.xtrendence.acs.model.Item;
import com.xtrendence.acs.model.Repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class Stock {
    // Part of the Observer design pattern. A List of classes implementing the IObserver interfaces.
    public static List<IObserver> observers = new ArrayList<>();

    // A List of Item objects.
    private static List<Item> items;

    /* Adds a class (that has the IObserver interfaces as an implementation) to the list of observers.
    *  @param observer The class to add to the list.
    *  @return Nothing.
    */
    public static void attach(IObserver observer) {
        observers.add(observer);
    }

    /* Calls the updateTables() method of every observer.
    *  @return Nothing.
    */
    public static void notifyAllObservers() {
        for(IObserver observer : observers) {
            observer.updateTables();
        }
    }

    public static List<Item> getItems() {
        return items;
    }

    /* Returns an Item object from the List of items given a product code.
    *  @param code The product code.
    *  @return Item The relevant item if found, null if not found.
    */
    public static Item getItem(String code) {
        for(Item item : items) {
            if(code.equals(item.getCode())) {
                return item;
            }
        }
        return null;
    }

    /* Replaces an Item object in the List.
    *  @param updatedItem The item to replace.
    *  @return Nothing.
    */
    public static void setItem(Item updatedItem) {
        for(Item item : items) {
            if(updatedItem.getCode().equals(item.getCode())) {
                items.set(items.indexOf(item), updatedItem);
                notifyAllObservers();
                break;
            }
        }
    }

    /* Reads the stock.json file and turns the JSON string into a TreeMap, and then each entry into an Item object.
    *  @return Nothing.
    */
    public static List<Item> getStock() {
        String content = Repository.read(Repository.stockFile);
        if(content != null) {
            try {
                Gson gson = new Gson();

                // TreeMaps keep their order, whereas HashMaps don't, so they're used in this case to ensure the format of the data is consistent.
                Type mapType = new TypeToken<TreeMap<String,TreeMap<String, String>>>() {}.getType();
                TreeMap<String, TreeMap<String, String>> map = gson.fromJson(content, mapType);

                int count = map.size();
                items = new ArrayList<>();

                for(int i = 1; i <= count; i++) {
                    TreeMap<String, String> entry = map.get(String.valueOf(i));
                    Item item = new Item(entry.get("code"), entry.get("name"), Float.parseFloat(entry.get("price")), Integer.parseInt(entry.get("quantity")));
                    items.add(item);
                }

                // When the Stock object is updated, all observers are notified.
                notifyAllObservers();
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        return items;
    }

    /* Overwrites the stock.json file with new stock data.
    *  @param updatedItems a List object containing all the Item objects to be written to the stock.json as a JSON string.
    *  @return Nothing.
    */
    public static void setStock(List<Item> updatedItems) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        TreeMap<Integer, TreeMap<String, String>> map = new TreeMap<>();

        for(int i = 0; i < updatedItems.size(); i++) {
            Item item = updatedItems.get(i);

            Class itemClass = item.getClass();

            for(Method method : itemClass.getDeclaredMethods()) {
                if(method.isAnnotationPresent(Initialize.class)) {
                    method.setAccessible(true);
                    try {
                        method.invoke(item);
                    } catch(Exception e) {
                        System.out.println(e);
                    }
                }
            }

            if(itemClass.isAnnotationPresent(JSONSerializable.class)) {
                int id = i + 1;
                String code = item.getCode();
                String name = item.getName();
                String price = String.format("%.2f", item.getPrice());
                String quantity = String.valueOf(item.getQuantity());

                TreeMap<String, String> properties = new TreeMap<>();

                for(Field field : itemClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    if(field.isAnnotationPresent(JSONElement.class)) {
                        properties.put("code", code);
                        properties.put("name", name);
                        properties.put("price", price);
                        properties.put("quantity", quantity);
                    }
                }

                map.put(id, properties);
            }
        }

        String json = gson.toJson(map);

        Repository.update(Repository.stockFile, json);

        // When the Stock object is updated, all observers are notified.
        notifyAllObservers();
    }
}
