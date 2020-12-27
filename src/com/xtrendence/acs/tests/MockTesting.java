package com.xtrendence.acs.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtrendence.acs.controller.Cart;
import com.xtrendence.acs.controller.Stock;
import com.xtrendence.acs.model.Item;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

// A test class that utilizes Mockito's ability to mock a class, and return hardcoded values. This eliminates the dependency classes like Stock have on an external database (in this case, a flat file).
public class MockTesting {
    /* Mocks the Stock class to test the cart.
    *  @return Nothing.
    */
    @Test
    public void testCart() {
        System.out.println("Testing Cart with mock Stock.");

        MockedStatic<Stock> stock = mockStatic(Stock.class);

        String defaultStock = "{\"1\": {\"code\": \"pc00001\", \"name\": \"Milk\", \"price\": \"1.20\", \"quantity\": \"5\"}, \"2\": {\"code\": \"pc00002\", \"name\": \"Bread\", \"price\": \"1.10\", \"quantity\": \"10\"}, \"3\": {\"code\": \"pc00003\", \"name\": \"Eggs\", \"price\": \"0.70\", \"quantity\": \"3\"}, \"4\": {\"code\": \"pc00004\", \"name\": \"Beef\", \"price\": \"3.00\", \"quantity\": \"4\"}, \"5\": {\"code\": \"pc00005\", \"name\": \"Chicken\", \"price\": \"2.50\", \"quantity\": \"9\"}, \"6\": {\"code\": \"pc00006\", \"name\": \"Bacon\", \"price\": \"1.50\", \"quantity\": \"12\"}, \"7\": {\"code\": \"pc00007\", \"name\": \"Bananas\", \"price\": \"1.25\", \"quantity\": \"3\"}, \"8\": {\"code\": \"pc00008\", \"name\": \"Apples\", \"price\": \"1.35\", \"quantity\": \"2\"}, \"9\": {\"code\": \"pc00009\", \"name\": \"Peaches\", \"price\": \"1.20\", \"quantity\": \"1\"}, \"10\": {\"code\": \"pc00010\", \"name\": \"Chocolate\", \"price\": \"3.25\", \"quantity\": \"2\"}, \"11\": {\"code\": \"pc00011\", \"name\": \"Strawberries\", \"price\": \"2.75\", \"quantity\": \"4\"}, \"12\": {\"code\": \"pc00012\", \"name\": \"Chips\", \"price\": \"1.75\", \"quantity\": \"5\"}, \"13\": {\"code\": \"pc00013\", \"name\": \"Olives\", \"price\": \"0.85\", \"quantity\": \"6\"}, \"14\": {\"code\": \"pc00014\", \"name\": \"Oranges\", \"price\": \"0.50\", \"quantity\": \"8\"}, \"15\": {\"code\": \"pc00015\", \"name\": \"Red Bull\", \"price\": \"1.25\", \"quantity\": \"9\"}, \"16\": {\"code\": \"pc00016\", \"name\": \"Salmon\", \"price\": \"4.00\", \"quantity\": \"5\"}, \"17\": {\"code\": \"pc00017\", \"name\": \"Dignity\", \"price\": \"0.01\", \"quantity\": \"1\"}, \"18\": {\"code\": \"pc00018\", \"name\": \"Coffee\", \"price\": \"2.00\", \"quantity\": \"3\"}, \"19\": {\"code\": \"pc00019\", \"name\": \"Tea\", \"price\": \"1.50\", \"quantity\": \"7\"}, \"20\": {\"code\": \"pc00020\", \"name\": \"Nando's Sauce\", \"price\": \"3.00\", \"quantity\": \"99\"}}";

        Gson gson = new Gson();

        // TreeMaps keep their order, whereas HashMaps don't, so they're used in this case to ensure the format of the data is consistent.
        Type mapType = new TypeToken<TreeMap<String, TreeMap<String, String>>>() {}.getType();
        TreeMap<String, TreeMap<String, String>> map = gson.fromJson(defaultStock, mapType);

        int count = map.size();
        List<Item> items = new ArrayList<>();

        for(int i = 1; i <= count; i++) {
            TreeMap<String, String> entry = map.get(String.valueOf(i));
            Item item = new Item(entry.get("code"), entry.get("name"), Float.parseFloat(entry.get("price")), Integer.parseInt(entry.get("quantity")));
            items.add(item);
        }

        when(Stock.getStock()).thenReturn(items);
        when(Stock.getItem("pc00001")).thenReturn(new Item("pc00001", "Milk", (float) 1.20, 5));
        when(Stock.getItem("pc00002")).thenReturn(new Item("pc00002", "Bread", (float) 1.10, 10));
        when(Stock.getItem("pc00003")).thenReturn(new Item("pc00003", "Eggs", (float) 0.70, 3));

        assertEquals("The mock class should be returning the default stock items.", Stock.getStock(), items);

        Map<String, Integer> expectedCart = new HashMap<>();
        expectedCart.put("pc00001", 5);
        expectedCart.put("pc00002", 10);
        expectedCart.put("pc00003", 3);

        Cart cart = new Cart();

        for(int i = 1; i < 4; i++) {
            for(int j = 0; j < 10; j++) {
                Item item = Stock.getItem("pc0000" + i);
                if(item.getQuantity() > j) {
                    cart.addToCart(item.getCode(), item.getPrice());
                }
            }
        }

        assertEquals("The cart object should not have more items than available in the mock stock.", expectedCart, cart.getCart());

        System.out.println("Finished testing Cart with mock Stock.");
    }
}
