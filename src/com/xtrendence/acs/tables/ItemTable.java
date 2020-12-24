package com.xtrendence.acs.tables;

import com.xtrendence.acs.data.Item;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

public class ItemTable implements ITableFactory {
    @Override
    public DefaultTableModel create() {
        String[] columns = new String[]{ "Product Code", "Name", "Price (£)", "Remaining Quantity" };
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        return model;
    }

    @Override
    public DefaultTableModel setItems(TableModel tableModel, List<Item> items) {
        DefaultTableModel model = (DefaultTableModel) tableModel;
        for(Item item : items) {
            if(item.getQuantity() > 0) {
                model.addRow(new Object[]{ item.getCode(), item.getName(), item.getPrice(), item.getQuantity() });
            }
        }
        return model;
    }
}
