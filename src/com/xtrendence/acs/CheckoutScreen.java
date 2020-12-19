package com.xtrendence.acs;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class CheckoutScreen extends JFrame {
    private JButton cashButton;
    private JButton cardButton;
    private JPanel mainPanel;
    private JTextPane paymentLabel;
    private JTextField inputCash;
    private JButton declineButton;
    private JButton approveButton;
    private JLabel cardLabel;
    private JLabel cashLabel;
    private JButton payButton;

    public CheckoutScreen(CustomerArea customerArea) {
        this.setContentPane(mainPanel);
        this.setSize(400, 210);
        this.setLocation(600, 300);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("X Mart - Checkout");

        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

        paymentLabel.setText("Total: £" + String.format("%.2f", Cart.total) + " | Payment Method:");
        StyledDocument paymentLabelText = paymentLabel.getStyledDocument();
        paymentLabelText.setParagraphAttributes(0, paymentLabelText.getLength(), center, false);
        paymentLabel.setFont(paymentLabel.getFont().deriveFont(Font.BOLD, 16));
        paymentLabel.setBackground(new Color(150,135,255));
        paymentLabel.setForeground(new Color(255,255,255));

        cashButton.setBackground(new Color(0,125,255));
        cashButton.setForeground(new Color(255,255,255));

        cardButton.setBackground(new Color(0,125,255));
        cardButton.setForeground(new Color(255,255,255));

        cashLabel.setVisible(false);
        cardLabel.setVisible(false);
        inputCash.setVisible(false);
        payButton.setVisible(false);
        declineButton.setVisible(false);
        approveButton.setVisible(false);

        inputCash.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        inputCash.setHorizontalAlignment(JTextField.CENTER);

        payButton.setBackground(new Color(0,125,255));
        payButton.setForeground(new Color(255,255,255));

        declineButton.setBackground(new Color(200,50,50));
        declineButton.setForeground(new Color(255,255,255));

        approveButton.setBackground(new Color(0,125,255));
        approveButton.setForeground(new Color(255,255,255));

        cashButton.addActionListener(actionEvent -> {
            cashLabel.setVisible(true);
            cardLabel.setVisible(false);
            inputCash.setVisible(true);
            payButton.setVisible(true);
            declineButton.setVisible(false);
            approveButton.setVisible(false);
        });

        cardButton.addActionListener(actionEvent -> {
            cashLabel.setVisible(false);
            cardLabel.setVisible(true);
            inputCash.setVisible(false);
            payButton.setVisible(false);
            declineButton.setVisible(true);
            approveButton.setVisible(true);
        });

        payButton.addActionListener(actionEvent -> {
            customerArea.setVisible(true);
            CustomerArea.loadData(customerArea);
            Cart.emptyCart();
            dispose();
        });

        declineButton.addActionListener(actionEvent -> {
            customerArea.setVisible(true);
            dispose();
        });

        approveButton.addActionListener(actionEvent -> {
            customerArea.setVisible(true);
            CustomerArea.loadData(customerArea);
            Cart.emptyCart();
            dispose();
        });

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                customerArea.setVisible(true);
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
}
