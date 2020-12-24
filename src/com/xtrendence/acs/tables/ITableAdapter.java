package com.xtrendence.acs.tables;

import com.xtrendence.acs.data.Item;

import javax.swing.table.TableModel;
import java.util.List;

// Part of the Adapter design pattern.
public interface ITableAdapter {
    public List<Item> getItems(TableModel model); // Used to turn a TableModel object into a List object containing Item objects.
}
