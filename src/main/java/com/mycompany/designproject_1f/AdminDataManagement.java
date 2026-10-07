package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AdminDataManagement extends JPanel {

    public AdminDataManagement(MainFrame frame) {
        setLayout(null);
        add(new AdminLeftPanel(frame));
        JLabel title = new JLabel("Club Data");
        title.setBounds(250, 25, 300, 30);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(760, 25, 100, 30);
        add(backBtn);
        backBtn.addActionListener(e -> frame.showPage(MainFrame.ADMIN_DASH));


        JTextArea area = new JTextArea();
        area.setEditable(false);
        JScrollPane sp = new JScrollPane(area);
        sp.setBounds(250, 80, 610, 480); // shifted right
        add(sp);


        JButton viewUsers = new JButton("Users.csv");
        viewUsers.setBounds(250, 570, 120, 30);
        add(viewUsers);

        JButton viewEventRegistrations = new JButton("Event Registrations");
        viewEventRegistrations.setBounds(380, 570, 160, 30);
        add(viewEventRegistrations);

        JButton viewClubs = new JButton("Clubs.csv");
        viewClubs.setBounds(550, 570, 120, 30);
        add(viewClubs);

        JButton viewMemberships = new JButton("Memberships");
        viewMemberships.setBounds(680, 570, 160, 30);
        add(viewMemberships);

   
        viewUsers.addActionListener(e -> area.setText(readFile("users.csv")));
        viewClubs.addActionListener(e -> area.setText(readFile("clubs.csv")));

        // MEMBERSHIP VIEW
        viewMemberships.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("Member → Club\n-------------------------\n");

            try {
                Path membershipsPath = Paths.get(System.getProperty("user.home"), "designproject_data", "memberships.csv");
                Path clubsPath = Paths.get(System.getProperty("user.home"), "designproject_data", "clubs.csv");

                // Read club ID → club name mapping
                Map<String, String> clubNames = new HashMap<>();
                if (Files.exists(clubsPath)) {
                    for (String line : Files.readAllLines(clubsPath)) {
                        String[] p = line.split(",", 3);
                        if (p.length >= 2) clubNames.put(p[0], p[1]);
                    }
                }

                // Read membership data
                if (Files.exists(membershipsPath)) {
                    for (String line : Files.readAllLines(membershipsPath)) {
                        String[] p = line.split(",", 2);
                        if (p.length >= 2) {
                            String username = p[0];
                            String clubId = p[1];
                            String clubName = clubNames.getOrDefault(clubId, "Unknown Club");
                            sb.append(username).append(" → ").append(clubName).append("\n");
                        }
                    }
                } else {
                    sb.append("No membership data found.");
                }
            } catch (IOException ex) {
                sb.append("Error reading memberships: ").append(ex.getMessage());
            }

            area.setText(sb.toString());
        });

        // EVENT REGISTRATION VIEW
        viewEventRegistrations.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("User → Event\n------------------------------\n");

            try {
                Path regPath = Paths.get(System.getProperty("user.home"), "designproject_data", "registrations.csv");
                Path eventsPath = Paths.get(System.getProperty("user.home"), "designproject_data", "events.csv");

                Map<String, String> eventNames = new HashMap<>();
                if (Files.exists(eventsPath)) {
                    for (String line : Files.readAllLines(eventsPath)) {
                        String[] p = line.split(",", 4);
                        if (p.length >= 2) eventNames.put(p[0], p[1]);
                    }
                }

                if (Files.exists(regPath)) {
                    for (String line : Files.readAllLines(regPath)) {
                        String[] p = line.split(",", 2);
                        if (p.length >= 2) {
                            String username = p[0];
                            String eventId = p[1];
                            String eventName = eventNames.getOrDefault(eventId, "Unknown Event");
                            sb.append(username).append(" → ").append(eventName).append("\n");
                        }
                    }
                } else {
                    sb.append("No event registrations found.");
                }

            } catch (IOException ex) {
                sb.append("Error reading registrations: ").append(ex.getMessage());
            }

            area.setText(sb.toString());
        });
    }


    // FILE READER
    private String readFile(String name) {
        Path p = Paths.get(System.getProperty("user.home"), "designproject_data", name);
        try {
            return Files.readString(p);
        } catch (IOException e) {
            return "Error reading " + name + ": " + e.getMessage();
        }
    }
}




