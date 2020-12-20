package com.xtrendence.acs.admins;
import com.xtrendence.acs.customers.CustomerArea;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

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
        deliveryScrollPane.getViewport().setBackground(new Color(255, 255, 255));

        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

        StyledDocument stockLabelText = stockLabel.getStyledDocument();
        stockLabelText.setParagraphAttributes(0, stockLabelText.getLength(), center, false);
        stockLabel.setFont(stockLabel.getFont().deriveFont(Font.BOLD, 16));
        stockLabel.setBackground(new Color(150,135,255));
        stockLabel.setForeground(new Color(255,255,255));
        stockLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        StyledDocument deliveryLabelText = deliveryLabel.getStyledDocument();
        deliveryLabelText.setParagraphAttributes(0, deliveryLabelText.getLength(), center, false);
        deliveryLabel.setFont(deliveryLabel.getFont().deriveFont(Font.BOLD, 16));
        deliveryLabel.setBackground(new Color(150,135,255));
        deliveryLabel.setForeground(new Color(255,255,255));
        deliveryLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

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

        JScrollBar deliveryScrollBar = new JScrollBar();
        deliveryScrollBar.setBackground(new Color(230,230,230));
        deliveryScrollBar.setPreferredSize(new Dimension(10, 40));
        deliveryScrollBar.setMinimumSize(new Dimension(10, 40));
        deliveryScrollBar.setMaximumSize(new Dimension(10, 2147483647));
        deliveryScrollBar.setBorder(BorderFactory.createEmptyBorder());

        stockScrollPane.setVerticalScrollBar(stockScrollBar);
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
        stockTable.setDefaultEditor(Object.class, null);

        deliveryTable.setBackground(new Color(255, 255, 255));
        deliveryTable.setForeground(new Color(75,75,75));
        deliveryTable.setSelectionBackground(new Color(0,125,255));
        deliveryTable.setSelectionForeground(new Color(255,255,255));
        deliveryTable.setGridColor(new Color(230,230,230));
        deliveryTable.getTableHeader().setPreferredSize(new Dimension(deliveryTable.getTableHeader().getWidth(), 30));
        deliveryTable.getTableHeader().setReorderingAllowed(false);
        deliveryTable.setRowHeight(30);
        deliveryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        deliveryTable.setDefaultEditor(Object.class, null);

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CustomerArea customerArea = CustomerArea.getInstance();
                customerArea.setVisible(true);
                dispose();
            }
        });

        int delay = 3000;
        ActionListener deliverItems = actionEvent -> {
            deliveryTable.setEnabled(false);
            delivered = true;
            deliveryButton.setText("Request Items");
            deliveryButton.setBackground(new Color(0, 75, 150));
            replenishButton.setBackground(new Color(0,125,255));
        };
        Timer timer = new Timer(delay, deliverItems);

        deliveryButton.addActionListener(actionEvent -> {
            if(!delivered) {
                deliveryButton.setText("Delivering...");
                timer.restart();
            } else {
                JOptionPane.showMessageDialog(null, "There's already a delivery that needs to be processed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        replenishButton.addActionListener(actionEvent -> {
            if(delivered) {
                timer.stop();
                deliveryTable.setEnabled(true);
                delivered = false;
                deliveryButton.setBackground(new Color(0,125,255));
                replenishButton.setBackground(new Color(0, 75, 150));
            } else {
                JOptionPane.showMessageDialog(null, "Please order items for delivery first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {

    }
}
