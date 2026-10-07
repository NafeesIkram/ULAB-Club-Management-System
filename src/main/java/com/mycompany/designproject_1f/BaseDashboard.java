package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;

public abstract class BaseDashboard extends JPanel {

    protected JLabel titleLabel;
    protected JLabel welcomeLabel;

    public BaseDashboard(MainFrame frame, String title, String welcomeText) {
        setLayout(null);

        //Title
        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setBounds(250, 30, 400, 30);
        add(titleLabel);

        // Welcome Text
        welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomeLabel.setBounds(250, 100, 500, 30);
        add(welcomeLabel);
    }

    // Child class will implement this
    protected abstract JPanel createLeftPanel(MainFrame frame);
}

