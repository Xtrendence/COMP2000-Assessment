package com.xtrendence.acs.model;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

// Part of the Factory design pattern.
public class DeliveryTable implements ITableFactory {
    /* Create an empty model object only containing the appropriate columns to be used for the delivery table.
    *  @return DefaultTableModel The model object.
    */
    @Override
    public DefaultTableModel create() {
        String[] columns = new String[]{ "Product Code", "Name", "Quantity To Order" };
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
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
            model.addRow(new Object[]{ item.getCode(), item.getName(), 0 });
        }
        return model;
    }
}
