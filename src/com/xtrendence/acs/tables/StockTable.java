package com.xtrendence.acs.tables;

import com.xtrendence.acs.data.Item;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

public class StockTable implements ITableFactory {
    @Override
    public DefaultTableModel create() {
        String[] columns = new String[]{ "Product Code", "Name", "Price (£)", "Remaining Quantity" };
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 2 || column == 3;
            }
        };
        model.setColumnIdentifiers(columns);
        return model;
    }

    @Override
    public DefaultTableModel setItems(TableModel tableModel, List<Item> items) {
        DefaultTableModel model = (DefaultTableModel) tableModel;
        for(Item item : items) {
            model.addRow(new Object[]{ item.getCode(), item.getName(), item.getPrice(), item.getQuantity() });
        }
        return model;
    }
}
