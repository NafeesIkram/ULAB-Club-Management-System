package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class AdminMemberProgress extends JPanel {

    private final DefaultListModel<String> model;

    public AdminMemberProgress(MainFrame frame) {
        setLayout(null);

        // Left fixed admin panel
        add(new AdminLeftPanel(frame));

        // Page Title (same as AdminDashboard style)
        JLabel title = new JLabel("Members Progress (Points)");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(250, 30, 400, 30);
        add(title);

        // Back button (right side)
        JButton backBtn = new JButton("Back");
        backBtn.setBounds(760, 25, 100, 30);
        add(backBtn);
        backBtn.addActionListener(e -> frame.showPage(MainFrame.ADMIN_DASH));

        // Member progress list
        model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);

        JScrollPane sp = new JScrollPane(list);
        sp.setBounds(250, 90, 610, 400);
        add(sp);

        // Refresh button
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(490, 510, 120, 30);
        add(refreshBtn);
        refreshBtn.addActionListener(e -> loadProgress(frame));

        loadProgress(frame);
    }

    private void loadProgress(MainFrame frame) {
        model.clear();

        User cur = frame.getCurrentUser();
        if (cur == null) {
            model.addElement("No user logged in.");
            return;
        }

        // show only points for members visible to this admin
        List<String> visibleMembers = DataStore.listMembersForAdmin(cur.getUsername());
        if (visibleMembers == null || visibleMembers.isEmpty()) {
            model.addElement("No members found.");
            return;
        }

        for (String username : visibleMembers) {
            Integer pts = DataStore.getMemberPoints(username);
            if (pts == null) pts = 0;
            model.addElement(username + " → " + pts + " pts");
        }
    }
}



