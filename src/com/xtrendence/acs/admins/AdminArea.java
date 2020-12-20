package com.xtrendence.acs.admins;
import com.xtrendence.acs.Item;
import com.xtrendence.acs.Stock;
import com.xtrendence.acs.customers.CustomerArea;

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
import java.util.ArrayList;
import java.util.Collections;

public class AdminArea extends JFrame {
    private boolean delivered = false;
    public JPanel navbar;
    public JPanel mainPanel;
    public JLabel backButton;
    public JLabel companyTitle;
    private JPanel contentWrapper;
    private JPanel stockWrapper;
    private JScrollPane stockScrollPane;
    private JTable stockTable;
    private JTextPane stockLabel;
    private JPanel deliveryWrapper;
    private JScrollPane deliveryScrollPane;
    private JTextPane deliveryLabel;
    private JButton deliveryButton;
    private JTable deliveryTable;
    private JButton replenishButton;
    private JTextPane lowStockLabel;
    private JScrollPane lowStockScrollPane;
    private JTable lowStockTable;
    private JButton saveButton;
    private JButton removeButton;
    private JButton addButton;

    public AdminArea() {
        String separator = System.getProperty("file.separator");
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + separator + "resources" + separator + "acs.png").getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH));
        this.setSize(1280, 720);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("X Mart - Admin Area");

        navbar.setBackground(new Color(0, 125, 255));
        navbar.setSize(navbar.getWidth(), 60);

        try {
            BufferedImage userIcon = ImageIO.read(new File(System.getProperty("user.dir") + separator + "resources" + separator + "back.png"));
            backButton.setIcon(new ImageIcon(userIcon.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        } catch(Exception e) {
            System.out.println(e);
        }

        companyTitle.setFont(companyTitle.getFont().deriveFont(Font.BOLD, 20));
        companyTitle.setForeground(new Color(255,255,255));

        mainPanel.setBackground(new Color(235,235,235));
        contentWrapper.setBackground(new Color(235,235,235));

        stockScrollPane.getViewport().setBackground(new Color(255, 255, 255));
        lowStockScrollPane.getViewport().setBackground(new Color(255, 255, 255));
        deliveryScrollPane.getViewport().setBackground(new Color(255, 255, 255));

        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

        StyledDocument stockLabelText = stockLabel.getStyledDocument();
        stockLabelText.setParagraphAttributes(0, stockLabelText.getLength(), center, false);
        stockLabel.setFont(stockLabel.getFont().deriveFont(Font.BOLD, 16));
        stockLabel.setBackground(new Color(150,135,255));
        stockLabel.setForeground(new Color(255,255,255));
        stockLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        StyledDocument lowStockLabelText = lowStockLabel.getStyledDocument();
        lowStockLabelText.setParagraphAttributes(0, lowStockLabelText.getLength(), center, false);
        lowStockLabel.setFont(lowStockLabel.getFont().deriveFont(Font.BOLD, 16));
        lowStockLabel.setBackground(new Color(150,135,255));
        lowStockLabel.setForeground(new Color(255,255,255));
        lowStockLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        StyledDocument deliveryLabelText = deliveryLabel.getStyledDocument();
        deliveryLabelText.setParagraphAttributes(0, deliveryLabelText.getLength(), center, false);
        deliveryLabel.setFont(deliveryLabel.getFont().deriveFont(Font.BOLD, 16));
        deliveryLabel.setBackground(new Color(150,135,255));
        deliveryLabel.setForeground(new Color(255,255,255));
        deliveryLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        removeButton.setOpaque(true);
        removeButton.setBackground(new Color(200,50,50));
        removeButton.setForeground(new Color(255,255,255));

        addButton.setOpaque(true);
        addButton.setBackground(new Color(0,125,255));
        addButton.setForeground(new Color(255,255,255));

        saveButton.setOpaque(true);
        saveButton.setBackground(new Color(0,125,255));
        saveButton.setForeground(new Color(255,255,255));

        deliveryButton.setOpaque(true);
        deliveryButton.setBackground(new Color(0,125,255));
        deliveryButton.setForeground(new Color(255,255,255));

        replenishButton.setOpaque(true);
        replenishButton.setBackground(new Color(0, 75, 150));
        replenishButton.setForeground(new Color(255,255,255));

        JScrollBar stockScrollBar = new JScrollBar();
        stockScrollBar.setBackground(new Color(230,230,230));
        stockScrollBar.setPreferredSize(new Dimension(10, 40));
        stockScrollBar.setMinimumSize(new Dimension(10, 40));
        stockScrollBar.setMaximumSize(new Dimension(10, 2147483647));
        stockScrollBar.setBorder(BorderFactory.createEmptyBorder());

        JScrollBar lowStockScrollBar = new JScrollBar();
        lowStockScrollBar.setBackground(new Color(230,230,230));
        lowStockScrollBar.setPreferredSize(new Dimension(10, 40));
        lowStockScrollBar.setMinimumSize(new Dimension(10, 40));
        lowStockScrollBar.setMaximumSize(new Dimension(10, 2147483647));
        lowStockScrollBar.setBorder(BorderFactory.createEmptyBorder());

        JScrollBar deliveryScrollBar = new JScrollBar();
        deliveryScrollBar.setBackground(new Color(230,230,230));
        deliveryScrollBar.setPreferredSize(new Dimension(10, 40));
        deliveryScrollBar.setMinimumSize(new Dimension(10, 40));
        deliveryScrollBar.setMaximumSize(new Dimension(10, 2147483647));
        deliveryScrollBar.setBorder(BorderFactory.createEmptyBorder());

        stockScrollPane.setVerticalScrollBar(stockScrollBar);
        lowStockScrollPane.setVerticalScrollBar(lowStockScrollBar);
        deliveryScrollPane.setVerticalScrollBar(deliveryScrollBar);

        stockTable.setBackground(new Color(255, 255, 255));
        stockTable.setForeground(new Color(75,75,75));
        stockTable.setSelectionBackground(new Color(0,125,255));
        stockTable.setSelectionForeground(new Color(255,255,255));
        stockTable.setGridColor(new Color(230,230,230));
        stockTable.getTableHeader().setPreferredSize(new Dimension(stockTable.getTableHeader().getWidth(), 30));
        stockTable.getTableHeader().setReorderingAllowed(false);
        stockTable.setRowHeight(30);
        stockTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lowStockTable.setBackground(new Color(255, 255, 255));
        lowStockTable.setForeground(new Color(75,75,75));
        lowStockTable.setSelectionBackground(new Color(0,125,255));
        lowStockTable.setSelectionForeground(new Color(255,255,255));
        lowStockTable.setGridColor(new Color(230,230,230));
        lowStockTable.getTableHeader().setPreferredSize(new Dimension(lowStockTable.getTableHeader().getWidth(), 30));
        lowStockTable.getTableHeader().setReorderingAllowed(false);
        lowStockTable.setRowHeight(30);
        lowStockTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lowStockTable.setDefaultEditor(Object.class, null);

        deliveryTable.setBackground(new Color(255, 255, 255));
        deliveryTable.setForeground(new Color(75,75,75));
        deliveryTable.setSelectionBackground(new Color(0,125,255));
        deliveryTable.setSelectionForeground(new Color(255,255,255));
        deliveryTable.setGridColor(new Color(230,230,230));
        deliveryTable.getTableHeader().setPreferredSize(new Dimension(deliveryTable.getTableHeader().getWidth(), 30));
        deliveryTable.getTableHeader().setReorderingAllowed(false);
        deliveryTable.setRowHeight(30);
        deliveryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CustomerArea customerArea = CustomerArea.getInstance();
                customerArea.setVisible(true);
                dispose();
            }
        });

        removeButton.addActionListener(actionEvent -> {
            removeItem();
        });

        addButton.addActionListener(actionEvent -> {
            addToStockTable();
        });

        saveButton.addActionListener(actionEvent -> {

        });

        int delay = 3000;
        ActionListener deliverItems = actionEvent -> {
            delivered = true;
            deliveryButton.setText("Request Items");
            deliveryButton.setBackground(new Color(0, 75, 150));
            replenishButton.setBackground(new Color(0,125,255));
        };
        Timer timer = new Timer(delay, deliverItems);

        deliveryButton.addActionListener(actionEvent -> {
            if(!delivered) {
                deliveryButton.setText("Delivering...");
                deliveryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                deliveryTable.setEnabled(false);
                deliveryTable.setBackground(new Color(200, 200, 200));
                deliveryTable.setForeground(new Color(150,150,150));
                deliveryTable.setGridColor(new Color(230,230,230));
                timer.restart();
            } else {
                JOptionPane.showMessageDialog(null, "There's already a delivery that needs to be processed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        replenishButton.addActionListener(actionEvent -> {
            if(delivered) {
                timer.stop();
                deliveryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                deliveryTable.setEnabled(true);
                deliveryTable.setBackground(new Color(255, 255, 255));
                deliveryTable.setForeground(new Color(75,75,75));
                deliveryTable.setGridColor(new Color(230,230,230));
                delivered = false;
                deliveryButton.setBackground(new Color(0,125,255));
                replenishButton.setBackground(new Color(0, 75, 150));
            } else {
                JOptionPane.showMessageDialog(null, "Please order items for delivery first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) { }

    public void loadData(AdminArea adminArea) {
        Stock.getStock();

        createStockTable(Stock.items, adminArea.stockTable);
        createLowStockTable(Stock.items, adminArea.lowStockTable);
        createDeliveryTable(Stock.items, adminArea.deliveryTable);

        createStockPopupMenu(adminArea.stockTable);
    }

    public void createStockTable(java.util.List<Item> stock, JTable table) {
        String[] columns = new String[]{ "Product Code", "Name", "Price (£)", "Remaining Quantity" };
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 2 || column == 3;
            }
        };
        model.setColumnIdentifiers(columns);
        for(Item item : stock) {
            model.addRow(new Object[]{ item.getCode(), item.getName(), item.getPrice(), item.getQuantity() });
        }
        table.setModel(model);
        table.getRowSorter().toggleSortOrder(0);
    }

    public void createLowStockTable(java.util.List<Item> stock, JTable table) {
        String[] columns = new String[]{ "Product Code", "Remaining Quantity" };
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        for(Item item : stock) {
            if(item.getQuantity() < 5) {
                model.addRow(new Object[]{ item.getCode(), item.getQuantity() });
            }
        }
        table.setModel(model);
        table.getRowSorter().toggleSortOrder(1);
    }

    public void createDeliveryTable(java.util.List<Item> stock, JTable table) {
        String[] columns = new String[]{ "Product Code", "Quantity To Order" };
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        model.setColumnIdentifiers(columns);
        for(Item item : stock) {
            model.addRow(new Object[]{ item.getCode(), 0 });
        }
        table.setModel(model);
        table.getRowSorter().toggleSortOrder(0);
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
        String code = "- Product Code...";
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
