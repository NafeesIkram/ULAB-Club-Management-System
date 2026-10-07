package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MemberViewClubs extends JPanel {
    private JList<String> clubList;
    private DefaultListModel<String> listModel;
    private JCheckBox showJoinedOnly;

    public MemberViewClubs(MainFrame frame) {
        setLayout(null);

        MemberLeftPanel left = new MemberLeftPanel(frame);
        add(left);

        JLabel title = new JLabel("View Clubs");
        title.setBounds(250, 30, 400, 30);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        add(title);

        listModel = new DefaultListModel<>();
        clubList = new JList<>(listModel);
        JScrollPane sp = new JScrollPane(clubList);
        sp.setBounds(250, 100, 610, 360);
        add(sp);

        JButton join = new JButton("Join");
        join.setBounds(250, 480, 120, 30);
        add(join);

        JButton leave = new JButton("Leave");
        leave.setBounds(400, 480, 120, 30);
        add(leave);

        JButton refresh = new JButton("Refresh");
        refresh.setBounds(740, 480, 120, 30);
        add(refresh);

        showJoinedOnly = new JCheckBox("Show joined only");
        showJoinedOnly.setBounds(250, 70, 160, 25);
        add(showJoinedOnly);

        JButton back = new JButton("Back");
        back.setBounds(760, 25, 100, 30);
        back.addActionListener(e -> frame.showPage(MainFrame.MEMBER_DASH));
        add(back);

        refresh.addActionListener(e -> loadClubs(frame.getCurrentUser()));
        showJoinedOnly.addActionListener(e -> loadClubs(frame.getCurrentUser()));

        join.addActionListener(e -> {
            User u = frame.getCurrentUser();
            if (u == null) {
                JOptionPane.showMessageDialog(this, "Login first");
                frame.showPage(MainFrame.LOGIN);
                return;
            }
            int idx = clubList.getSelectedIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(this, "Select a club");
                return;
            }
            List<Club> clubs = DataStore.listClubs();
            Club c = clubs.get(idx);

            boolean ok = DataStore.requestJoinClub(u.getUsername(), c.getId());
            JOptionPane.showMessageDialog(this, ok ? "Join request submitted. Wait for admin approval." : "Already a member or request exists.");
            loadClubs(frame.getCurrentUser());
        });

        leave.addActionListener(e -> {
            User u = frame.getCurrentUser();
            if (u == null) {
                JOptionPane.showMessageDialog(this, "Login first");
                frame.showPage(MainFrame.LOGIN);
                return;
            }
            int idx = clubList.getSelectedIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(this, "Select a club");
                return;
            }
            List<Club> clubs = DataStore.listClubs();
            Club c = clubs.get(idx);
            boolean ok = DataStore.leaveClub(u.getUsername(), c.getId());
            JOptionPane.showMessageDialog(this, ok ? "Left club" : "Error");
            loadClubs(frame.getCurrentUser());
        });

        loadClubs(frame.getCurrentUser());
    }

    private void loadClubs(User currentUser) {
        listModel.clear();
        List<Club> clubs = DataStore.listClubs();
        List<String> joined = currentUser == null ? List.of() : DataStore.listMemberships(currentUser.getUsername());

        for (Club c : clubs) {
            boolean isJoined = joined.contains(c.getId());
            String label = c.getName() + " - " + c.getDescription() + (isJoined ? " (Joined)" : "");
            if (showJoinedOnly.isSelected()) {
                if (isJoined) listModel.addElement(label);
            } else {
                listModel.addElement(label);
            }
        }

        if (clubs.isEmpty()) listModel.addElement("No clubs available.");
    }
}





