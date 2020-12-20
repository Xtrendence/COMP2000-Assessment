package com.xtrendence.acs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {
    private CustomerArea customerArea;
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

    public LoginDialog(CustomerArea customerArea) {
        this.customerArea = customerArea;
        String separator = System.getProperty("file.separator");
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + separator + "resources" + separator + "acs.png").getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH));
        this.setContentPane(contentPane);
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

        buttonCancel.setOpaque(true);
        buttonCancel.setBackground(new Color(255,255,255));
        buttonCancel.setForeground(new Color(0,100,200));

        buttonLogin.setOpaque(true);
        buttonLogin.setBackground(new Color(255,255,255));
        buttonLogin.setForeground(new Color(0,100,200));

        // TODO: Remove after development.
        inputUsername.setText("Xtrendence");
        inputPassword.setText("passw0rd");

        buttonCancel.addActionListener(e -> onCancel());

        buttonLogin.addActionListener(e -> onLogin());

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

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        dispose();
    }

    private void onLogin() {
        boolean login = Account.login(inputUsername.getText(), inputPassword.getText());
        if(login) {
            customerArea.setVisible(false);
            AdminArea adminArea = new AdminArea();
            adminArea.setVisible(true);
        }
        dispose();
    }

    public static void main(String[] args) { }
}
