package com.xtrendence.acs.admins;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class AdminAreaStyling {
    private AdminArea adminArea;

    public AdminAreaStyling(AdminArea adminArea) {
        this.adminArea = adminArea;
    }

    public void applyStyle() {
        adminArea.navbar.setBackground(new Color(0, 125, 255));
        adminArea.navbar.setSize(adminArea.navbar.getWidth(), 60);

        try {
            String separator = System.getProperty("file.separator");
            BufferedImage userIcon = ImageIO.read(new File(System.getProperty("user.dir") + separator + "resources" + separator + "back.png"));
            adminArea.backButton.setIcon(new ImageIcon(userIcon.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        } catch(Exception e) {
            System.out.println(e);
        }

        adminArea.companyTitle.setFont(adminArea.companyTitle.getFont().deriveFont(Font.BOLD, 20));
        adminArea.companyTitle.setForeground(new Color(255,255,255));

        adminArea.mainPanel.setBackground(new Color(235,235,235));
        adminArea.contentWrapper.setBackground(new Color(235,235,235));

        adminArea.stockScrollPane.getViewport().setBackground(new Color(255, 255, 255));
        adminArea.lowStockScrollPane.getViewport().setBackground(new Color(255, 255, 255));
        adminArea.deliveryScrollPane.getViewport().setBackground(new Color(255, 255, 255));

        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

        StyledDocument stockLabelText = adminArea.stockLabel.getStyledDocument();
        stockLabelText.setParagraphAttributes(0, stockLabelText.getLength(), center, false);
        adminArea.stockLabel.setFont(adminArea.stockLabel.getFont().deriveFont(Font.BOLD, 16));
        adminArea.stockLabel.setBackground(new Color(150,135,255));
        adminArea.stockLabel.setForeground(new Color(255,255,255));
        adminArea.stockLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        StyledDocument lowStockLabelText = adminArea.lowStockLabel.getStyledDocument();
        lowStockLabelText.setParagraphAttributes(0, lowStockLabelText.getLength(), center, false);
        adminArea.lowStockLabel.setFont(adminArea.lowStockLabel.getFont().deriveFont(Font.BOLD, 16));
        adminArea.lowStockLabel.setBackground(new Color(150,135,255));
        adminArea.lowStockLabel.setForeground(new Color(255,255,255));
        adminArea.lowStockLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        StyledDocument deliveryLabelText = adminArea.deliveryLabel.getStyledDocument();
        deliveryLabelText.setParagraphAttributes(0, deliveryLabelText.getLength(), center, false);
        adminArea.deliveryLabel.setFont(adminArea.deliveryLabel.getFont().deriveFont(Font.BOLD, 16));
        adminArea.deliveryLabel.setBackground(new Color(150,135,255));
        adminArea.deliveryLabel.setForeground(new Color(255,255,255));
        adminArea.deliveryLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        adminArea.removeButton.setOpaque(true);
        adminArea.removeButton.setBackground(new Color(200,50,50));
        adminArea.removeButton.setForeground(new Color(255,255,255));

        adminArea.addButton.setOpaque(true);
        adminArea.addButton.setBackground(new Color(0,125,255));
        adminArea.addButton.setForeground(new Color(255,255,255));

        adminArea.saveButton.setOpaque(true);
        adminArea.saveButton.setBackground(new Color(0,125,255));
        adminArea.saveButton.setForeground(new Color(255,255,255));

        adminArea.deliveryButton.setOpaque(true);
        adminArea.deliveryButton.setBackground(new Color(0,125,255));
        adminArea.deliveryButton.setForeground(new Color(255,255,255));

        adminArea.replenishButton.setOpaque(true);
        adminArea.replenishButton.setBackground(new Color(0, 75, 150));
        adminArea.replenishButton.setForeground(new Color(255,255,255));

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

        adminArea.stockScrollPane.setVerticalScrollBar(stockScrollBar);
        adminArea.lowStockScrollPane.setVerticalScrollBar(lowStockScrollBar);
        adminArea.deliveryScrollPane.setVerticalScrollBar(deliveryScrollBar);

        adminArea.stockTable.setBackground(new Color(255, 255, 255));
        adminArea.stockTable.setForeground(new Color(75,75,75));
        adminArea.stockTable.setSelectionBackground(new Color(0,125,255));
        adminArea.stockTable.setSelectionForeground(new Color(255,255,255));
        adminArea.stockTable.setGridColor(new Color(230,230,230));
        adminArea.stockTable.getTableHeader().setPreferredSize(new Dimension(adminArea.stockTable.getTableHeader().getWidth(), 30));
        adminArea.stockTable.getTableHeader().setReorderingAllowed(false);
        adminArea.stockTable.setRowHeight(30);
        adminArea.stockTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        adminArea.lowStockTable.setBackground(new Color(255, 255, 255));
        adminArea.lowStockTable.setForeground(new Color(75,75,75));
        adminArea.lowStockTable.setSelectionBackground(new Color(0,125,255));
        adminArea.lowStockTable.setSelectionForeground(new Color(255,255,255));
        adminArea.lowStockTable.setGridColor(new Color(230,230,230));
        adminArea.lowStockTable.getTableHeader().setPreferredSize(new Dimension(adminArea.lowStockTable.getTableHeader().getWidth(), 30));
        adminArea.lowStockTable.getTableHeader().setReorderingAllowed(false);
        adminArea.lowStockTable.setRowHeight(30);
        adminArea.lowStockTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        adminArea.lowStockTable.setDefaultEditor(Object.class, null);

        adminArea.deliveryTable.setBackground(new Color(255, 255, 255));
        adminArea.deliveryTable.setForeground(new Color(75,75,75));
        adminArea.deliveryTable.setSelectionBackground(new Color(0,125,255));
        adminArea.deliveryTable.setSelectionForeground(new Color(255,255,255));
        adminArea.deliveryTable.setGridColor(new Color(230,230,230));
        adminArea.deliveryTable.getTableHeader().setPreferredSize(new Dimension(adminArea.deliveryTable.getTableHeader().getWidth(), 30));
        adminArea.deliveryTable.getTableHeader().setReorderingAllowed(false);
        adminArea.deliveryTable.setRowHeight(30);
        adminArea.deliveryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
