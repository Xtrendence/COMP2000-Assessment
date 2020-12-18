package com.xtrendence.acs;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CustomerArea extends JFrame {
    private JPanel mainPanel;
    private JPanel navbar;
    private JPanel contentWrapper;
    private JPanel listWrapper;
    private JPanel actionsWrapper;
    private JLabel basketButton;
    private JLabel adminButton;

    public CustomerArea() throws IOException {
        this.setSize(1280, 720);
        this.setLocation(150, 150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Customer Area");

        navbar.setBackground(new Color(0, 125, 255));
        navbar.setSize(navbar.getWidth(), 60);

        BufferedImage basketIcon = ImageIO.read(new File(System.getProperty("user.dir") + "\\resources\\basket.png"));
        basketButton.setIcon(new ImageIcon(basketIcon.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));

        BufferedImage userIcon = ImageIO.read(new File(System.getProperty("user.dir") + "\\resources\\user.png"));
        adminButton.setIcon(new ImageIcon(userIcon.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
    }

    public static void main(String[] args) throws IOException {
        CustomerArea customerArea = new CustomerArea();
        customerArea.setVisible(true);
        customerArea.setContentPane(customerArea.mainPanel);

        Stock stock = new Stock();
        stock.updateStock();
    }
}
