package com.xtrendence.acs;
import com.google.gson.*;

import java.util.Map;

public class Stock {
    public Item[] items = new Item[2];

    public void updateStock() {
        DataAccess dataAccess = new DataAccess();
        String content = DataAccess.readFile(dataAccess.stockFile);
        Gson gson = new Gson();
        Map map = gson.fromJson(content, Map.class);
        for(Object products : map.entrySet()) {

        }
    }
}
