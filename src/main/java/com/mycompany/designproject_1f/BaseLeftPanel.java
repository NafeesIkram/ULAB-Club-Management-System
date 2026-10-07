package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;

public abstract class BaseLeftPanel extends JPanel {

    protected JLabel profilePic;
    protected JLabel nameLabel;
    protected JButton logoutBtn;

    protected int startY = 160;
    protected int height = 40;

    public BaseLeftPanel(MainFrame frame, Color bgColor) {
        setLayout(null);
        setBackground(bgColor);
        setBounds(0, 0, 200, 650);
        profilePic = new JLabel(new ImageIcon("a.jpg"));
        profilePic.setBounds(10, 20, 180, 80);
        add(profilePic);

        //Name label
        nameLabel = new JLabel();
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setBounds(60, 110, 120, 20);
        add(nameLabel);

        // Set name dynamically
        User user = frame.getCurrentUser();
        nameLabel.setText(user != null ? user.getUsername() : "User");

        //Logout
        logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(25, 550, 150, 40);
        logoutBtn.setBackground(new Color(210, 60, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));

        logoutBtn.addActionListener(e -> {
            frame.setCurrentUser(null);
            frame.showPage(MainFrame.LOGIN);
        });

        add(logoutBtn);
    }

    // Helper for creating menu buttons
    protected JButton createButton(String text, int y, Color color) {
        JButton btn = new JButton(text);
        btn.setBounds(20, y, 160, 40);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        return btn;
    }

    // Child class must implement this to add its menu buttons
    protected abstract void buildMenu(MainFrame frame);
}

