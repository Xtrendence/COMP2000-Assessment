package com.xtrendence.acs.controller;

import com.xtrendence.acs.model.Item;

import javax.swing.table.TableModel;
import java.util.List;

// Part of the Adapter design pattern.
public interface ITableAdapter {
    public List<Item> getItems(TableModel model); // Used to turn a TableModel object into a List object containing Item objects.
}
