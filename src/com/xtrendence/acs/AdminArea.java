package com.xtrendence.acs;
import javax.swing.*;
import java.awt.*;

public class AdminArea extends JFrame {
    public AdminArea() {
        this.setSize(700, 300);
        this.setLocation(150, 150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        AdminArea adminArea = new AdminArea();
        adminArea.setVisible(true);
    }
}
