package com.xtrendence.acs;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Stock {
    static List<Item> items;

    public void updateStock() {
        DataAccess dataAccess = new DataAccess();
        String content = DataAccess.readFile(dataAccess.stockFile);
        if(content != null) {
            try {
                Gson gson = new Gson();
                Type mapType = new TypeToken<Map<String,Map<String, String>>>() {}.getType();
                Map<String, Map<String, String>> map = gson.fromJson(content, mapType);

                int count = map.size();
                items = new ArrayList<Item>();

                for(int i = 1; i <= count; i++)
                {
                    Map<String, String> entry = map.get(String.valueOf(i));
                    Item item = new Item(entry.get("code"), entry.get("name"), Float.parseFloat(entry.get("price")), Integer.parseInt(entry.get("quantity")));
                    items.add(item);
                }
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }
}
