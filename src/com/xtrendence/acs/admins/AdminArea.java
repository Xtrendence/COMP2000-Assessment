package com.xtrendence.acs.admins;
import com.xtrendence.acs.data.IObserver;
import com.xtrendence.acs.accounts.Account;
import com.xtrendence.acs.data.Item;
import com.xtrendence.acs.data.Stock;
import com.xtrendence.acs.customers.CustomerArea;
import com.xtrendence.acs.tables.*;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

public class AdminArea extends JFrame implements IObserver {
    private boolean deliveryProcessingRequired = false;
    private java.util.List<Item> currentStock = new ArrayList<>();
    private Account account;
    private static AdminArea instance;
    public JPanel navbar;
    public JPanel mainPanel;
    public JLabel backButton;
    public JLabel navbarTitle;
    public JPanel contentWrapper;
    public JPanel stockWrapper;
    public JScrollPane stockScrollPane;
    public JTable stockTable;
    public JTextPane stockLabel;
    public JPanel deliveryWrapper;
    public JScrollPane deliveryScrollPane;
    public JTextPane deliveryLabel;
    public JButton deliveryButton;
    public JTable deliveryTable;
    public JButton replenishButton;
    public JTextPane lowStockLabel;
    public JScrollPane lowStockScrollPane;
    public JTable lowStockTable;
    public JButton saveButton;
    public JButton removeButton;
    public JButton addButton;
    public JButton customerButton;

    public AdminArea(Account account) {
        String separator = System.getProperty("file.separator");
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + separator + "resources" + separator + "acs.png").getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH));
        this.setSize(1280, 720);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("X Mart - Admin Area");

        this.instance = this;
        this.account = account;

        Stock.attach(this);

        AdminAreaStyling styling = new AdminAreaStyling(this);
        styling.applyStyle();

        navbarTitle.setText("Welcome, " + account.getUsername());

        customerButton.addActionListener(actionEvent -> {
            logout();
        });

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logout();
            }
        });

        removeButton.addActionListener(actionEvent -> {
            if(account.getState().loggedIn()) {
                if(!deliveryProcessingRequired) {
                    removeItem();
                } else {
                    JOptionPane.showMessageDialog(null, "There's a delivery that needs to be processed. Click the \"Replenish Stock\" button.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "You don't seem to be logged in...", "Error", JOptionPane.ERROR_MESSAGE);
                logout();
            }
        });

        addButton.addActionListener(actionEvent -> {
            if(account.getState().loggedIn()) {
                if(!deliveryProcessingRequired) {
                    addToStockTable();
                } else {
                    JOptionPane.showMessageDialog(null, "There's a delivery that needs to be processed. Click the \"Replenish Stock\" button.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "You don't seem to be logged in...", "Error", JOptionPane.ERROR_MESSAGE);
                logout();
            }
        });

        saveButton.addActionListener(actionEvent -> {
            if(account.getState().loggedIn()) {
                if(!deliveryProcessingRequired) {
                    TableCellEditor editor = stockTable.getCellEditor();
                    if(editor != null) {
                        editor.stopCellEditing();
                    }
                    DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
                    java.util.List<Item> updatedStock = new TableAdapter().getItems(model);
                    createStockTable(updatedStock, stockTable);
                    createLowStockTable(updatedStock, lowStockTable);
                    createDeliveryTable(updatedStock, deliveryTable);
                    currentStock = updatedStock;
                    Stock.setStock(currentStock);
                    Stock.getStock();
                    JOptionPane.showMessageDialog(null, "Changes have been saved.", "Saved", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "There's a delivery that needs to be processed. Click the \"Replenish Stock\" button.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "You don't seem to be logged in...", "Error", JOptionPane.ERROR_MESSAGE);
                logout();
            }
        });

        int delay = 3000;
        ActionListener deliverItems = actionEvent -> {
            deliveryButton.setText("Request Items");
            replenishButton.setBackground(new Color(0,125,255));
        };
        Timer timer = new Timer(delay, deliverItems);

        deliveryButton.addActionListener(actionEvent -> {
            if(account.getState().loggedIn()) {
                if(!deliveryProcessingRequired) {
                    TableCellEditor editor = deliveryTable.getCellEditor();
                    if(editor != null) {
                        editor.stopCellEditing();
                    }

                    deliveryProcessingRequired = true;

                    deliveryButton.setBackground(new Color(0, 75, 150));
                    deliveryButton.setText("Delivering...");

                    removeButton.setBackground(new Color(0, 75, 150));
                    addButton.setBackground(new Color(0, 75, 150));
                    saveButton.setBackground(new Color(0, 75, 150));

                    stockScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                    lowStockScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                    deliveryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

                    stockTable.setEnabled(false);
                    stockTable.setBackground(new Color(200, 200, 200));
                    stockTable.setForeground(new Color(150,150,150));
                    stockTable.setGridColor(new Color(230,230,230));

                    lowStockTable.setEnabled(false);
                    lowStockTable.setBackground(new Color(200, 200, 200));
                    lowStockTable.setForeground(new Color(150,150,150));
                    lowStockTable.setGridColor(new Color(230,230,230));

                    deliveryTable.setEnabled(false);
                    deliveryTable.setBackground(new Color(200, 200, 200));
                    deliveryTable.setForeground(new Color(150,150,150));
                    deliveryTable.setGridColor(new Color(230,230,230));

                    timer.restart();
                } else {
                    JOptionPane.showMessageDialog(null, "There's a delivery that needs to be processed. Click the \"Replenish Stock\" button.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "You don't seem to be logged in...", "Error", JOptionPane.ERROR_MESSAGE);
                logout();
            }
        });

        replenishButton.addActionListener(actionEvent -> {
            if(account.getState().loggedIn()) {
                if(deliveryProcessingRequired) {
                    timer.stop();

                    removeButton.setBackground(new Color(200,50,50));
                    addButton.setBackground(new Color(0,125,255));
                    saveButton.setBackground(new Color(0,125,255));

                    stockScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                    lowStockScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                    deliveryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                    stockTable.setEnabled(true);
                    stockTable.setBackground(new Color(255, 255, 255));
                    stockTable.setForeground(new Color(75,75,75));
                    stockTable.setGridColor(new Color(230,230,230));

                    lowStockTable.setEnabled(true);
                    lowStockTable.setBackground(new Color(255, 255, 255));
                    lowStockTable.setForeground(new Color(75,75,75));
                    lowStockTable.setGridColor(new Color(230,230,230));

                    deliveryTable.setEnabled(true);
                    deliveryTable.setBackground(new Color(255, 255, 255));
                    deliveryTable.setForeground(new Color(75,75,75));
                    deliveryTable.setGridColor(new Color(230,230,230));

                    deliveryProcessingRequired = false;
                    deliveryButton.setBackground(new Color(0,125,255));
                    replenishButton.setBackground(new Color(0, 75, 150));
                    replenishStock();
                    JOptionPane.showMessageDialog(null, "Stock replenished. Please remember to \"Save Changes\" to update the database.", "Reminder", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Please order items for delivery first.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "You don't seem to be logged in...", "Error", JOptionPane.ERROR_MESSAGE);
                logout();
            }
        });

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                logout();
                dispose();
            }
            @Override
            public void windowClosed(WindowEvent windowEvent) { }
            @Override
            public void windowOpened(WindowEvent windowEvent) { }
            @Override
            public void windowIconified(WindowEvent windowEvent) { }
            @Override
            public void windowDeiconified(WindowEvent windowEvent) { }
            @Override
            public void windowActivated(WindowEvent windowEvent) { }
            @Override
            public void windowDeactivated(WindowEvent windowEvent) { }
        });
    }

    public static void main(String[] args) { }

    public static AdminArea getInstance() {
        return instance;
    }

    public void logout() {
        account.logout();
        CustomerArea.getInstance().setVisible(true);
        dispose();
    }

    @Override
    public void updateTables() {
        AdminArea adminArea = AdminArea.getInstance();

        currentStock = Stock.items;

        createStockTable(Stock.items, adminArea.stockTable);
        createLowStockTable(Stock.items, adminArea.lowStockTable);
        createDeliveryTable(Stock.items, adminArea.deliveryTable);

        createStockPopupMenu(adminArea.stockTable);
    }

    public void createStockTable(java.util.List<Item> stock, JTable table) {
        StockTable stockTable = new StockTable();
        DefaultTableModel model = stockTable.create();
        model = stockTable.setItems(model, stock);
        table.setModel(model);
        table.getRowSorter().toggleSortOrder(0);
    }

    public void createLowStockTable(java.util.List<Item> stock, JTable table) {
        LowStockTable lowStockTable = new LowStockTable();
        DefaultTableModel model = lowStockTable.create();
        model = lowStockTable.setItems(model, stock);
        table.setModel(model);
        table.getRowSorter().toggleSortOrder(1);
    }

    public void createDeliveryTable(java.util.List<Item> stock, JTable table) {
        DeliveryTable deliveryTable = new DeliveryTable();
        DefaultTableModel model = deliveryTable.create();
        model = deliveryTable.setItems(model, stock);
        table.setModel(model);
        table.getRowSorter().toggleSortOrder(0);
    }

    public void replenishStock() {
        DefaultTableModel model = (DefaultTableModel) deliveryTable.getModel();
        java.util.List<Item> updatedStock = new ArrayList<>();
        for(Item item : currentStock) {
            for(int i = model.getRowCount() - 1; i >= 0; --i) {
                try {
                    String code = model.getValueAt(i, 0).toString();
                    int quantity = Integer.parseInt(model.getValueAt(i, 2).toString());
                    if(item.getCode().equals(code)) {
                        item.setQuantity(item.getQuantity() + quantity);
                    }
                    if(!updatedStock.contains(item)) {
                        updatedStock.add(item);
                    }
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        }
        createStockTable(updatedStock, stockTable);
        createLowStockTable(updatedStock, lowStockTable);
        createDeliveryTable(updatedStock, deliveryTable);
        currentStock = updatedStock;
    }

    public void removeItem() {
        if(!stockTable.getSelectionModel().isSelectionEmpty()) {
            int row = stockTable.getSelectedRow();
            String code = stockTable.getValueAt(row, 0).toString();
            DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
            model = deleteRowByCode(model, code);
            stockTable.setModel(model);
            stockTable.repaint();
        } else {
            JOptionPane.showMessageDialog(null, "Please select an item first.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String generateProductCode(DefaultTableModel model) {
        String code;
        ArrayList<Integer> codes = new ArrayList<>();
        for(int i = model.getRowCount() - 1; i >= 0; --i) {
            String productCode = model.getValueAt(i, 0).toString().replace("pc", "");
            try {
                int number = Integer.parseInt(productCode);
                codes.add(number);
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        int max = Collections.max(codes);
        code = "pc" + String.format("%05d", max + 1);
        return code;
    }

    public DefaultTableModel deleteRowByCode(DefaultTableModel model, String value) {
        for(int i = model.getRowCount() - 1; i >= 0; --i) {
            if(model.getValueAt(i, 0).equals(value)) {
                model.removeRow(i);
            }
        }
        return model;
    }

    public void addToStockTable() {
        DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
        String code = generateProductCode(model);
        model.addRow(new Object[]{ code, "Item Name...", "Price...", "Quantity..." });
        stockTable.setModel(model);
        stockTable.repaint();
        stockTable.getRowSorter().toggleSortOrder(0);
        if(!stockTable.getValueAt(0, 0).toString().equals(code)) {
            stockTable.getRowSorter().toggleSortOrder(0);
        }
    }

    public void createStockPopupMenu(JTable table) {
        JPopupMenu stockPopupMenu = new JPopupMenu();
        stockPopupMenu.setBackground(new Color(230,230,230));
        JMenuItem removeItem = new JMenuItem("Remove Item");
        removeItem.setSize(removeItem.getWidth(), 30);
        removeItem.setBackground(new Color(255,255,255));
        removeItem.setForeground(new Color(75,75,75));
        removeItem.addActionListener(e -> {
            removeItem();
        });
        stockPopupMenu.add(removeItem);
        stockTable.setComponentPopupMenu(stockPopupMenu);

        stockPopupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    int rowAtCursor = stockTable.rowAtPoint(SwingUtilities.convertPoint(stockPopupMenu, new Point(0, 0), stockTable));
                    if(rowAtCursor > -1) {
                        stockTable.setRowSelectionInterval(rowAtCursor, rowAtCursor);
                    }
                });
            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) { }
            @Override
            public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) { }
        });
    }
}
