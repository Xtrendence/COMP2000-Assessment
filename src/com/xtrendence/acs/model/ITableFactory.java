package com.xtrendence.acs.model;

import com.xtrendence.acs.model.Item;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

// Part of the Factory design pattern.
public interface ITableFactory {
    public DefaultTableModel create(); // Used to create an empty table model containing the appropriate columns.
    public DefaultTableModel setItems(TableModel tableModel, List<Item> items); // Used to populate the table model with data given a List object containing Item objects.
}
