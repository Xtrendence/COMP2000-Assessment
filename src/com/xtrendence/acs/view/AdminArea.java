package com.xtrendence.acs.view;
import com.xtrendence.acs.model.IObserver;
import com.xtrendence.acs.model.Account;
import com.xtrendence.acs.controller.TableAdapter;
import com.xtrendence.acs.model.DeliveryTable;
import com.xtrendence.acs.model.Item;
import com.xtrendence.acs.controller.Stock;
import com.xtrendence.acs.model.LowStockTable;
import com.xtrendence.acs.model.StockTable;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

// AdminArea is one of the two classes that implements the IObserver method in order to automatically update its JTables when the stock is changed.
public class AdminArea extends JFrame implements IObserver {
    private boolean deliveryProcessingRequired = false; // When products are delivered, they need to be processed before more deliveries can be made, or the stock can be edited. This variable keeps track of that.
    private java.util.List<Item> currentStock = new ArrayList<>();
    private Account account; // Used to verify whether or not the admin is logged in before they can perform any CRUD functionality that involves writing data.
    private static AdminArea instance; // Part of the Singleton design pattern.
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
        // Depending on the OS, the file separator can be different (usually either / or \).
        String fs = System.getProperty("file.separator");

        // Sets the application icon.
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + fs + "resources" + fs + "acs.png").getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH));

        this.setSize(1280, 720);
        this.setLocation(100, 100);

        // The AdminArea window has an event listener that updates the CustomerArea stock table, so DO_NOTHING_ON_CLOSE is used to ensure the custom event listener is used rather than simply closing the window.
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.setTitle("X Mart - Admin Area");

        instance = this;
        this.account = account;

        // Part of the Observer design pattern. This essentially registers the AdminArea as a page that requires updating whenever the Stock object is changed.
        Stock.attach(this);

        // Most of the styling code is in a separate file to improve code readability.
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
                    // Commits any changes that were being made on the table.
                    TableCellEditor editor = stockTable.getCellEditor();
                    if(editor != null) {
                        editor.stopCellEditing();
                    }

                    DefaultTableModel model = (DefaultTableModel) stockTable.getModel();

                    // Part of the Adapter design pattern. A new TableAdapter object is created, which converts a JTable's model into a List object containing Item objects.
                    java.util.List<Item> updatedStock = new TableAdapter().getItems(model);

                    createStockTable(updatedStock, stockTable);
                    createLowStockTable(updatedStock, lowStockTable);
                    createDeliveryTable(updatedStock, deliveryTable);

                    currentStock = updatedStock;

                    // Updates the Stock object, which also triggers the observers to be updated.
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

        // Simulates waiting for a delivery to be made.
        int delay = 3000;
        ActionListener deliverItems = actionEvent -> {
            deliveryButton.setText("Request Items");
            replenishButton.setBackground(new Color(0,125,255));
        };
        Timer timer = new Timer(delay, deliverItems);

        deliveryButton.addActionListener(actionEvent -> {
            if(account.getState().loggedIn()) {
                if(!deliveryProcessingRequired) {
                    // Commits any changes that were being made to the delivery table.
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

                    // Starts the timer that simulates waiting for a delivery.
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
                // Logs the admin out before closing the window.
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

    /* Returns the current AdminArea instance.
    *  @return AdminArea The current AdminArea instance.
    */
    public static AdminArea getInstance() {
        return instance;
    }

    /* Logs the admin out.
    *  @return Nothing.
    */
    public void logout() {
        account.logout();
        CustomerArea.getInstance().setVisible(true);
        dispose();
    }

    /* Part of the Observer design pattern; used to update the JTable components whenever the Stock object is changed.
    *  @return Nothing.
    */
    @Override
    public void updateTables() {
        AdminArea adminArea = AdminArea.getInstance();

        currentStock = Stock.items;

        createStockTable(Stock.items, adminArea.stockTable);
        createLowStockTable(Stock.items, adminArea.lowStockTable);
        createDeliveryTable(Stock.items, adminArea.deliveryTable);

        createStockPopupMenu(adminArea.stockTable);
    }

    /* Creates the model for the stock table, and populates it with data.
    *  @param stock Items to populate the table with.
    *  @param table The JTable component to modify.
    *  @return Nothing.
    */
    public void createStockTable(java.util.List<Item> stock, JTable table) {
        StockTable stockTable = new StockTable(); // Part of the Factory design pattern.
        DefaultTableModel model = stockTable.create(); // Create an empty table model (with the appropriate columns).
        model = stockTable.setItems(model, stock); // Add data to the model.
        table.setModel(model); // Set the JTable's model to the data-filled model.
        table.getRowSorter().toggleSortOrder(0); // Sort by the first column's data (Product Code).
    }

    /* Creates the model for the low stock table, and populates it with data.
    *  @param stock Items to populate the table with.
    *  @param table The JTable component to modify.
    *  @return Nothing.
    */
    public void createLowStockTable(java.util.List<Item> stock, JTable table) {
        LowStockTable lowStockTable = new LowStockTable(); // Part of the Factory design pattern.
        DefaultTableModel model = lowStockTable.create(); // Create an empty table model (with the appropriate columns).
        model = lowStockTable.setItems(model, stock); // Add data to the model.
        table.setModel(model); // Set the JTable's model to the data-filled model.
        table.getRowSorter().toggleSortOrder(1); // Sort by the second column's data (Quantity).
    }

    /* Creates the model for the delivery table, and populates it with data.
    *  @param stock Items to populate the table with.
    *  @param table The JTable component to modify.
    *  @return Nothing.
    */
    public void createDeliveryTable(java.util.List<Item> stock, JTable table) {
        DeliveryTable deliveryTable = new DeliveryTable(); // Part of the Factory design pattern.
        DefaultTableModel model = deliveryTable.create(); // Create an empty table model (with the appropriate columns).
        model = deliveryTable.setItems(model, stock); // Add data to the model.
        table.setModel(model); // Set the JTable's model to the data-filled model.
        table.getRowSorter().toggleSortOrder(0); // Sort by the first column's data (Product Code).
    }

    /* Loops through the delivery JTable's model's rows, gets the quantity, and adds it to the corresponding item in the stock table.
    *  @return Nothing.
    */
    public void replenishStock() {
        DefaultTableModel model = (DefaultTableModel) deliveryTable.getModel();
        java.util.List<Item> updatedStock = new ArrayList<>();
        for(Item item : currentStock) {
            for(int i = model.getRowCount() - 1; i >= 0; --i) {
                try {
                    // The first column of each row would be the Product Code.
                    String code = model.getValueAt(i, 0).toString();
                    // The third column would be the quantity the admin ordered.
                    int quantity = Integer.parseInt(model.getValueAt(i, 2).toString());
                    // If the Item object that is currently being looped is the right one (based on the product code), then increment its quantity by however many of that item the admin ordered.
                    if(item.getCode().equals(code)) {
                        item.setQuantity(item.getQuantity() + quantity);
                    }
                    // If the Item object hasn't already been added to the list of updated stock, then add it.
                    if(!updatedStock.contains(item)) {
                        updatedStock.add(item);
                    }
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        }

        // Creates new models of the stock, low stock, and delivery table using the updated stock. Since the changes haven't actually been written to the stock.json file, the Observer pattern does not automatically update the tables here. This is to allow the admin to discard their changes easily.
        createStockTable(updatedStock, stockTable);
        createLowStockTable(updatedStock, lowStockTable);
        createDeliveryTable(updatedStock, deliveryTable);

        currentStock = updatedStock;
    }

    /* Remove an item from the store's stock.
    *  @return Nothing.
    */
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

    /* Automatically generates a product code based on the last item's product code. Makes the admin's job easier.
    *  @param model The table model.
    *  @return String An unused product code.
    */
    public String generateProductCode(DefaultTableModel model) {
        String code;

        // Since the Collections.max() method is used to find the current largest product code, the product codes need to be turned into integers, and added to an ArrayList object.
        ArrayList<Integer> codes = new ArrayList<>();

        // Loops over every row in the stock table.
        for(int i = model.getRowCount() - 1; i >= 0; --i) {
            // The product code would be in the first column of each row, and since product codes are prefixed with "pc", it needs to be removed before the code can be parsed as an integer.
            String productCode = model.getValueAt(i, 0).toString().replace("pc", "");

            try {
                int number = Integer.parseInt(productCode);
                codes.add(number);
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        // Get the largest product code.
        int max = Collections.max(codes);

        // The newly generated product code would be "pc" + the largest product code + 1.
        code = "pc" + String.format("%05d", max + 1);

        return code;
    }

    /* Removes a table row from a model based on the product code.
    *  @param model The table model.
    *  @param value The product code.
    *  @return DefaultTableModel The table model with the row removed..
    */
    public DefaultTableModel deleteRowByCode(DefaultTableModel model, String value) {
        for(int i = model.getRowCount() - 1; i >= 0; --i) {
            if(model.getValueAt(i, 0).equals(value)) {
                model.removeRow(i);
            }
        }
        return model;
    }

    /* Add a new empty item to the stock table.
    *  @return Nothing.
    */
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

    /* Add a popup menu to the stock JTable. Used to remove rows.
    *  @param table The JTable to attach the JPopupMenu to
    *  @return Nothing.
    */
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
