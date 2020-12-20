package com.xtrendence.acs.admins;
import com.xtrendence.acs.customers.CustomerArea;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class AdminArea extends JFrame {
    public JPanel navbar;
    public JPanel mainPanel;
    public JPanel contentWrapper;
    public JLabel backButton;
    public JLabel companyTitle;

    public AdminArea() {
        String separator = System.getProperty("file.separator");
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + separator + "resources" + separator + "acs.png").getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH));
        this.setSize(1280, 720);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("X Mart - Admin Area");

        navbar.setBackground(new Color(0, 125, 255));
        navbar.setSize(navbar.getWidth(), 60);

        try {
            BufferedImage userIcon = ImageIO.read(new File(System.getProperty("user.dir") + separator + "resources" + separator + "back.png"));
            backButton.setIcon(new ImageIcon(userIcon.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        } catch(Exception e) {
            System.out.println(e);
        }

        companyTitle.setFont(companyTitle.getFont().deriveFont(Font.BOLD, 20));
        companyTitle.setForeground(new Color(255,255,255));

        mainPanel.setBackground(new Color(235,235,235));

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CustomerArea customerArea = CustomerArea.getInstance();
                customerArea.setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {

    }
}
