package com.xtrendence.acs.customers;
import com.xtrendence.acs.data.IObserver;
import com.xtrendence.acs.data.Cart;
import com.xtrendence.acs.data.Item;
import com.xtrendence.acs.data.Repository;
import com.xtrendence.acs.data.Stock;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CustomerArea extends JFrame implements IObserver {
    private static CustomerArea instance = new CustomerArea();
    private Cart cart = new Cart();
    public JPanel mainPanel;
    public JPanel navbar;
    public JPanel contentWrapper;
    public JPanel listWrapper;
    public JPanel actionsWrapper;
    public JLabel adminIcon;
    public JTable itemTable;
    public JScrollPane itemTableScrollPane;
    public JTextField inputProductCode;
    public JPanel scanWrapper;
    public JButton scanButton;
    public JTextPane scanOutput;
    public JPanel basketWrapper;
    public JTable scannedTable;
    public JScrollPane scannedTableScrollPane;
    public JTextPane itemTableTitle;
    public JTextPane scannedTableTitle;
    public JTextPane scannedTotal;
    public JButton checkoutButton;
    public JLabel navbarTitle;
    public JButton adminButton;

    public CustomerArea() {
        instance = this;

        String separator = System.getProperty("file.separator");
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + separator + "resources" + separator + "acs.png").getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH));
        this.setSize(1280, 720);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("X Mart - Customer Area");

        Stock.attach(this);

        CustomerAreaStyling styling = new CustomerAreaStyling();
        styling.applyStyle();

        adminButton.addActionListener(actionEvent -> {
            showLogin();
        });

        adminIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showLogin();
            }
        });

        itemTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if(me.getClickCount() == 2) {
                    String code = itemTable.getValueAt(itemTable.getSelectedRow(), 0).toString();
                    inputProductCode.setText(code);
                }
            }
        });

        createScannedTable(scannedTable);

        inputProductCode.addActionListener(e -> scanButton.doClick());

        scanButton.addActionListener(actionEvent -> {
            if(inputProductCode.isEnabled()) {
                String code = inputProductCode.getText();
                if(code != null && !code.equals("")) {
                    scanItem(code);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a product code, or double/right click on a product in the list to automatically enter its code.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        checkoutButton.addActionListener(actionEvent -> {
            showCheckout();
        });
    }

    public static void main(String[] args) {
        Repository.create();

        CustomerArea customerArea = CustomerArea.getInstance();

        Stock.getStock();

        customerArea.setVisible(true);
        customerArea.setContentPane(customerArea.mainPanel);

        customerArea.createItemPopupMenu();
        customerArea.createScannedPopupMenu();
    }

    // Singleton objects have a getInstance() method to return the one and only instance of the object.
    public static CustomerArea getInstance() {
        return instance;
    }

    private void showLogin() {
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.setLocation(adminIcon.getLocationOnScreen().x - 150, adminIcon.getLocationOnScreen().y + 50);
        loginDialog.setSize(200, 180);
        loginDialog.setResizable(false);
        loginDialog.setUndecorated(true);
        loginDialog.setVisible(true);
    }

    private void showCheckout() {
        if(cart.getSize() > 0) {
            CheckoutScreen checkoutScreen = new CheckoutScreen(cart);
            checkoutScreen.setVisible(true);
            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(null, "Please add something to your basket before checking out.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updateTables() {
        CustomerArea customerArea = CustomerArea.getInstance();

        emptyCart();

        updateItemTable(Stock.items, customerArea.itemTable);
        createScannedTable(customerArea.scannedTable);

        customerArea.scannedTotal.setText("Total: £0.00");
    }

    public void emptyCart() {
        cart.emptyCart();
    }

    private void scanItem(String code) {
        int delay = 2000;

        ActionListener hideOutput = actionEvent -> {
            scanOutput.setVisible(false);
            scanButton.setText("Scan Item");
            scanButton.setBackground(new Color(0,125,255));
            inputProductCode.setEnabled(true);
        };

        Timer timer = new Timer(delay, hideOutput);

        for(Item item : Stock.items) {
            if(item.getCode().equals(code) && item.getQuantity() >= 0) {
                if(cart.itemExists(code) && cart.getQuantity(code) >= item.getQuantity()) {
                    inputProductCode.setText("");
                    JOptionPane.showMessageDialog(null, "No more \"" + item.getName() + "\" in stock.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    selectRowByValue(itemTable, code);

                    TableModel model = itemTable.getModel();
                    int row = itemTable.getSelectedRow();
                    int currentQuantity = Integer.parseInt(model.getValueAt(row, 3).toString());
                    model.setValueAt(currentQuantity - 1, row, 3);

                    inputProductCode.setText("");
                    inputProductCode.setEnabled(false);

                    scanButton.setText("Please Wait...");
                    scanButton.setBackground(new Color(0,100,200));
                    scanOutput.setVisible(true);
                    scanOutput.setEditable(false);
                    scanOutput.setBackground(new Color(150, 135, 255));
                    scanOutput.setForeground(new Color(255,255,255));
                    scanOutput.setText("The item has been added to your shopping cart.");

                    addToScannedTable(scannedTotal, item, scannedTable);

                    timer.restart();
                }
            }
        }
    }

    public void selectRowByValue(JTable table, String value) {
        TableModel model = table.getModel();
        for(int i = model.getRowCount() - 1; i >= 0; --i) {
            if(model.getValueAt(i, 0).equals(value)) {
                table.setRowSelectionInterval(i, i);
            }
        }
    }

    public void createItemPopupMenu() {
        CustomerArea customerArea = CustomerArea.getInstance();
        JPopupMenu itemTablePopupMenu = new JPopupMenu();
        itemTablePopupMenu.setBackground(new Color(230,230,230));
        JMenuItem scanItem = new JMenuItem("Scan Item");
        scanItem.setSize(scanItem.getWidth(), 30);
        scanItem.setBackground(new Color(255,255,255));
        scanItem.setForeground(new Color(75,75,75));
        scanItem.addActionListener(e -> {
            String code = customerArea.itemTable.getValueAt(customerArea.itemTable.getSelectedRow(), 0).toString();
            customerArea.inputProductCode.setText(code);
        });
        itemTablePopupMenu.add(scanItem);
        customerArea.itemTable.setComponentPopupMenu(itemTablePopupMenu);

        itemTablePopupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    int rowAtCursor = customerArea.itemTable.rowAtPoint(SwingUtilities.convertPoint(itemTablePopupMenu, new Point(0, 0), customerArea.itemTable));
                    if(rowAtCursor > -1) {
                        customerArea.itemTable.setRowSelectionInterval(rowAtCursor, rowAtCursor);
                    }
                });
            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) { }
            @Override
            public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) { }
        });
    }

    public void createScannedPopupMenu() {
        CustomerArea customerArea = CustomerArea.getInstance();
        JPopupMenu scannedTablePopupMenu = new JPopupMenu();
        scannedTablePopupMenu.setBackground(new Color(230,230,230));
        JMenuItem removeItem = new JMenuItem("Remove Item");
        removeItem.setSize(removeItem.getWidth(), 30);
        removeItem.setBackground(new Color(255,255,255));
        removeItem.setForeground(new Color(75,75,75));
        removeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = customerArea.scannedTable.getSelectedRow();
                String code = customerArea.scannedTable.getValueAt(row, 0).toString();
                float price = Float.parseFloat(customerArea.scannedTable.getValueAt(row, 2).toString());
                cart.removeFromCart(code, price);
                customerArea.scannedTotal.setText("Total: £" + String.format("%.2f", cart.getTotal()));
                DefaultTableModel model = (DefaultTableModel) customerArea.scannedTable.getModel();
                model.removeRow(row);
                customerArea.scannedTable.setModel(model);
                customerArea.scannedTable.repaint();

                selectRowByValue(customerArea.itemTable, code);
                TableModel itemTableModel = customerArea.itemTable.getModel();
                int itemTableRow = customerArea.itemTable.getSelectedRow();
                int currentQuantity = Integer.parseInt(itemTableModel.getValueAt(itemTableRow, 3).toString());
                itemTableModel.setValueAt(currentQuantity + 1, itemTableRow, 3);
            }
        });
        scannedTablePopupMenu.add(removeItem);
        customerArea.scannedTable.setComponentPopupMenu(scannedTablePopupMenu);

        scannedTablePopupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    int rowAtCursor = customerArea.scannedTable.rowAtPoint(SwingUtilities.convertPoint(scannedTablePopupMenu, new Point(0, 0), customerArea.scannedTable));
                    if(rowAtCursor > -1) {
                        customerArea.scannedTable.setRowSelectionInterval(rowAtCursor, rowAtCursor);
                    }
                });
            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) { }
            @Override
            public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) { }
        });
    }

    public void addToScannedTable(JTextPane total, Item item, JTable table) {
        cart.addToCart(item.getCode(), item.getPrice());
        total.setText("Total: £" + String.format("%.2f", cart.getTotal()));
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{ item.getCode(), item.getName(), item.getPrice() });
        table.setModel(model);
        table.repaint();
    }

    public void updateItemTable(List<Item> stock, JTable table) {
        String[] columns = new String[]{ "Product Code", "Name", "Price (£)", "Remaining Quantity" };
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        for(Item item : stock) {
            if(item.getQuantity() > 0) {
                model.addRow(new Object[]{ item.getCode(), item.getName(), item.getPrice(), item.getQuantity() });
            }
        }
        table.setModel(model);
        table.repaint();
        table.getRowSorter().toggleSortOrder(0);
    }

    public void createScannedTable(JTable table) {
        String[] columns = new String[]{ "Product Code", "Name", "Price" };
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table.setModel(model);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.repaint();
    }
}
