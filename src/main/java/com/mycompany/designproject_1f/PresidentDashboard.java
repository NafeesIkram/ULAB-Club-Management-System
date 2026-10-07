package com.mycompany.designproject_1f;

import javax.swing.*;

public class PresidentDashboard extends BaseDashboard {
    public PresidentDashboard(MainFrame frame) {
        super(frame, 
              "President Dashboard", 
              "Welcome ULAB Computer Programming Club President");

        JPanel left = new PresidentLeftPanel(frame);
        add(left);
    }
    @Override
    protected JPanel createLeftPanel(MainFrame frame) {
        return new PresidentLeftPanel(frame);
    }
}


