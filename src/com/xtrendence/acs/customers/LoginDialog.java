package com.xtrendence.acs.customers;

import com.xtrendence.acs.accounts.Account;
import com.xtrendence.acs.admins.AdminArea;
import com.xtrendence.acs.data.Stock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonLogin;
    private JButton buttonCancel;
    private JPanel buttonsWrapper;
    private JPanel contentWrapper;
    private JPanel formWrapper;
    private JTextField inputUsername;
    private JTextField inputPassword;
    private JLabel labelUsername;
    private JLabel labelPassword;

    public LoginDialog() {
        // Depending on the OS, the file separator can be different (usually either / or \).
        String fs = System.getProperty("file.separator");

        // Sets the application icon.
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + fs + "resources" + fs + "acs.png").getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH));

        this.setContentPane(contentPane);

        // When the user clicks off the dialog, it's closed, so it can't be a modal, otherwise the user wouldn't be able to click on another GUI.
        this.setModal(false);

        this.getRootPane().setDefaultButton(buttonLogin);

        contentPane.setBackground(new Color(0, 100, 200));
        contentWrapper.setBackground(new Color(0, 100, 200));
        formWrapper.setBackground(new Color(0, 100, 200));
        buttonsWrapper.setBackground(new Color(0, 100, 200));

        inputUsername.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        inputUsername.setHorizontalAlignment(JTextField.CENTER);
        inputUsername.setBackground(new Color(255,255,255));
        inputUsername.setForeground(new Color(75,75,75));
        labelUsername.setForeground(new Color(255,255,255));

        inputPassword.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        inputPassword.setHorizontalAlignment(JTextField.CENTER);
        inputPassword.setBackground(new Color(255,255,255));
        inputPassword.setForeground(new Color(75,75,75));
        labelPassword.setForeground(new Color(255,255,255));

        // If buttons' opaque values aren't set to true, they don't show up on macOS systems.
        buttonCancel.setOpaque(true);
        buttonCancel.setBackground(new Color(255,255,255));
        buttonCancel.setForeground(new Color(0,100,200));

        buttonLogin.setOpaque(true);
        buttonLogin.setBackground(new Color(255,255,255));
        buttonLogin.setForeground(new Color(0,100,200));

        // TODO: Remove after development.
        inputUsername.setText("Temp");
        inputPassword.setText("t3mp");

        buttonCancel.addActionListener(e -> onCancel());

        buttonLogin.addActionListener(e -> onLogin());

        // The LoginDialog window has an event listener that disposes of itself when it loses focus, so DO_NOTHING_ON_CLOSE is used to ensure the custom event listener is used rather than simply closing the window.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        this.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent windowEvent) { }
            @Override
            public void windowLostFocus(WindowEvent windowEvent) {
                dispose();
            }
        });

        // If the escape key is pressed, the dialog window is closed.
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /* Closes the window.
    *  @return Nothing.
    */
    private void onCancel() {
        dispose();
    }

    /* Creates a new Account object based on the username and password in the JTextField components.
    *  @return Nothing.
    */
    private void onLogin() {
        String username = inputUsername.getText();
        String password = inputPassword.getText();

        Account account = new Account(username, password);

        // Part of the State design pattern. If the Account object's state has a loggedIn() method that returns the boolean value of "true", then the admin must be logged in and the username/password combination must be correct.
        if(account.getState().loggedIn()) {
            Stock.getStock();

            CustomerArea.getInstance().setVisible(false);

            AdminArea adminArea = new AdminArea(account);
            adminArea.setVisible(true);
            adminArea.setContentPane(adminArea.mainPanel);
            adminArea.updateTables();
        }
        dispose();
    }

    public static void main(String[] args) { }
}
