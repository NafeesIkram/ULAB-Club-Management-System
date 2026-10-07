package com.mycompany.designproject_1f;

import javax.swing.*;

public class PresidentEventManagement extends EventManagementBase {

    public PresidentEventManagement(MainFrame frame) {
        super(frame);
    }
    @Override
    protected JPanel createLeftPanel() {
        return new PresidentLeftPanel(frame);
    }
    @Override
    protected void onBack() {
        frame.showPage(MainFrame.PRESIDENT_DASH);
    }
    @Override
    protected boolean canModify() {
        User cur = frame.getCurrentUser();
        return cur != null && cur.isPresident();
    }
    @Override
    protected String getPageTitle() {
        return "Event Management (President)";
    }
}


