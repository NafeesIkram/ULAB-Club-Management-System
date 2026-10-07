package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JPanel {
    public LoginPage(MainFrame frame) {
        setLayout(null);
        setBackground(new Color(255, 255, 255)); // White background

        // Logo on top
        JLabel profilePic = new JLabel(new ImageIcon("a.jpg"));
        profilePic.setBounds(10, 20, 180, 80);
        add(profilePic);

        // Main title
        JLabel title1 = new JLabel("Welcome to Ulab Club Management System");
        title1.setFont(new Font("Arial", Font.BOLD, 24));
        title1.setBounds(200, 100, 600, 30);
        add(title1);

        // Login title
        JLabel title = new JLabel("Login");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(430, 150, 200, 30);
        add(title);

        // Username
        JLabel userLbl = new JLabel("Username:");
        userLbl.setBounds(260, 210, 100, 25);
        add(userLbl);

        JTextField userField = new JTextField();
        userField.setBounds(360, 210, 220, 25);
        add(userField);

        // Password
        JLabel passLbl = new JLabel("Password:");
        passLbl.setBounds(260, 250, 100, 25);
        add(passLbl);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(360, 250, 220, 25);
        add(passField);

        // Buttons
        JButton forgetBtn = new JButton("Forget Password");
        forgetBtn.setBounds(360, 290, 120, 30);
        add(forgetBtn);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(460, 290, 120, 30);
        add(registerBtn);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(360, 330, 220, 30);
        add(loginBtn);

        registerBtn.addActionListener(e -> frame.showPage(MainFrame.REGISTER));
        forgetBtn.addActionListener(e -> frame.showPage(MainFrame.RESET));

        loginBtn.addActionListener(e -> {
            String u = userField.getText().trim();
            String p = new String(passField.getPassword());

            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter username and password");
                return;
            }

            var opt = DataStore.findUser(u);
            if (opt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "User not found");
                return;
            }

            var user = opt.get();
            if (!user.getPassword().equals(p)) {
                JOptionPane.showMessageDialog(this, "Wrong password");
                return;
            }

            // Login success
            frame.setCurrentUser(user);

            JOptionPane.showMessageDialog(this,
                    "Welcome " + user.getUsername() + " (" + user.getRole() + ")");

            // FIXED ROLE ROUTING
            if (user.isAdmin()) {
                frame.showPage(MainFrame.ADMIN_DASH);
            } else if (user.isPresident()) {
                frame.showPage(MainFrame.PRESIDENT_DASH);
            } else {
                frame.showPage(MainFrame.MEMBER_DASH);
            }
        });
    }
}


