package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;

public class PresidentLeftPanel extends BaseLeftPanel {

    public PresidentLeftPanel(MainFrame frame) {
        super(frame, new Color(0, 110, 65));  // President color
        nameLabel.setText("President");
        buildMenu(frame);
    }
    @Override
    protected void buildMenu(MainFrame frame) {
        int y = startY;
        JButton dash = createButton("Dashboard", y, new Color(0, 140, 80)); y += height + 10;
        JButton members = createButton("View Members", y, new Color(0, 140, 80)); y += height + 10;
        JButton approval = createButton("Approval", y, new Color(0, 140, 80)); y += height + 10;
        JButton events = createButton("Events", y, new Color(0, 140, 80)); y += height + 10;
        JButton notification = createButton("Notifications", y, new Color(0, 140, 80)); y += height + 10;
        JButton progress = createButton("Progress", y, new Color(0, 140, 80));

        add(dash); add(members); add(approval); add(events);
        add(notification); add(progress);
        dash.addActionListener(e -> frame.showPage(MainFrame.PRESIDENT_DASH));
        members.addActionListener(e -> frame.showPage(MainFrame.PRESIDENT_VIEW_MEMBERS));
        approval.addActionListener(e -> frame.showPage(MainFrame.PRESIDENT_APPROVAL));
        events.addActionListener(e -> frame.showPage(MainFrame.PRESIDENT_EVENTS));
        notification.addActionListener(e -> frame.showPage(MainFrame.PRESIDENT_NOTIFICATION));
        progress.addActionListener(e -> frame.showPage(MainFrame.PRESIDENT_MEMBER_PROGRESS));
    }
}



