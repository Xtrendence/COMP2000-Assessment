package com.xtrendence.acs.tables;

import com.xtrendence.acs.data.Item;

import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

// Part of the Adapter design pattern.
public class TableAdapter implements ITableAdapter {
    /* Given a TableModel object, turn it into a List object containing Item objects.
    *  @param model The TableModel to convert.
    *  @return List A List object containing Item objects.
    */
    @Override
    public List<Item> getItems(TableModel model) {
        List<Item> items = new ArrayList<>();
        for(int i = model.getRowCount() - 1; i >= 0; --i) {
            try {
                String code = model.getValueAt(i, 0).toString();
                String name = model.getValueAt(i, 1).toString();
                float price = Float.parseFloat(model.getValueAt(i, 2).toString());
                int quantity = Integer.parseInt(model.getValueAt(i, 3).toString());
                Item item = new Item(code, name, price, quantity);
                items.add(item);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return items;
    }
}
