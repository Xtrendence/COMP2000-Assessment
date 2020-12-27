package com.xtrendence.acs.view;

import com.xtrendence.acs.controller.Cart;
import com.xtrendence.acs.model.Item;
import com.xtrendence.acs.controller.Stock;

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
    private Cart cart;
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

    // The customer's shopping cart is passed to the constructor of the CheckoutScreen. The Cart object contains a HashMap with the keys being product codes, and the values being the quantity.
    public CheckoutScreen(Cart cart) {
        // Depending on the OS, the file separator can be different (usually either / or \).
        String fs = System.getProperty("file.separator");

        // Sets the application icon.
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + fs + "resources" + fs + "acs.png").getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH));

        this.setContentPane(mainPanel);
        this.setSize(400, 210);
        this.setLocation(600, 300);

        // The CheckoutScreen window has an event listener that makes the Singleton instance of the CustomerArea visible again, so DO_NOTHING_ON_CLOSE is used to ensure the custom event listener is used rather than simply closing the window.
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.setTitle("X Mart - Checkout");

        this.cart = cart;

        // Used to center content inside JTextPane components.
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

        paymentLabel.setText("Total: £" + String.format("%.2f", cart.getTotal()) + " | Payment Method:");
        StyledDocument paymentLabelText = paymentLabel.getStyledDocument();
        paymentLabelText.setParagraphAttributes(0, paymentLabelText.getLength(), center, false);
        paymentLabel.setFont(paymentLabel.getFont().deriveFont(Font.BOLD, 16));
        paymentLabel.setBackground(new Color(150,135,255));
        paymentLabel.setForeground(new Color(255,255,255));
        paymentLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        // If buttons' opaque values aren't set to true, they don't show up on macOS systems.
        cashButton.setOpaque(true);
        cashButton.setBackground(new Color(0,125,255));
        cashButton.setForeground(new Color(255,255,255));

        cardButton.setOpaque(true);
        cardButton.setBackground(new Color(0,125,255));
        cardButton.setForeground(new Color(255,255,255));

        // By default, neither payment method's relevant components are shown until the customer picks a method.
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

        // If they pick to pay by cash, a JTextField component is made visible for them to enter the amount of cash they'd like to "insert".
        cashButton.addActionListener(actionEvent -> {
            cashLabel.setVisible(true);
            cardLabel.setVisible(false);

            inputCash.setVisible(true);
            payButton.setVisible(true);

            declineButton.setVisible(false);
            approveButton.setVisible(false);

            this.payingByCash = true;
        });

        // If they want to pay by card, the customer is asked to confirm the payment with their bank.
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

                // Ensures the customer actually inserted enough cash to pay for their items.
                if(cash >= cart.getTotal()) {
                    change = cash - cart.getTotal();
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
                CustomerArea.getInstance().setVisible(true); // Part of the Singleton design pattern. Since there is only one instance of the CustomerArea object, the instance has to be fetched before other classes can interact with it.
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

    /* Opens a new GUI form that contains the customer's receipt.
    *  @param change The amount of change owed to the customer.
    *  @return Nothing.
    */
    public void showReceipt(Float change) {
        ReceiptDisplay receiptDisplay = new ReceiptDisplay();
        receiptDisplay.setVisible(true);

        // Since the customer will have already paid for the items by this point, the stock will have to be updated, so a new ArrayList is created to replace the current stock, and write the changes to the stock.json file.
        java.util.List<Item> updatedStock = new ArrayList<>();

        // A new thread is used to process the receipt data, and once the process is complete, the receipt text is sent to the ReceiptDisplay object.
        new Thread(() -> {
            SimpleDateFormat today = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
            String dateString = today.format(new Date());

            StringBuilder builder = new StringBuilder();

            builder.append("**************************************\n****  X Mart - Receipt\n****  " + dateString + "\n**************************************\n");

            // The while loop below loops over the customer's cart, gets each item's product code, checks it against the stock, gets the item's price and name to display in the receipt, and then decrements the item's quantity by however many the customer bought of the item.
            Iterator iterator = cart.getCart().entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                String code = pair.getKey().toString();
                int quantity = Integer.parseInt(pair.getValue().toString());
                for(Item item : Stock.getItems()) {
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

            builder.append("\n*******************\nTotal: £" + String.format("%.2f", cart.getTotal()));

            if(this.payingByCash) {
                builder.append("\nChange Due: £" + String.format("%.2f", change));
                builder.append("\nPayment Method: Cash");
            } else {
                builder.append("\nPayment Method: Card");
            }

            // Updates the store's stock, which also triggers the Observer pattern's observers to update their tables.
            Stock.setStock(updatedStock);

            // The StringBuilder being used to construct the receipt's text is turned into an actual string, and passed to the ReceiptDisplay class' printReceipt() method.
            receiptDisplay.printReceipt(builder.toString());
        }).start();
    }
}
