package com.xtrendence.acs.view;
import com.xtrendence.acs.model.IObserver;
import com.xtrendence.acs.controller.Cart;
import com.xtrendence.acs.model.Item;
import com.xtrendence.acs.model.Repository;
import com.xtrendence.acs.controller.Stock;
import com.xtrendence.acs.model.ScannedTable;
import com.xtrendence.acs.model.ItemTable;
import com.xtrendence.acs.tests.MockTesting;
import com.xtrendence.acs.tests.Testing;

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

// CustomerArea is one of the two classes that implements the IObserver method in order to automatically update its JTables when the stock is changed.
public class CustomerArea extends JFrame implements IObserver {
    private static CustomerArea instance = new CustomerArea(); // Part of the Singleton design pattern.
    private Cart cart = new Cart(); // An empty Cart object is created for the customer to add items to.
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

        // Depending on the OS, the file separator can be different (usually either / or \).
        String fs = System.getProperty("file.separator");

        // Sets the application icon.
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + fs + "resources" + fs + "acs.png").getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH));
        this.setSize(1280, 720);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("X Mart - Customer Area");

        // Part of the Observer design pattern. This essentially registers the CustomerArea as a page that requires updating whenever the Stock object is changed.
        Stock.attach(this);

        // Most of the styling code is in a separate file to improve code readability.
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

        // Allows the customer to double-click on a row in the item JTable and autofill the inputProductCode JTextField.
        itemTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if(me.getClickCount() == 2) {
                    String code = itemTable.getValueAt(itemTable.getSelectedRow(), 0).toString();
                    inputProductCode.setText(code);
                }
            }
        });

        // Creates an empty JTable to act as a visual representation of the customer's cart.
        createScannedTable(scannedTable);

        // If the enter key is pressed, the product code gets scanned.
        inputProductCode.addActionListener(e -> scanButton.doClick());

        scanButton.addActionListener(actionEvent -> {
            // Since there is a short artificial wait time interval between item scans, this checks to ensure the wait is over.
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
        // Generates the necessary files for the application to function (but only if they don't exist already).
        Repository.create();

        try {
            // Run all the regular tests that check the actual functionality of the application.
            Testing testing = new Testing();
            testing.testAll();

            // Run the mock test that ensures the Stock class works correctly without having to depend on a database.
            MockTesting mockTesting = new MockTesting();
            mockTesting.testCart();

            // Use a separate thread for testing login/logout functionality as bcrypt's hash comparison time could freeze up the GUI.
            new Thread(testing::testAccount).start();
        } catch(Exception e) {
            System.out.println(e);
        }

        CustomerArea customerArea = CustomerArea.getInstance();

        // Fetches the store's stock using the Stock class' getStock() method.
        Stock.getStock();

        customerArea.setVisible(true);
        customerArea.setContentPane(customerArea.mainPanel);

        customerArea.createItemPopupMenu();
        customerArea.createScannedPopupMenu();
    }

    /* Singleton objects have a getInstance() method to return the one and only instance of the object.
    *  @return CustomerArea The singular CustomerArea instance.
    */
    public static CustomerArea getInstance() {
        return instance;
    }

    /* Shows the LoginDialog GUI.
    *  @return Nothing.
    */
    private void showLogin() {
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.setLocation(adminIcon.getLocationOnScreen().x - 150, adminIcon.getLocationOnScreen().y + 50);
        loginDialog.setSize(200, 180);
        loginDialog.setResizable(false);
        loginDialog.setUndecorated(true);
        loginDialog.setVisible(true);
    }

    /* Shows the CheckoutScreen GUI.
    *  @return Nothing.
    */
    private void showCheckout() {
        if(cart.getSize() > 0) {
            CheckoutScreen checkoutScreen = new CheckoutScreen(cart);
            checkoutScreen.setVisible(true);
            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(null, "Please add something to your basket before checking out.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /* Part of the Observer design pattern. When triggered from the Stock class, it updates the two tables in the CustomerArea.
    *  @return Nothing.
    */
    @Override
    public void updateTables(List<Item> stock) {
        CustomerArea customerArea = CustomerArea.getInstance();

        emptyCart();

        createItemTable(stock, customerArea.itemTable);
        createScannedTable(customerArea.scannedTable);

        customerArea.scannedTotal.setText("Total: £0.00");
    }

    /* Empties the customer's shopping cart.
    *  @return Nothing.
    */
    private void emptyCart() {
        cart.emptyCart();
    }

    /* Takes a product code as a parameter, and adds the item to the scanned table.
    *  @param code A product code.
    *  @return Nothing.
    */
    private void scanItem(String code) {
        int delay = 2000; // Time interval required between each item scan.

        // Shows a notification to let the customer know their item was scanned.
        ActionListener hideOutput = actionEvent -> {
            scanOutput.setVisible(false);
            scanButton.setText("Scan Item");
            scanButton.setBackground(new Color(0,125,255));
            inputProductCode.setEnabled(true);
        };

        Timer timer = new Timer(delay, hideOutput);
        timer.setRepeats(false);

        for(Item item : Stock.getItems()) {
            // Separate "if" statements in order to break the loop when the item is found (for better performance).
            if(item.getCode().equals(code)) {
                // Ensures the item is actually in stock.
                if(item.getQuantity() >= 0) {
                    // Ensures the customer isn't buying more than the available amount of the desired item.
                    if (cart.itemExists(code) && cart.getQuantity(code) >= item.getQuantity()) {
                        inputProductCode.setText("");
                        JOptionPane.showMessageDialog(null, "No more \"" + item.getName() + "\" in stock.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Selects the corresponding JTable row.
                        selectRowByValue(itemTable, code);

                        // Decrements the item's quantity by 1 in the item table. This is only a visual change, no data is written until checkout.
                        TableModel model = itemTable.getModel();
                        int row = itemTable.getSelectedRow();
                        int currentQuantity = Integer.parseInt(model.getValueAt(row, 3).toString());
                        model.setValueAt(currentQuantity - 1, row, 3);

                        inputProductCode.setText("");
                        inputProductCode.setEnabled(false);

                        scanButton.setText("Please Wait...");
                        scanButton.setBackground(new Color(0, 100, 200));
                        scanOutput.setVisible(true);
                        scanOutput.setEditable(false);
                        scanOutput.setBackground(new Color(150, 135, 255));
                        scanOutput.setForeground(new Color(255, 255, 255));
                        scanOutput.setText("The item has been added to your shopping cart.");

                        addToScannedTable(scannedTotal, item, scannedTable);

                        timer.restart();
                    }
                }
                break;
            }
        }
    }

    /* Selects a JTable row given a value that would be found in the first column.
    *  @param table The JTable component to search.
    *  @param value The value to search for.
    *  @return Nothing.
    */
    public void selectRowByValue(JTable table, String value) {
        TableModel model = table.getModel();
        for(int i = model.getRowCount() - 1; i >= 0; --i) {
            if(model.getValueAt(i, 0).equals(value)) {
                table.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    /* Creates a JPopupMenu and adds it to the item table. Used to scan items quicker.
    *  @return Nothing.
    */
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

    /* Creates a JPopupMenu and adds it to the scanned table. Allows the customer to remove items from their cart.
    *  @return Nothing.
    */
    public void createScannedPopupMenu() {
        CustomerArea customerArea = CustomerArea.getInstance();

        JPopupMenu scannedTablePopupMenu = new JPopupMenu();
        scannedTablePopupMenu.setBackground(new Color(230,230,230));

        JMenuItem removeItem = new JMenuItem("Remove Item");
        removeItem.setSize(removeItem.getWidth(), 30);
        removeItem.setBackground(new Color(255,255,255));
        removeItem.setForeground(new Color(75,75,75));

        // Remove the selected row from the scanned table, and increment the corresponding row's quantity column (based on the product code) in the item table by 1.
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

    /*
    *  @param total The current total cost of every item in the customer's shopping cart.
    *  @param item The item to add to the scanned table.
    *  @param table The JTable to add the row to.
    *  @return Nothing.
    */
    public void addToScannedTable(JTextPane total, Item item, JTable table) {
        cart.addToCart(item.getCode(), item.getPrice());
        total.setText("Total: £" + String.format("%.2f", cart.getTotal()));
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{ item.getCode(), item.getName(), item.getPrice() });
        table.setModel(model);
        table.repaint();
    }

    /* Create a model for the item table, and populate it with data.
    *  @param stock A list of Item objects to add to the table model.
    *  @param table The JTable component to modify.
    *  @return Nothing.
    */
    public void createItemTable(List<Item> stock, JTable table) {
        ItemTable itemTable = new ItemTable(); // Part of the Factory design pattern.
        DefaultTableModel model = itemTable.create(); // Creates an empty itemTable model with the relevant columns (as determined and as part of the Factory design pattern).
        model = itemTable.setItems(model, stock); // Add the Item objects to the table model as rows.
        table.setModel(model);
        table.repaint();
        table.getRowSorter().toggleSortOrder(0);
    }

    /* Create the scanned table.
    *  @param table The JTable component to modify.
    *  @return Nothing.
    */
    public void createScannedTable(JTable table) {
        DefaultTableModel model = new ScannedTable().create(); // Part of the Factory design pattern. Creates an empty scannedTable model with the appropriate columns.
        table.setModel(model);

        // The scanned table has a hidden column wth each product's code, and it is sorted by this column.
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        table.repaint();
    }
}
