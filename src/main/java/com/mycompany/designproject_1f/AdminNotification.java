package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdminNotification extends JPanel implements Refreshable {

    private final JComboBox<String> clubCombo;
    private final JTextArea msgArea;
    private final JTextArea recentArea;
    private final MainFrame parent;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public AdminNotification(MainFrame frame) {
        this.parent = frame;
        setLayout(null);

        // Left fixed admin panel
        add(new AdminLeftPanel(frame));

        // Page Title
        JLabel title = new JLabel("Send Notification");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(250, 30, 300, 30);
        add(title);

        // Back button 
        JButton backBtn = new JButton("Back");
        backBtn.setBounds(760, 25, 100, 30);
        add(backBtn);
        backBtn.addActionListener(e -> frame.showPage(MainFrame.ADMIN_DASH));

        // Target label
        JLabel clubLbl = new JLabel("Target (select club or ALL):");
        clubLbl.setBounds(250, 80, 200, 25);
        add(clubLbl);

        clubCombo = new JComboBox<>();
        clubCombo.setBounds(450, 80, 300, 25);
        add(clubCombo);

        // Message label
        JLabel msgLbl = new JLabel("Message:");
        msgLbl.setBounds(250, 120, 200, 25);
        add(msgLbl);

        // Message area
        msgArea = new JTextArea();
        msgArea.setLineWrap(true);
        msgArea.setWrapStyleWord(true);
        JScrollPane msgSp = new JScrollPane(msgArea);
        msgSp.setBounds(250, 150, 500, 180);
        add(msgSp);

        // Send button
        JButton send = new JButton("Send");
        send.setBounds(760, 150, 100, 30);
        add(send);
        send.addActionListener(e -> onSend());

        // Recent notifications area
        JLabel recentLbl = new JLabel("Recent Notifications:");
        recentLbl.setBounds(250, 340, 200, 25);
        add(recentLbl);

        recentArea = new JTextArea();
        recentArea.setEditable(false);
        JScrollPane recentSp = new JScrollPane(recentArea);
        recentSp.setBounds(250, 370, 610, 150);
        add(recentSp);

        // Load clubs and notifications
        refresh();
    }

    private void onSend() {
        User u = parent.getCurrentUser();
        if (u == null || !u.isAdmin()) {
            JOptionPane.showMessageDialog(this, "Admin only");
            return;
        }

        String target = (String) clubCombo.getSelectedItem();
        if (target == null || target.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a target");
            return;
        }
        if (target.contains(":")) target = target.split(":", 2)[0];

        String msg = msgArea.getText().trim();
        if (msg.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a message");
            return;
        }

        // check permission: club-admins can only send to their club or individual members of their club
        boolean allowed = DataStore.isAdminAllowedToSendTo(u.getUsername(), target);
        if (!allowed) {
            JOptionPane.showMessageDialog(this, "You are not allowed to send notifications to that target.");
            return;
        }

        boolean ok = DataStore.saveNotification(target, msg, u.getUsername());
        JOptionPane.showMessageDialog(this, ok ? "Notification sent" : "Error sending notification");

        if (ok) {
            msgArea.setText("");
            refresh();
        }
    }

    @Override
    public void refresh() {
        clubCombo.removeAllItems();

        User u = parent.getCurrentUser();
        String adminUsername = u == null ? null : u.getUsername();
        String assignedClubId = adminUsername == null ? null : DataStore.getAdminAssignedClub(adminUsername);

        if (assignedClubId == null || assignedClubId.isEmpty()) {
            // main admin: can send to ALL or any club
            clubCombo.addItem("ALL");
            for (Club c : DataStore.listClubs()) {
                clubCombo.addItem(c.getId() + ":" + c.getName());
            }
        } else {
            for (Club c : DataStore.listClubs()) {
                if (c.getId().equals(assignedClubId)) {
                    clubCombo.addItem(c.getId() + ":" + c.getName());
                    break;
                }
            }
        }

        List<String[]> notes = DataStore.listNotificationsForAdmin(adminUsername);
        StringBuilder sb = new StringBuilder();

        for (int i = notes.size() - 1; i >= 0; i--) {
            String[] r = notes.get(i);
            String ts = r.length > 1 ? r[1] : "";
            String target = r.length > 2 ? r[2] : "";
            String message = r.length > 3 ? r[3] : "";
            String sender = r.length > 4 ? r[4] : "";

            String when = ts;
            try {
                long epoch = Long.parseLong(ts);
                when = sdf.format(new Date(epoch));
            } catch (Exception ignored) {}

            sb.append("Time: ").append(when)
                    .append(" | Target: ").append(target)
                    .append(" | From: ").append(sender).append("\n")
                    .append(message).append("\n\n");
        }

        recentArea.setText(sb.toString());
        recentArea.setCaretPosition(0);
    }
}






