package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;

public class ResetPasswordPage extends JPanel {

    public ResetPasswordPage(MainFrame frame) {
             setLayout(null);
        setBackground(new Color(255, 255, 255));

        JLabel title = new JLabel("Reset Password");
        title.setBounds(340, 40, 300, 30);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title);

        JLabel userLbl = new JLabel("Username:");
        userLbl.setBounds(280, 140, 100, 25);
        add(userLbl);
        JTextField userField = new JTextField();
        userField.setBounds(380, 140, 200, 25);
        add(userField);

        JLabel newLbl = new JLabel("New Password:");
        newLbl.setBounds(280, 180, 100, 25);
        add(newLbl);
        JPasswordField newField = new JPasswordField();
        newField.setBounds(380, 180, 200, 25);
        add(newField);

        JButton submit = new JButton("Submit");
        submit.setBounds(380, 220, 120, 30);
        add(submit);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(20, 20, 80, 30);
        add(backBtn);

        backBtn.addActionListener(e -> frame.showPage(MainFrame.LOGIN));

        submit.addActionListener(e -> {
            String username = userField.getText().trim();
            String newPass = new String(newField.getPassword());

            if (username.isEmpty() || newPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            // Check if user exists
            var userOpt = DataStore.findUser(username);
            if (userOpt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "User not found!");
                return;
            }

            // email verification
            String emailInput = JOptionPane.showInputDialog(this, "Enter your registered email:");
            if (emailInput == null) return; 

            var foundUser = userOpt.get();
            if (!foundUser.getEmail().equalsIgnoreCase(emailInput.trim())) {
                JOptionPane.showMessageDialog(this, "Email does not match this username!");
                return;
            }

            // Reset password
            boolean ok = DataStore.resetPassword(username, newPass);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Password changed successfully!");
                frame.showPage(MainFrame.LOGIN);
            } else {
                JOptionPane.showMessageDialog(this, "Error updating password!");
            }
        });
    }
}

