package com.xtrendence.acs.model;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

// Part of the Factory design pattern.
public class ScannedTable implements ITableFactory {
    /* Create an empty model object only containing the appropriate columns to be used for the scanned table.
    *  @return DefaultTableModel The model object.
    */
    @Override
    public DefaultTableModel create() {
        String[] columns = new String[]{ "Product Code", "Name", "Price" };
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
            model.addRow(new Object[]{ item.getCode(), item.getName(), item.getPrice() });
        }
        return model;
    }
}
