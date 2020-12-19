package com.xtrendence.acs;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
        itemTable.setBackground(new Color(255, 255, 255));
        itemTable.setForeground(new Color(75,75,75));
        itemTable.setSelectionBackground(new Color(0,125,255));
        itemTable.setSelectionForeground(new Color(255,255,255));
        itemTable.getTableHeader().setPreferredSize(new Dimension(itemTable.getTableHeader().getWidth(), 30));
        itemTable.setRowHeight(30);
    }

    public static void main(String[] args) throws IOException {
        CustomerArea customerArea = new CustomerArea();

        Stock stock = new Stock();
        stock.updateStock();

        updateTable(stock, customerArea.itemTable);

        customerArea.setVisible(true);
        customerArea.setContentPane(customerArea.mainPanel);
    }

    public static void updateTable(Stock stock, JTable table) {
        String[] columns = new String[]{ "Product Code", "Name", "Price", "Remaining Quantity" };
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table.setModel(model);
        table.repaint();
    }
}
