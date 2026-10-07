package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PresidentViewMembers extends JPanel {

    private DefaultListModel<String> model;
    private JList<String> userList;
    private static final String PRESIDENT_CLUB_ID = "1";

    public PresidentViewMembers(MainFrame frame) {
        setLayout(null);
        add(new PresidentLeftPanel(frame));

        JLabel title = new JLabel("View Club Members");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(250, 30, 350, 30);
        add(title);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(760, 25, 100, 30);
        add(backBtn);
        backBtn.addActionListener(e -> frame.showPage(MainFrame.PRESIDENT_DASH));

        model = new DefaultListModel<>();
        userList = new JList<>(model);
        JScrollPane sp = new JScrollPane(userList);
        sp.setBounds(250, 90, 610, 400);
        add(sp);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(740, 510, 120, 30);
        add(refreshBtn);
        refreshBtn.addActionListener(e -> loadUsers());

        JButton removeBtn = new JButton("Remove Member");
        removeBtn.setBounds(600, 510, 130, 30);
        add(removeBtn);

        removeBtn.addActionListener(e -> {
            String selected = userList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Please select a member first!");
                return;
            }

            String username = selected.split(" ")[0];

            if (username.equals(frame.getCurrentUser().getUsername())) {
                JOptionPane.showMessageDialog(this, "You cannot remove yourself!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Remove: " + username + " from club?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                if (DataStore.leaveClub(username, PRESIDENT_CLUB_ID)) {
                    JOptionPane.showMessageDialog(this, "Member removed successfully.");
                    loadUsers();
                } else {
                    JOptionPane.showMessageDialog(this, "Error removing member.");
                }
            }
        });

        loadUsers();
    }

    private void loadUsers() {
        model.clear();

        List<String> members = DataStore.listMembersOfClub(PRESIDENT_CLUB_ID);

        if (members.isEmpty()) {
            model.addElement("No members in your club.");
            return;
        }
        for (String username : members) {
            String role = DataStore.findUser(username)
                                   .map(User::getRole)
                                   .orElse("MEMBER");

            model.addElement(username + " (" + role + ")");
        }
    }
}


