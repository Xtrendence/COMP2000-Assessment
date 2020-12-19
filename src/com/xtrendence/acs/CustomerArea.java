package com.xtrendence.acs;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CustomerArea extends JFrame {
    private JPanel mainPanel;
    private JPanel navbar;
    private JPanel contentWrapper;
    private JPanel listWrapper;
    private JPanel actionsWrapper;
    private JLabel basketButton;
    private JLabel adminButton;
    private JTable itemTable;
    private JScrollPane tableScrollPane;
    private JTextField inputProductCode;
    private JPanel scanWrapper;
    private JButton scanButton;
    private JTextPane scanOutput;

    public CustomerArea() throws IOException {
        this.setSize(1280, 720);
        this.setLocation(150, 150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Customer Area");

        navbar.setBackground(new Color(0, 125, 255));
        navbar.setSize(navbar.getWidth(), 60);

        mainPanel.setBackground(new Color(245,245,245));

        BufferedImage basketIcon = ImageIO.read(new File(System.getProperty("user.dir") + "\\resources\\basket.png"));
        basketButton.setIcon(new ImageIcon(basketIcon.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));

        BufferedImage userIcon = ImageIO.read(new File(System.getProperty("user.dir") + "\\resources\\user.png"));
        adminButton.setIcon(new ImageIcon(userIcon.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));

        tableScrollPane.getViewport().setBackground(new Color(255, 255, 255));
        JScrollBar scrollBar = new JScrollBar();
        scrollBar.setBackground(new Color(230,230,230));
        scrollBar.setPreferredSize(new Dimension(10, 40));
        scrollBar.setMinimumSize(new Dimension(10, 40));
        scrollBar.setMaximumSize(new Dimension(20, 2147483647));
        scrollBar.setBorder(BorderFactory.createEmptyBorder());
        tableScrollPane.setVerticalScrollBar(scrollBar);
        itemTable.setBackground(new Color(255, 255, 255));
        itemTable.setForeground(new Color(75,75,75));
        itemTable.setSelectionBackground(new Color(0,125,255));
        itemTable.setSelectionForeground(new Color(255,255,255));
        itemTable.setGridColor(new Color(230,230,230));
        itemTable.getTableHeader().setPreferredSize(new Dimension(itemTable.getTableHeader().getWidth(), 30));
        itemTable.setRowHeight(30);
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTable.setDefaultEditor(Object.class, null);

        scanOutput.setVisible(false);
        scanOutput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scanButton.setBackground(new Color(0,125,255));
        scanButton.setForeground(new Color(255,255,255));
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
                    for(Item item : Stock.items) {
                        if(item.getCode().equals(code) && item.getQuantity() > 0) {
                            inputProductCode.setText("");
                            inputProductCode.setEnabled(false);
                            scanButton.setText("Please Wait...");
                            scanOutput.setVisible(true);
                            scanOutput.setEditable(false);
                            scanOutput.setBackground(new Color(143, 135, 255));
                            scanOutput.setForeground(new Color(255,255,255));
                            scanOutput.setText("The item has been added to your shopping cart.");
                            timer.restart();
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

        updateTable(Stock.items, customerArea.itemTable);

        customerArea.setVisible(true);
        customerArea.setContentPane(customerArea.mainPanel);

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(new Color(230,230,230));
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
        popupMenu.add(scanItem);
        customerArea.itemTable.setComponentPopupMenu(popupMenu);

        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtCursor = customerArea.itemTable.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), customerArea.itemTable));
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

    public static void updateTable(List<Item> stock, JTable table) {
        String[] columns = new String[]{ "Product Code", "Name", "Price", "Remaining Quantity" };
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        for(Item item : stock) {
            model.addRow(new Object[]{ item.getCode(), item.getName(), item.getPrice(), item.getQuantity() });
        }
        table.setModel(model);
        table.repaint();
        table.getRowSorter().toggleSortOrder(0);
    }
}
