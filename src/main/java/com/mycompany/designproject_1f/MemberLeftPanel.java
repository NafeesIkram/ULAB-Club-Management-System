package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;

public class MemberLeftPanel extends BaseLeftPanel {
private String username = "nafees";

    public MemberLeftPanel(MainFrame frame) {
        super(frame, new Color(30, 90, 100)); // Member color
 
        nameLabel.setText("Member: " + username);
        buildMenu(frame);
    }

    @Override
    protected void buildMenu(MainFrame frame) {

        int y = startY;

        JButton dash = createButton("Dashboard", y, new Color(50, 120, 130)); y += height + 10;
        JButton viewClubs = createButton("View Clubs", y, new Color(50, 120, 130)); y += height + 10;
        JButton events = createButton("Upcoming Events", y, new Color(50, 120, 130)); y += height + 10;
        JButton progress = createButton("Progress", y, new Color(50, 120, 130)); y += height + 10;
        JButton notification = createButton("Notifications", y, new Color(50, 120, 130));

        add(dash); add(viewClubs); add(events);
        add(progress); add(notification);

        dash.addActionListener(e -> frame.showPage(MainFrame.MEMBER_DASH));
        viewClubs.addActionListener(e -> frame.showPage(MainFrame.MEMBER_VIEW));
        events.addActionListener(e -> frame.showPage(MainFrame.MEMBER_EVENTS));
        progress.addActionListener(e -> frame.showPage(MainFrame.MEMBER_PROGRESS));
        notification.addActionListener(e -> frame.showPage(MainFrame.MEMBER_NOTIFICATION));
    }
}


