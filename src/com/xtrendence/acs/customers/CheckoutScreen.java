package com.xtrendence.acs.customers;

import com.xtrendence.acs.data.Item;
import com.xtrendence.acs.data.Stock;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class CheckoutScreen extends JFrame {
    private boolean payingByCash;
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

    public CheckoutScreen() {
        String separator = System.getProperty("file.separator");
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + separator + "resources" + separator + "acs.png").getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH));
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
        paymentLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        cashButton.setOpaque(true);
        cashButton.setBackground(new Color(0,125,255));
        cashButton.setForeground(new Color(255,255,255));

        cardButton.setOpaque(true);
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

        payButton.setOpaque(true);
        payButton.setBackground(new Color(0,125,255));
        payButton.setForeground(new Color(255,255,255));

        declineButton.setOpaque(true);
        declineButton.setBackground(new Color(200,50,50));
        declineButton.setForeground(new Color(255,255,255));

        approveButton.setOpaque(true);
        approveButton.setBackground(new Color(0,125,255));
        approveButton.setForeground(new Color(255,255,255));

        cashButton.addActionListener(actionEvent -> {
            cashLabel.setVisible(true);
            cardLabel.setVisible(false);
            inputCash.setVisible(true);
            payButton.setVisible(true);
            declineButton.setVisible(false);
            approveButton.setVisible(false);
            this.payingByCash = true;
        });

        cardButton.addActionListener(actionEvent -> {
            cashLabel.setVisible(false);
            cardLabel.setVisible(true);
            inputCash.setVisible(false);
            payButton.setVisible(false);
            declineButton.setVisible(true);
            approveButton.setVisible(true);
            this.payingByCash = false;
        });

        payButton.addActionListener(actionEvent -> {
            float change = 0;
            float cash = 0;
            boolean valid = false;
            String message = "Invalid cash entry.";
            try {
                cash = Float.parseFloat(inputCash.getText());
                if(cash >= Cart.total) {
                    change = cash - Cart.total;
                    message = "You are owed £" + String.format("%.2f", change) + " in change.";
                    valid = true;
                } else {
                    message = "Not enough cash inserted.";
                }
            } catch(Exception e) {
                inputCash.setText("");
            }
            if(valid) {
                JOptionPane.showMessageDialog(null, message, "Successful Payment", JOptionPane.INFORMATION_MESSAGE);
                showReceipt(change);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        declineButton.addActionListener(actionEvent -> {
            CustomerArea.getInstance().setVisible(true);
            dispose();
        });

        approveButton.addActionListener(actionEvent -> {
            showReceipt((float) 0);
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

    public void showReceipt(Float change) {
        ReceiptDisplay receiptDisplay = new ReceiptDisplay();
        receiptDisplay.setVisible(true);

        java.util.List<Item> updatedStock = new ArrayList<>();

        new Thread(() -> {
            SimpleDateFormat today = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
            String dateString = today.format(new Date());
            StringBuilder builder = new StringBuilder();
            builder.append("**************************************\n****  X Mart - Receipt\n****  " + dateString + "\n**************************************\n");
            Iterator iterator = Cart.cart.entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                String code = pair.getKey().toString();
                int quantity = Integer.parseInt(pair.getValue().toString());
                for(Item item : Stock.items) {
                    if(code.equals(item.getCode())) {
                        float price = item.getPrice();
                        builder.append(code + "      " + quantity + " " + item.getName() + "        £" + String.format("%.2f", price * quantity) + "\n");

                        item.setQuantity(item.getQuantity() - quantity);
                    }
                    if(!updatedStock.contains(item)) {
                        updatedStock.add(item);
                    }
                }
                iterator.remove();
            }
            builder.append("\n*******************\nTotal: £" + String.format("%.2f", Cart.total));
            if(this.payingByCash) {
                builder.append("\nChange Due: £" + String.format("%.2f", change));
                builder.append("\nPayment Method: Cash");
            } else {
                builder.append("\nPayment Method: Card");
            }

            Stock.setStock(updatedStock);

            receiptDisplay.printReceipt(builder.toString());
        }).start();
    }
}
