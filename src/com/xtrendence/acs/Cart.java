package com.xtrendence.acs;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    static Map<String, Integer> cart = new HashMap<String, Integer>();
    static float total;

    static void addToCart(String code, float price) {
        if(cart.containsKey(code)) {
            cart.put(code, cart.get(code) + 1);
        } else {
            cart.put(code, 1);
        }
        total += price;
    }

    static void removeFromCart(String code, float price) {
        if(cart.containsKey(code)) {
            int currentQuantity = cart.get(code);
            if(currentQuantity == 1) {
                cart.remove(code);
            } else {
                cart.put(code, cart.get(code) - 1);
            }
        }
        total -= price;
    }
}
