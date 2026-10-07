package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PresidentMemberApproval extends JPanel {

    private DefaultListModel<String> model;
    private JList<String> reqList;
    private final MainFrame frame;

    public PresidentMemberApproval(MainFrame frame) {
        this.frame = frame;
        setLayout(null);

        add(new PresidentLeftPanel(frame));

        JLabel title = new JLabel("Member Approval (President)");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(250, 30, 400, 30);
        add(title);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(760, 25, 100, 30);
        backBtn.addActionListener(e -> frame.showPage(MainFrame.PRESIDENT_DASH));
        add(backBtn);

        model = new DefaultListModel<>();
        reqList = new JList<>(model);
        JScrollPane sp = new JScrollPane(reqList);
        sp.setBounds(250, 100, 610, 360);
        add(sp);

        JButton approve = new JButton("Approve");
        approve.setBounds(250, 480, 120, 30);
        add(approve);

        JButton deny = new JButton("Deny");
        deny.setBounds(400, 480, 120, 30);
        add(deny);

        JButton refresh = new JButton("Refresh");
        refresh.setBounds(740, 480, 120, 30);
        add(refresh);

        refresh.addActionListener(e -> loadRequests());

        approve.addActionListener(e -> {
            int idx = reqList.getSelectedIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(this, "Select a request");
                return;
            }
            List<String[]> rows = getFilteredRequests();
            String[] r = rows.get(idx);
            boolean ok = DataStore.approveJoinRequest(r[0], r[1]);
            JOptionPane.showMessageDialog(this, ok ? "Approved" : "Error");
            loadRequests();
        });

        deny.addActionListener(e -> {
            int idx = reqList.getSelectedIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(this, "Select a request");
                return;
            }
            List<String[]> rows = getFilteredRequests();
            String[] r = rows.get(idx);
            boolean ok = DataStore.denyJoinRequest(r[0], r[1]);
            JOptionPane.showMessageDialog(this, ok ? "Denied" : "Error");
            loadRequests();
        });

        loadRequests();
    }

    private List<String[]> getFilteredRequests() {
        // President should see only requests for their assigned club
        String username = frame.getCurrentUser() == null ? null : frame.getCurrentUser().getUsername();
        String myClubId = DataStore.getAdminAssignedClub(username);
        List<String[]> all = DataStore.listJoinRequests();
        List<String[]> out = new ArrayList<>();
        for (String[] r : all) {
            if (r.length >= 2 && r[1].equals(myClubId)) out.add(r);
        }
        return out;
    }

    private void loadRequests() {
        model.clear();
        List<String[]> rows = getFilteredRequests();

        for (String[] r : rows) {
            String username = r[0];
            String clubId = r[1];
            String clubName = clubId;
            for (Club c : DataStore.listClubs()) {
                if (c.getId().equals(clubId)) {
                    clubName = c.getName();
                    break;
                }
            }
            model.addElement("Member " + username + " → " + clubName + " (requested)");
        }
        if (rows.isEmpty()) model.addElement("No pending join requests.");
    }
}
