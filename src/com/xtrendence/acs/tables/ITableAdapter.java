package com.xtrendence.acs.tables;

import com.xtrendence.acs.data.Item;

import javax.swing.table.TableModel;
import java.util.List;

public interface ITableAdapter {
    public List<Item> getItems(TableModel model);
}
