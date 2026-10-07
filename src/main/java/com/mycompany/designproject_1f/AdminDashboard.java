package com.mycompany.designproject_1f;

import javax.swing.*;

public class AdminDashboard extends BaseDashboard {

    public AdminDashboard(MainFrame frame) {
        super(frame, 
              "Admin Dashboard", 
              "Welcome to the Admin Control Panel");

        // Add Admin left panel
        JPanel left = new AdminLeftPanel(frame);
        add(left);
    }

    @Override
    protected JPanel createLeftPanel(MainFrame frame) {
        return new AdminLeftPanel(frame);
    }
}





