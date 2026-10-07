package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MemberProgress extends JPanel implements Refreshable {
    private final MainFrame frame;
    private final JTextArea area;
    private final JLabel pointLabel;

    public MemberProgress(MainFrame frame) {
        this.frame = frame;
        setLayout(null);
        MemberLeftPanel left = new MemberLeftPanel(frame);
        add(left);
        int X = 200;

        JLabel title = new JLabel("Member Progress");
        title.setBounds(250, 30, 400, 30);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        add(title);

        pointLabel = new JLabel();
        pointLabel.setBounds(250, 70, 400, 30);
        pointLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(pointLabel);

        area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane sp = new JScrollPane(area);
        sp.setBounds(250, 100, 610, 360);
        add(sp);

        JButton back = new JButton("Back");
        back.setBounds(760, 25, 100, 30);
        back.addActionListener(e -> frame.showPage(MainFrame.MEMBER_DASH));
        add(back);

        refresh();
    }

    @Override
    public void refresh() {
        User u = frame.getCurrentUser();
        StringBuilder sb = new StringBuilder();

        if (u == null) {
            pointLabel.setText("<html><b>Please login to see progress.</b></html>");
            area.setText("");
            return;
        }
        Integer points = DataStore.getMemberPoints(u.getUsername());
        if (points == null) points = 0;
        List<String> regs = DataStore.listRegistrations(u.getUsername());

        pointLabel.setText("<html><b>My Point is: " + points + "</b></html>");

        sb.append("Events participated: ").append(regs.size()).append("\n");
        for (String id : regs) {
            Event e = DataStore.getEventById(id);
            if (e != null) {
                sb.append("- Event: ").append(e.getTitle()).append("\n");
            } else {
                sb.append("- Event ID: ").append(id).append(" (not found)\n");
            }
        }

        area.setText(sb.toString());
    }
}



