package com.xtrendence.acs.model;

import com.xtrendence.acs.annotations.Initialize;
import com.xtrendence.acs.annotations.JSONElement;
import com.xtrendence.acs.annotations.JSONSerializable;

// Each product has a unique ID (product code), name, price, and quantity.
@JSONSerializable
public class Item {
    @JSONElement
    private String code;

    @JSONElement
    private String name;

    @JSONElement
    private float price;

    @JSONElement
    private int quantity;

    @Initialize
    private void initializeCode() {
        this.code = this.code.toLowerCase();
        String firstTwo = this.code.substring(0, 2);
        if(!firstTwo.equals("pc")) {
            this.code = "pc" + this.code;
        }
    }

    public Item() { }

    public Item(String code, String name, float price, int quantity) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    /* Sets the product code of the item.
    *  @param code A product code.
    *  @return Nothing.
    */
    public void setCode(String code) {
        this.code = code;
    }

    /* Sets the name of the item.
    *  @param name A name.
    *  @return Nothing.
    */
    public void setName(String name) {
        this.name = name;
    }

    /* Sets the price of the item.
    *  @param price The price.
    *  @return Nothing.
    */
    public void setPrice(float price) {
        this.price = price;
    }

    /* Sets the quantity of the item.
    *  @param quantity The quantity.
    *  @return Nothing.
    */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /* Returns the product code of the item.
    *  @return String The product code.
    */
    public String getCode() {
        return code;
    }

    /* Returns the name of the item.
    *  @return String The name.
    */
    public String getName() {
        return name;
    }

    /* Returns the price of the item.
    *  @return float The price.
    */
    public float getPrice() {
        return price;
    }

    /* Returns the quantity of the item.
    *  @return int The quantity.
    */
    public int getQuantity() {
        return quantity;
    }
}
