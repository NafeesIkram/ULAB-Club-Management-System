package com.mycompany.designproject_1f;

import javax.swing.*;

public class AdminEventManagement extends EventManagementBase {

    public AdminEventManagement(MainFrame frame) {
        super(frame);
    }
    @Override
    protected JPanel createLeftPanel() {
        return new AdminLeftPanel(frame);
    }
    @Override
    protected void onBack() {
        frame.showPage(MainFrame.ADMIN_DASH);
    }
    @Override
    protected boolean canModify() {
        return true; // Admin always allowed
    }
    @Override
    protected String getPageTitle() {
        return "Event Management (Admin)";
    }
}










