package com.xtrendence.acs.tables;

import com.xtrendence.acs.data.Item;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

public class LowStockTable implements ITableFactory {
    @Override
    public DefaultTableModel create() {
        String[] columns = new String[]{ "Product Code", "Remaining" };
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        return model;
    }

    @Override
    public DefaultTableModel setItems(TableModel tableModel, List<Item> items) {
        DefaultTableModel model = (DefaultTableModel) tableModel;
        for(Item item : items) {
            int lowStockThreshold = 5;
            if(item.getQuantity() < lowStockThreshold) {
                model.addRow(new Object[]{ item.getCode(), item.getQuantity() });
            }
        }
        return model;
    }
}
