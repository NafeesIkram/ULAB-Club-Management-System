package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminViewMembers extends JPanel {

    private DefaultListModel<String> model;
    private JList<String> userList;

    public AdminViewMembers(MainFrame frame) {
        setLayout(null);

        // Left fixed admin panel
        add(new AdminLeftPanel(frame));

        // Page Title
        JLabel title = new JLabel("View Members");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(250, 30, 300, 30);
        add(title);

        // Back button (top-right)
        JButton backBtn = new JButton("Back");
        backBtn.setBounds(760, 25, 100, 30);
        add(backBtn);
        backBtn.addActionListener(e -> frame.showPage(MainFrame.ADMIN_DASH));

        // Members list
        model = new DefaultListModel<>();
        userList = new JList<>(model);
        JScrollPane sp = new JScrollPane(userList);
        sp.setBounds(250, 90, 610, 400);
        add(sp);

        // Refresh button
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(740, 510, 120, 30);
        add(refreshBtn);
        refreshBtn.addActionListener(e -> loadUsers(frame));

        // Delete user button
        JButton deleteBtn = new JButton("Delete User");
        deleteBtn.setBounds(600, 510, 120, 30);
        add(deleteBtn);
        deleteBtn.addActionListener(e -> {
            String selected = userList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a user first!");
                return;
            }

            String username = selected.split(" ")[0];
            // prevent deleting the main admin user
            if (username.equalsIgnoreCase("admin")) {
                JOptionPane.showMessageDialog(this, "You cannot delete the admin user!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete user: " + username + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (DataStore.deleteUser(username)) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully.");
                    loadUsers(frame);
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting user.");
                }
            }
        });

        // Load users initially
        loadUsers(frame);
    }

    private void loadUsers(MainFrame frame) {
        model.clear();
        User cur = frame.getCurrentUser();
        if (cur == null) {
            model.addElement("No user logged in.");
            return;
        }

        // Use DataStore helper that returns visible members for this admin
        List<String> users = DataStore.listMembersForAdmin(cur.getUsername());
        if (users == null || users.isEmpty()) {
            model.addElement("No members found.");
            return;
        }

        for (String u : users) {
            // try to show role too (if available)
            var opt = DataStore.findUser(u);
            String role = opt.map(User::getRole).orElse("MEMBER");
            model.addElement(u + " (" + role + ")");
        }
    }
}



