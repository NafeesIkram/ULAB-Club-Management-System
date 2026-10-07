package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MemberEvents extends JPanel {
    private JList<String> eventList;
    private DefaultListModel<String> model;
    private JCheckBox showJoinedOnly;

    public MemberEvents(MainFrame frame) {
        setLayout(null);
        MemberLeftPanel left = new MemberLeftPanel(frame);
        add(left);
        int X = 200;
        JLabel title = new JLabel("Upcoming Events");
        title.setBounds(250, 30, 400, 30);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        add(title);
        showJoinedOnly = new JCheckBox("Show joined only");
        showJoinedOnly.setBounds(250, 70, 160, 25);
        add(showJoinedOnly);

        model = new DefaultListModel<>();
        eventList = new JList<>(model);
        JScrollPane sp = new JScrollPane(eventList);
        sp.setBounds(250, 100, 610, 360);
        add(sp);

        JButton register = new JButton("Register");
        register.setBounds(250, 480, 120, 30);
        add(register);

        JButton unregister = new JButton("Unregister");
        unregister.setBounds(400, 480, 120, 30);
        add(unregister);

        JButton refresh = new JButton("Refresh");
        refresh.setBounds(740, 480, 120, 30);
        add(refresh);

        JButton back = new JButton("Back");
        back.setBounds(760, 25, 100, 30);
        back.addActionListener(e -> frame.showPage(MainFrame.MEMBER_DASH));
        add(back);

        refresh.addActionListener(e -> loadEvents(frame.getCurrentUser()));
        showJoinedOnly.addActionListener(e -> loadEvents(frame.getCurrentUser()));

        register.addActionListener(e -> {
            User u = frame.getCurrentUser();
            if (u == null) {
                JOptionPane.showMessageDialog(this, "Login first");
                frame.showPage(MainFrame.LOGIN);
                return;
            }

            int idx = eventList.getSelectedIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(this, "Select an event");
                return;
            }

            List<Event> events = DataStore.listEvents();
            Event ev = events.get(idx);

            boolean ok = DataStore.registerForEvent(u.getUsername(), ev.getId());
            JOptionPane.showMessageDialog(this, ok ? "Registered for event" : "Already registered or error");

            loadEvents(u);
        });

        unregister.addActionListener(e -> {
            User u = frame.getCurrentUser();
            if (u == null) {
                JOptionPane.showMessageDialog(this, "Login first");
                frame.showPage(MainFrame.LOGIN);
                return;
            }

            int idx = eventList.getSelectedIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(this, "Select an event");
                return;
            }

            List<Event> events = DataStore.listEvents();
            Event ev = events.get(idx);

            boolean ok = DataStore.unregisterForEvent(u.getUsername(), ev.getId());
            JOptionPane.showMessageDialog(this, ok ? "Unregistered" : "Error");

            loadEvents(u);
        });

        loadEvents(frame.getCurrentUser());
    }

    private void loadEvents(User u) {
        model.clear();

        List<Event> allEvents = DataStore.listEvents();
        List<String> registered = (u == null) ? List.of() :
                DataStore.listRegisteredEvents(u.getUsername());

        for (Event e : allEvents) {
            boolean isRegistered = registered.contains(e.getId());
            String label = e.getTitle() + " — " + e.getDate() + (isRegistered ? " (Registered)" : "");

            if (showJoinedOnly.isSelected()) {
                if (isRegistered)
                    model.addElement(label);
            } else {
                model.addElement(label);
            }
        }
    }
}


