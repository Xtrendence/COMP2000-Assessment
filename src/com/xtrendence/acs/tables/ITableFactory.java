package com.xtrendence.acs.tables;

import com.xtrendence.acs.data.Item;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

public interface ITableFactory {
    public DefaultTableModel create();
    public DefaultTableModel setItems(TableModel tableModel, List<Item> items);
}
