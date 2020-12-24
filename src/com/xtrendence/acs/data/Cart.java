package com.xtrendence.acs.data;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    // The customer shopping cart stores items in a HashMap, where the keys are product codes, and the values are quantities.
    private Map<String, Integer> cart = new HashMap<String, Integer>();

    // The total price of the items in the customer's cart.
    private float total;

    public Cart() { }

    /* Returns how many items the customer has in their shopping cart.
    *  @return int The number of items in the cart.
    */
    public int getSize() {
        return cart.size();
    }

    /* Returns the overall price of all the items in the shopping cart.
    *  @return float The total price of the items.
    */
    public float getTotal() {
        return this.total;
    }

    /* Returns the shopping cart HashMap.
    *  @return Map The shopping cart HashMap.
    */
    public Map<String, Integer> getCart() {
        return cart;
    }

    /* Returns how many of a particular item the customer has in their shopping cart.
    *  @param code The product code.
    *  @return int How many of a particular item the customer has in their cart.
    */
    public int getQuantity(String code) {
        if(itemExists(code)) {
            return cart.get(code);
        }
        return 0;
    }

    /* Uses the product code to check and see whether or not an item exists in the cart.
    *  @param code The product code to check.
    *  @return boolean Whether or not an item exists in the cart.
    */
    public boolean itemExists(String code) {
        return cart.containsKey(code);
    }

    /* Sets the total price of the items in the cart.
    *  @param total The total price.
    *  @return Nothing.
    */
    public void setTotal(float total) {
        this.total = total;
    }

    /* Sets the cart HashMap items.
    *  @param cart A HashMap to replace the current cart with.
    *  @return Nothing.
    */
    public void setCart(Map<String, Integer> cart) {
        this.cart = cart;
    }

    /* Adds an item to the cart. If it exists, then its quantity is incremented by 1. If it doesn't then the quantity is set to 1. The price is added to the total of the cart.
    *  @param code A product code.
    *  @param price The price of the item.
    *  @return Nothing.
    */
    public void addToCart(String code, float price) {
        if(itemExists(code)) {
            cart.put(code, cart.get(code) + 1);
        } else {
            cart.put(code, 1);
        }
        total += price;
    }

    /* Removes an item from the cart if it exists. If its quantity is at 1, then it's removed entirely, otherwise, its quantity is reduced by 1. The total price of the cart is also decremented by the price of the product.
    *  @return Nothing.
    */
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

    /* Empties the shopping cart.
    *  @return Nothing.
    */
    public void emptyCart() {
        cart.clear();
        total = 0;
    }
}
