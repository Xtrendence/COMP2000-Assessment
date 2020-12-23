package com.xtrendence.acs.customers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ReceiptDisplay extends JFrame {
    private JPanel mainPanel;
    private JTextPane receiptText;
    private JButton closeButton;

    public ReceiptDisplay() {
        String separator = System.getProperty("file.separator");
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + separator + "resources" + separator + "acs.png").getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH));
        this.setContentPane(mainPanel);
        this.setSize(400, 400);
        this.setLocation(600, 300);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("X Mart - Receipt");
        this.setUndecorated(true);

        receiptText.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        closeButton.setOpaque(true);
        closeButton.setBackground(new Color(0,125,255));
        closeButton.setForeground(new Color(255,255,255));

        closeButton.addActionListener(actionEvent -> {
            CustomerArea.getInstance().setVisible(true);
            dispose();
        });

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                CustomerArea.getInstance().setVisible(true);
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

    public void printReceipt(String text) {
        receiptText.setText(text);
    }
}
