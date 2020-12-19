package com.xtrendence.acs;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerArea extends JFrame {
    public JPanel mainPanel;
    public JPanel navbar;
    public JPanel contentWrapper;
    public JPanel listWrapper;
    public JPanel actionsWrapper;
    public JLabel adminButton;
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

    public CustomerArea() throws IOException {
        this.setSize(1280, 720);
        this.setLocation(150, 150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Customer Area");

        CustomerAreaStyling styling = new CustomerAreaStyling(this);
        styling.applyStyle();

        itemTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if(me.getClickCount() == 2) {
                    JTable target = (JTable) me.getSource();
                    String code = itemTable.getValueAt(itemTable.getSelectedRow(), 0).toString();
                    inputProductCode.setText(code);
                }
            }
        });

        createScannedTable(scannedTable);

        inputProductCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scanButton.doClick();
            }
        });

        int delay = 2000;
        ActionListener hideOutput = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scanOutput.setVisible(false);
                scanButton.setText("Scan Item");
                inputProductCode.setEnabled(true);
            }
        };
        Timer timer = new Timer(delay, hideOutput);

        scanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(inputProductCode.isEnabled()) {
                    String code = inputProductCode.getText();
                    if(code != null && !code.equals("")) {
                        for(Item item : Stock.items) {
                            if(item.getCode().equals(code) && item.getQuantity() >= 0) {
                                if(Cart.cart.containsKey(code) && Cart.cart.get(code) >= item.getQuantity()) {
                                    inputProductCode.setText("");
                                    JOptionPane.showMessageDialog(null, "No more \"" + item.getName() + "\" in stock.", "Error", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    selectRowByValue(itemTable, code);
                                    inputProductCode.setText("");
                                    inputProductCode.setEnabled(false);
                                    scanButton.setText("Please Wait...");
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
                }
            }
        });
    }

    public static void main(String[] args) throws IOException {
        CustomerArea customerArea = new CustomerArea();

        Stock stock = new Stock();
        stock.updateStock();

        updateItemTable(Stock.items, customerArea.itemTable);

        customerArea.setVisible(true);
        customerArea.setContentPane(customerArea.mainPanel);

        createItemPopupMenu(customerArea);
        createScannedPopupMenu(customerArea);
    }

    public static void selectRowByValue(JTable table, String value) {
        TableModel model = table.getModel();
        for(int i = model.getRowCount() - 1; i >= 0; --i) {
            if(model.getValueAt(i, 0).equals(value)) {
                table.setRowSelectionInterval(i, i);
            }
        }
    }

    public static void createItemPopupMenu(CustomerArea customerArea) {
        JPopupMenu itemTablePopupMenu = new JPopupMenu();
        itemTablePopupMenu.setBackground(new Color(230,230,230));
        JMenuItem scanItem = new JMenuItem("Scan Item");
        scanItem.setSize(scanItem.getWidth(), 30);
        scanItem.setBackground(new Color(255,255,255));
        scanItem.setForeground(new Color(75,75,75));
        scanItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = customerArea.itemTable.getValueAt(customerArea.itemTable.getSelectedRow(), 0).toString();
                customerArea.inputProductCode.setText(code);
            }
        });
        itemTablePopupMenu.add(scanItem);
        customerArea.itemTable.setComponentPopupMenu(itemTablePopupMenu);

        itemTablePopupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtCursor = customerArea.itemTable.rowAtPoint(SwingUtilities.convertPoint(itemTablePopupMenu, new Point(0, 0), customerArea.itemTable));
                        if(rowAtCursor > -1) {
                            customerArea.itemTable.setRowSelectionInterval(rowAtCursor, rowAtCursor);
                        }
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {

            }
        });
    }

    public static void createScannedPopupMenu(CustomerArea customerArea) {
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
                Cart.removeFromCart(code, price);
                customerArea.scannedTotal.setText("Total: £" + String.format("%.2f", Cart.total));
                DefaultTableModel model = (DefaultTableModel) customerArea.scannedTable.getModel();
                model.removeRow(row);
                customerArea.scannedTable.setModel(model);
                customerArea.scannedTable.repaint();
            }
        });
        scannedTablePopupMenu.add(removeItem);
        customerArea.scannedTable.setComponentPopupMenu(scannedTablePopupMenu);

        scannedTablePopupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtCursor = customerArea.scannedTable.rowAtPoint(SwingUtilities.convertPoint(scannedTablePopupMenu, new Point(0, 0), customerArea.scannedTable));
                        if(rowAtCursor > -1) {
                            customerArea.scannedTable.setRowSelectionInterval(rowAtCursor, rowAtCursor);
                        }
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {

            }
        });
    }

    public static void addToScannedTable(JTextPane total, Item item, JTable table) {
        Cart.addToCart(item.getCode(), item.getPrice());
        total.setText("Total: £" + String.format("%.2f", Cart.total));
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{ item.getCode(), item.getName(), item.getPrice() });
        table.setModel(model);
        table.repaint();
    }

    public static void updateItemTable(List<Item> stock, JTable table) {
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

    public static void createScannedTable(JTable table) {
        String[] columns = new String[]{ "Product Code", "Name", "Price" };
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table.setModel(model);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.repaint();
    }
}
