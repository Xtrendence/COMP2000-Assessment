package com.xtrendence.acs.model;

import java.util.List;

// Part of the Observer design pattern. Ensures that all classes that implement the IObserver interfaces have an implementation of the updateTables() method.
public interface IObserver {
    public void updateTables(List<Item> stock);
}
