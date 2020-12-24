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
        // Depending on the OS, the file separator can be different (usually either / or \).
        String fs = System.getProperty("file.separator");

        // Sets the application icon.
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + fs + "resources" + fs + "acs.png").getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH));

        this.setContentPane(mainPanel);
        this.setSize(400, 400);
        this.setLocation(600, 300);

        // The ReceiptDisplay window has an event listener that makes the CustomerArea visible again, so DO_NOTHING_ON_CLOSE is used to ensure the custom event listener is used rather than simply closing the window.
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.setTitle("X Mart - Receipt");
        this.setUndecorated(true);

        receiptText.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        // If buttons' opaque values aren't set to true, they don't show up on macOS systems.
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

    /* Sets the JTextPane component's text. This is called in a separate thread in the CheckoutScreen class.
    *  @param text Text to display in the receipt.
    *  @return Nothing.
    */
    public void printReceipt(String text) {
        receiptText.setText(text);
    }
}
