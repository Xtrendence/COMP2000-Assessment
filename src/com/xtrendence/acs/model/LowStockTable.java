package com.xtrendence.acs.model;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

// Part of the Factory design pattern.
public class LowStockTable implements ITableFactory {
    /* Create an empty model object only containing the appropriate columns to be used for the low stock table.
    *  @return DefaultTableModel The model object.
    */
    @Override
    public DefaultTableModel create() {
        String[] columns = new String[]{ "Product Code", "Remaining" };
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        return model;
    }

    /* Given a List object containing Item objects, add the items to a given table model as rows.
    *  @param tableModel The table model to add rows to.
    *  @param items The items to add to the model.
    *  @return DefaultTableModel The updated table model with the added rows.
    */
    @Override
    public DefaultTableModel setItems(TableModel tableModel, List<Item> items) {
        DefaultTableModel model = (DefaultTableModel) tableModel;
        for(Item item : items) {
            int lowStockThreshold = 5; // What the quantity of an item should be before it's considered to be low in stock.
            if(item.getQuantity() < lowStockThreshold) {
                model.addRow(new Object[]{ item.getCode(), item.getQuantity() });
            }
        }
        return model;
    }
}
