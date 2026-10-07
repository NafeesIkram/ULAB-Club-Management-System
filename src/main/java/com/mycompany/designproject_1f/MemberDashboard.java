package com.mycompany.designproject_1f;

import javax.swing.*;

public class MemberDashboard extends BaseDashboard {

    public MemberDashboard(MainFrame frame) {
        super(frame, 
              "Member Dashboard", 
              "Welcome to the ULAB Club Manegment System");
        JPanel left = new MemberLeftPanel(frame);
        add(left);
    }
    @Override
    protected JPanel createLeftPanel(MainFrame frame) {
        return new MemberLeftPanel(frame);
    }
}



