package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;

public class AdminLeftPanel extends BaseLeftPanel {

    public AdminLeftPanel(MainFrame frame) {
        super(frame, new Color(42, 50, 75));  // Admin color
        nameLabel.setText("Admin");
        buildMenu(frame);
    }

    @Override
    protected void buildMenu(MainFrame frame) {

        int y = startY;

        JButton dash = createButton("Dashboard", y, new Color(70, 80, 110)); y += height + 10;
        JButton members = createButton("View Members", y, new Color(70, 80, 110)); y += height + 10;
        JButton approval = createButton("Approval", y, new Color(70, 80, 110)); y += height + 10;
        JButton events = createButton("Events", y, new Color(70, 80, 110)); y += height + 10;
        JButton notification = createButton("Notifications", y, new Color(70, 80, 110)); y += height + 10;
        JButton progress = createButton("Progress", y, new Color(70, 80, 110)); y += height + 10;
        JButton data = createButton("Data Viewer", y, new Color(70, 80, 110));

        add(dash); add(members); add(approval); add(events);
        add(notification); add(progress); add(data);

        dash.addActionListener(e -> frame.showPage(MainFrame.ADMIN_DASH));
        members.addActionListener(e -> frame.showPage(MainFrame.ADMIN_VIEW_MEMBERS));
        approval.addActionListener(e -> frame.showPage(MainFrame.ADMIN_APPROVAL));
        events.addActionListener(e -> frame.showPage(MainFrame.ADMIN_EVENTS));
        notification.addActionListener(e -> frame.showPage(MainFrame.ADMIN_NOTIFICATION));
        progress.addActionListener(e -> frame.showPage(MainFrame.ADMIN_MEMBER_PROGRESS));
        data.addActionListener(e -> frame.showPage(MainFrame.ADMIN_DATA));
    }
}






