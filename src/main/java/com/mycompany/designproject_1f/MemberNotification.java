package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MemberNotification extends JPanel implements Refreshable {
    private final JTextArea area;
    private final MainFrame parent;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MemberNotification(MainFrame frame) {
        this.parent = frame;
        setLayout(null);
        MemberLeftPanel left = new MemberLeftPanel(frame);
        add(left);
        int X = 220; 
        JLabel title = new JLabel("Notifications");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(250, 30, 400, 30);
        add(title);
        area = new JTextArea();
        area.setEditable(false);
        JScrollPane sp = new JScrollPane(area);
        sp.setBounds(250, 100, 610, 360);
        add(sp);

        JButton back = new JButton("Back");
        back.setBounds(760, 25, 100, 30); // right side
        back.addActionListener(e -> frame.showPage(MainFrame.MEMBER_DASH));
        add(back);
    }

    @Override
    public void refresh() {
        User u = parent.getCurrentUser();
        if (u == null) {
            area.setText("Please login to see notifications.");
            return;
        }
        List<String[]> notes = DataStore.listNotificationsForUser(u.getUsername());
        if (notes.isEmpty()) {
            area.setText("No notifications.");
            return;
        }
        StringBuilder sb = new StringBuilder();

        for (int i = notes.size() - 1; i >= 0; i--) {
            String[] r = notes.get(i);
            String ts = r.length > 1 ? r[1] : "";
            String target = r.length > 2 ? r[2] : "";
            String msg = r.length > 3 ? r[3] : "";
            String sender = r.length > 4 ? r[4] : "";

            String when = ts;
            try {
                long epoch = Long.parseLong(ts);
                when = sdf.format(new Date(epoch));
            } catch (Exception ignored) {}

            sb.append("Time: ").append(when)
              .append(" | Target: ").append(target)
              .append(" | From: ").append(sender).append("\n");
            sb.append(msg).append("\n\n");
        }
        area.setText(sb.toString());
        area.setCaretPosition(0);
    }
}





