package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PresidentMemberProgress extends JPanel {

    private final DefaultListModel<String> model;
    private final MainFrame frame;

    public PresidentMemberProgress(MainFrame frame) {
        this.frame = frame;
        setLayout(null);
        add(new PresidentLeftPanel(frame));
        JLabel title = new JLabel("Members Progress (Points)");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(250, 30, 400, 30);
        add(title);
        JButton backBtn = new JButton("Back");
        backBtn.setBounds(760, 25, 100, 30);
        add(backBtn);
        backBtn.addActionListener(e -> frame.showPage(MainFrame.PRESIDENT_DASH));
        model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);

        JScrollPane sp = new JScrollPane(list);
        sp.setBounds(250, 90, 610, 400);
        add(sp);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(490, 510, 120, 30);
        add(refreshBtn);
        refreshBtn.addActionListener(e -> loadProgress());
        loadProgress();
    }

    private void loadProgress() {
        model.clear();
        User cur = frame.getCurrentUser();
        if (cur == null) {
            model.addElement("No user logged in.");
            return;
        }

        // show only points for members of this president's club
        String clubId = DataStore.getAdminAssignedClub(cur.getUsername());
        if (clubId == null) {
            model.addElement("No club assigned.");
            return;
        }
        List<String> visibleMembers = DataStore.listMembersOfClub(clubId);
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

