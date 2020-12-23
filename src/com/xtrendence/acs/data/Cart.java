package com.xtrendence.acs.data;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<String, Integer> cart = new HashMap<String, Integer>();
    private float total;

    public Cart() { }

    public int getSize() {
        return cart.size();
    }

    public float getTotal() {
        return this.total;
    }

    public Map<String, Integer> getCart() {
        return cart;
    }

    public int getQuantity(String code) {
        if(itemExists(code)) {
            return cart.get(code);
        }
        return 0;
    }

    public boolean itemExists(String code) {
        return cart.containsKey(code);
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setCart(Map<String, Integer> cart) {
        this.cart = cart;
    }

    public void addToCart(String code, float price) {
        if(itemExists(code)) {
            cart.put(code, cart.get(code) + 1);
        } else {
            cart.put(code, 1);
        }
        total += price;
    }

    public void removeFromCart(String code, float price) {
        if(itemExists(code)) {
            int currentQuantity = cart.get(code);
            if(currentQuantity == 1) {
                cart.remove(code);
            } else {
                cart.put(code, cart.get(code) - 1);
            }
        }
        total -= price;
    }

    public void emptyCart() {
        cart.clear();
        total = 0;
    }
}
