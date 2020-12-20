package com.xtrendence.acs.customers;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class CustomerAreaStyling {
    private CustomerArea customerArea;

    public CustomerAreaStyling(CustomerArea customerArea) {
        this.customerArea = customerArea;
    }

    public void applyStyle() {
        customerArea.navbar.setBackground(new Color(0, 125, 255));
        customerArea.navbar.setSize(customerArea.navbar.getWidth(), 60);

        customerArea.mainPanel.setBackground(new Color(235,235,235));
        customerArea.contentWrapper.setBackground(new Color(235,235,235));
        customerArea.actionsWrapper.setBackground(new Color(235,235,235));

        try {
            String separator = System.getProperty("file.separator");
            BufferedImage userIcon = ImageIO.read(new File(System.getProperty("user.dir") + separator + "resources" + separator + "user.png"));
            customerArea.adminButton.setIcon(new ImageIcon(userIcon.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        } catch(Exception e) {
            System.out.println(e);
        }

        customerArea.companyTitle.setFont(customerArea.itemTableTitle.getFont().deriveFont(Font.BOLD, 20));
        customerArea.companyTitle.setForeground(new Color(255,255,255));

        customerArea.itemTableScrollPane.getViewport().setBackground(new Color(255, 255, 255));
        customerArea.scannedTableScrollPane.getViewport().setBackground(new Color(255, 255, 255));

        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

        StyledDocument itemTableTitleText = customerArea.itemTableTitle.getStyledDocument();
        itemTableTitleText.setParagraphAttributes(0, itemTableTitleText.getLength(), center, false);
        customerArea.itemTableTitle.setFont(customerArea.itemTableTitle.getFont().deriveFont(Font.BOLD, 16));
        customerArea.itemTableTitle.setBackground(new Color(150,135,255));
        customerArea.itemTableTitle.setForeground(new Color(255,255,255));
        customerArea.itemTableTitle.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        StyledDocument scannedTableTitleText = customerArea.scannedTableTitle.getStyledDocument();
        scannedTableTitleText.setParagraphAttributes(0, scannedTableTitleText.getLength(), center, false);
        customerArea.scannedTableTitle.setFont(customerArea.scannedTableTitle.getFont().deriveFont(Font.BOLD, 16));
        customerArea.scannedTableTitle.setBackground(new Color(150,135,255));
        customerArea.scannedTableTitle.setForeground(new Color(255,255,255));
        customerArea.scannedTableTitle.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        StyledDocument scannedTotalText = customerArea.scannedTotal.getStyledDocument();
        scannedTotalText.setParagraphAttributes(0, scannedTotalText.getLength(), center, false);
        customerArea.scannedTotal.setFont(customerArea.scannedTotal.getFont().deriveFont(Font.BOLD, 16));
        customerArea.scannedTotal.setBackground(new Color(150,135,255));
        customerArea.scannedTotal.setForeground(new Color(255,255,255));
        customerArea.scannedTotal.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        customerArea.checkoutButton.setOpaque(true);
        customerArea.checkoutButton.setBackground(new Color(0,125,255));
        customerArea.checkoutButton.setForeground(new Color(255,255,255));

        JScrollBar itemScrollBar = new JScrollBar();
        itemScrollBar.setBackground(new Color(230,230,230));
        itemScrollBar.setPreferredSize(new Dimension(10, 40));
        itemScrollBar.setMinimumSize(new Dimension(10, 40));
        itemScrollBar.setMaximumSize(new Dimension(10, 2147483647));
        itemScrollBar.setBorder(BorderFactory.createEmptyBorder());

        JScrollBar scannedScrollBar = new JScrollBar();
        scannedScrollBar.setBackground(new Color(230,230,230));
        scannedScrollBar.setPreferredSize(new Dimension(10, 40));
        scannedScrollBar.setMinimumSize(new Dimension(10, 40));
        scannedScrollBar.setMaximumSize(new Dimension(10, 2147483647));
        scannedScrollBar.setBorder(BorderFactory.createEmptyBorder());

        customerArea.itemTableScrollPane.setVerticalScrollBar(itemScrollBar);
        customerArea.scannedTableScrollPane.setVerticalScrollBar(scannedScrollBar);

        customerArea.itemTable.setBackground(new Color(255, 255, 255));
        customerArea.itemTable.setForeground(new Color(75,75,75));
        customerArea.itemTable.setSelectionBackground(new Color(0,125,255));
        customerArea.itemTable.setSelectionForeground(new Color(255,255,255));
        customerArea.itemTable.setGridColor(new Color(230,230,230));
        customerArea.itemTable.getTableHeader().setPreferredSize(new Dimension(customerArea.itemTable.getTableHeader().getWidth(), 30));
        customerArea.itemTable.getTableHeader().setReorderingAllowed(false);
        customerArea.itemTable.setRowHeight(30);
        customerArea.itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerArea.itemTable.setDefaultEditor(Object.class, null);

        customerArea.scannedTable.setBackground(new Color(255, 255, 255));
        customerArea.scannedTable.setForeground(new Color(75,75,75));
        customerArea.scannedTable.setSelectionBackground(new Color(0,125,255));
        customerArea.scannedTable.setSelectionForeground(new Color(255,255,255));
        customerArea.scannedTable.setGridColor(new Color(230,230,230));
        customerArea.scannedTable.getTableHeader().setPreferredSize(new Dimension(customerArea.scannedTable.getTableHeader().getWidth(), 30));
        customerArea.scannedTable.setRowHeight(30);
        customerArea.scannedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerArea.scannedTable.setDefaultEditor(Object.class, null);
        customerArea.scannedTable.getTableHeader().setReorderingAllowed(false);

        customerArea.scanOutput.setVisible(false);
        customerArea.scanOutput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        customerArea.scanButton.setOpaque(true);
        customerArea.scanButton.setBackground(new Color(0,125,255));
        customerArea.scanButton.setForeground(new Color(255,255,255));

        customerArea.inputProductCode.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        customerArea.inputProductCode.setHorizontalAlignment(JTextField.CENTER);
    }
}
