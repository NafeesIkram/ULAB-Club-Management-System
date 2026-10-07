package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;

public class RegisterPage extends JPanel {
    public RegisterPage(MainFrame frame) {
        setLayout(null);
        setBackground(new Color(255, 255, 255));

        JLabel title = new JLabel("Register");
        title.setBounds(350, 30, 200, 30);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title);

        JLabel userLbl = new JLabel("Username:");
        userLbl.setBounds(260, 120, 100, 25);
        add(userLbl);
        JTextField userField = new JTextField();
        userField.setBounds(360, 120, 220, 25);
        add(userField);

        JLabel emailLbl = new JLabel("Email:");
        emailLbl.setBounds(260, 160, 100, 25);
        add(emailLbl);
        JTextField emailField = new JTextField();
        emailField.setBounds(360, 160, 220, 25);
        add(emailField);

        JLabel passLbl = new JLabel("Password:");
        passLbl.setBounds(260, 200, 100, 25);
        add(passLbl);
        JPasswordField passField = new JPasswordField();
        passField.setBounds(360, 200, 220, 25);
        add(passField);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(360, 250, 120, 30);
        add(registerBtn);

        JButton back = new JButton("Back");
        back.setBounds(20, 20, 80, 30);
        add(back);
        back.addActionListener(e -> {
    User u = frame.getCurrentUser();
    if (u != null) {
        if (u.isAdmin()) frame.showPage(MainFrame.ADMIN_DASH);
        else frame.showPage(MainFrame.MEMBER_DASH);
    } else {
        frame.showPage(MainFrame.LOGIN);
    }
});

        registerBtn.addActionListener(e -> {
            String u = userField.getText().trim();
            String em = emailField.getText().trim();
            String pw = new String(passField.getPassword());
            if (u.isEmpty() || pw.isEmpty()) { JOptionPane.showMessageDialog(this, "Fill username and password"); return; }
            if (DataStore.findUser(u).isPresent()) { JOptionPane.showMessageDialog(this, "User already exists"); return; }
            DataStore.saveUser(u, pw, em, "MEMBER");
            JOptionPane.showMessageDialog(this, "Registered. You can login now.");
            frame.showPage(MainFrame.LOGIN);
        });
    }
}

