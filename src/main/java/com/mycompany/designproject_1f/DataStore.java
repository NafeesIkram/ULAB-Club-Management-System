package com.mycompany.designproject_1f;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DataStore {
    private static final String DIR = System.getProperty("user.home") + File.separator + "designproject_data";
    private static final String USERS = DIR + File.separator + "users.csv";
    private static final String EVENTS = DIR + File.separator + "events.csv";
    private static final String CLUBS = DIR + File.separator + "clubs.csv";
    private static final String MEMBERSHIPS = DIR + File.separator + "memberships.csv";
    private static final String REGISTRATIONS = DIR + File.separator + "registrations.csv";
    private static final String JOIN_REQUESTS = DIR + File.separator + "join_requests.csv";
    private static final String NOTIFICATIONS = DIR + File.separator + "notifications.csv";
    private static final String POINTS = DIR + File.separator + "points.csv";
    private static final String ADMIN_ASSIGNMENTS = DIR + File.separator + "admin_assignments.csv"; // format: username,clubId

    public static void init() {
        try {
            Files.createDirectories(Paths.get(DIR));
            if (!Files.exists(Paths.get(USERS))) Files.createFile(Paths.get(USERS));
            if (!Files.exists(Paths.get(EVENTS))) Files.createFile(Paths.get(EVENTS));
            if (!Files.exists(Paths.get(CLUBS))) Files.createFile(Paths.get(CLUBS));
            if (!Files.exists(Paths.get(MEMBERSHIPS))) Files.createFile(Paths.get(MEMBERSHIPS));
            if (!Files.exists(Paths.get(REGISTRATIONS))) Files.createFile(Paths.get(REGISTRATIONS));
            if (!Files.exists(Paths.get(JOIN_REQUESTS))) Files.createFile(Paths.get(JOIN_REQUESTS));
            if (!Files.exists(Paths.get(NOTIFICATIONS))) Files.createFile(Paths.get(NOTIFICATIONS));
            if (!Files.exists(Paths.get(POINTS))) Files.createFile(Paths.get(POINTS));
            if (!Files.exists(Paths.get(ADMIN_ASSIGNMENTS))) Files.createFile(Paths.get(ADMIN_ASSIGNMENTS));
        } catch (IOException e) {
            e.printStackTrace();
        }
        seedDefaults();
    }

    private static void seedDefaults() {
        try {
            if (isFileEmpty(USERS)) {
                saveUser("admin", "admin123", "admin@ulab.edu", "ADMIN");
                saveUser("member", "member123", "member@ulab.edu", "MEMBER");
                // NEW: create club-specific admin
                saveUser("admin1", "1234", "admin1@ulab.edu", "ADMIN");
                saveUser("pre1", "1234", "pre@ulab.edu", "PRESIDENT");
            }

            if (isFileEmpty(CLUBS)) {
                saveClub("1", "Robotics Club", "Robotics enthusiasts");
                saveClub("2", "Art Club", "Painting and crafts");
                saveClub("3", "Music Club", "Play and practice music");
                saveClub("4", "ULAB Computer Programming Club", "Programming, problem solving, competitions");
            }
            // assign admin1
            if (getAdminAssignedClub("admin1") == null) assignAdminToClub("admin1", "4");

            if (isFileEmpty(EVENTS)) {
                saveEvent("1", "Welcome Meetup", "2025-12-01", "Open");
                saveEvent("2", "Robotics Workshop", "2025-12-10", "Open");
            }
            if (isFileEmpty(POINTS)) {
                setMemberPoints("admin", 0);
                setMemberPoints("member", 0);
                setMemberPoints("admin1", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isFileEmpty(String path) throws IOException {
        return Files.size(Paths.get(path)) == 0;
    }

    // Save user
    public static boolean saveUser(String username, String password, String email, String role) {
        return saveUser(username, password, email, role, "");
    }

    // Overload allowing optional club assignment stored separately
    public static boolean saveUser(String username, String password, String email, String role, String clubId) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS, true))) {
            pw.println(escape(username) + "," + escape(password) + "," + escape(email) + "," + role);
            if (getMemberPoints(username) == null) setMemberPoints(username, 0);
            if (clubId != null && !clubId.isEmpty()) assignAdminToClub(username, clubId);
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    //delete user
    public static boolean deleteUser(String username) {
        if ("admin".equalsIgnoreCase(username)) {
            return false; // Prevent deleting default admin
        }

        boolean userRemoved =
                removeLineFromFile(USERS, parts -> parts[0].equals(username));

        // Also remove data related to user
        removeLineFromFile(MEMBERSHIPS, parts -> parts[0].equals(username));
        removeLineFromFile(REGISTRATIONS, parts -> parts[0].equals(username));
        removeLineFromFile(JOIN_REQUESTS, parts -> parts[0].equals(username));
        removeLineFromFile(NOTIFICATIONS, parts -> parts[2].equals(username) || parts[4].equals(username));
        removeLineFromFile(POINTS, parts -> parts[0].equals(username));
        removeLineFromFile(ADMIN_ASSIGNMENTS, parts -> parts[0].equals(username));

        return userRemoved;
    }

    public static Optional<User> findUser(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = parseCSV(line);
                if (p[0].equals(username)) {
                    return Optional.of(new User(p[0], p[1], p[2], p[3]));
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public static boolean resetPassword(String username, String newPass) {
        try {
            File inFile = new File(USERS);
            File tempFile = new File(USERS + ".tmp");
            try (BufferedReader br = new BufferedReader(new FileReader(inFile));
                 PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = parseCSV(line);
                    if (p.length > 0 && p[0].equals(username)) {
                        pw.println(escape(p[0]) + "," + escape(newPass) + "," + escape(p.length>2?p[2]:"") + "," + (p.length>3?p[3]:"MEMBER"));
                    } else pw.println(line);
                }
            }
            Files.move(tempFile.toPath(), inFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    //clubs
    public static boolean saveClub(String id, String name, String desc) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CLUBS, true))) {
            pw.println(escape(id) + "," + escape(name) + "," + escape(desc));
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public static List<Club> listClubs() {
        List<Club> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CLUBS))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = parseCSV(line);
                if (p.length >= 2) out.add(new Club(p[0], p[1], p.length>2?p[2]:""));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return out;
    }

    //memberships and join requests
    public static boolean requestJoinClub(String username, String clubId) {
        try {
            // prevent duplicate pending request
            List<String> lines = Files.readAllLines(Paths.get(JOIN_REQUESTS));
            for (String l : lines) {
                String[] p = parseCSV(l);
                if (p.length >= 2 && p[0].equals(username) && p[1].equals(clubId)) return false;
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter(JOIN_REQUESTS, true))) {
                pw.println(escape(username) + "," + escape(clubId) + "," + System.currentTimeMillis());
            }
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public static boolean joinClub(String username, String clubId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(MEMBERSHIPS));
            for (String l : lines) {
                String[] p = parseCSV(l);
                if (p.length >= 2 && p[0].equals(username) && p[1].equals(clubId)) return false;
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter(MEMBERSHIPS, true))) {
                pw.println(escape(username) + "," + escape(clubId));
            }
            // ensure points row exists
            if (getMemberPoints(username) == null) setMemberPoints(username, 0);
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public static boolean leaveClub(String username, String clubId) {
        try {
            File inFile = new File(MEMBERSHIPS);
            File tempFile = new File(MEMBERSHIPS + ".tmp");
            try (BufferedReader br = new BufferedReader(new FileReader(inFile));
                 PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = parseCSV(line);
                    if (p.length >= 2 && p[0].equals(username) && p[1].equals(clubId)) {
                        // skip (remove)
                    } else pw.println(line);
                }
            }
            Files.move(tempFile.toPath(), inFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public static List<String> listMemberships(String username) {
        List<String> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(MEMBERSHIPS))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = parseCSV(line);
                if (p.length >= 2 && p[0].equals(username)) out.add(p[1]);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return out;
    }

    public static List<String[]> listJoinRequests() {
        List<String[]> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(JOIN_REQUESTS))) {
            String line;
            while ((line = br.readLine()) != null) out.add(parseCSV(line));
        } catch (IOException e) { e.printStackTrace(); }
        return out;
    }
    public static List<String[]> listJoinRequestsForAdmin(String adminUsername) {
        String clubId = getAdminAssignedClub(adminUsername);
        if (clubId == null || clubId.isEmpty()) return listJoinRequests(); // main admin
        List<String[]> out = new ArrayList<>();
        for (String[] r : listJoinRequests()) {
            if (r.length >= 2 && r[1].equals(clubId)) out.add(r);
        }
        return out;
    }

    public static boolean approveJoinRequest(String username, String clubId) {
        boolean joined = joinClub(username, clubId);
        if (!joined) return false;
        return removeLineFromFile(JOIN_REQUESTS, (parts) -> parts.length>=2 && parts[0].equals(username) && parts[1].equals(clubId));
    }

    public static boolean denyJoinRequest(String username, String clubId) {
        return removeLineFromFile(JOIN_REQUESTS, (parts) -> parts.length>=2 && parts[0].equals(username) && parts[1].equals(clubId));
    }

    //events 
    public static boolean saveEvent(String id, String title, String date, String status) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(EVENTS, true))) {
            pw.println(escape(id) + "," + escape(title) + "," + escape(date) + "," + escape(status));
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public static List<String[]> listEventsRaw() {
        List<String[]> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(EVENTS))) {
            String line;
            while ((line = br.readLine()) != null) out.add(parseCSV(line));
        } catch (IOException e) { e.printStackTrace(); }
        return out;
    }

    public static List<Event> listEvents() {
        List<Event> out = new ArrayList<>();
        for (String[] row : listEventsRaw()) {
            if (row.length >= 4) out.add(new Event(row[0], row[1], row[2], row[3]));
            else if (row.length >= 2) out.add(new Event(row[0], row[1], row.length>2?row[2]:"", row.length>3?row[3]:""));
        }
        return out;
    }

//edit event
    public static boolean editEvent(String id, String newTitle, String newDate, String newStatus) {
        return removeLineFromFile(EVENTS, (parts) -> parts.length>=1 && parts[0].equals(id))
                && saveEvent(id, newTitle, newDate, newStatus);
        
    }
//delete event
    public static boolean deleteEvent(String id) {
        boolean removed = removeLineFromFile(EVENTS, (parts) -> parts.length>=1 && parts[0].equals(id));
        if (!removed) return false;
        removeLineFromFile(REGISTRATIONS, (parts) -> parts.length>=2 && parts[1].equals(id));
        return true;
    }

    //registrations and points
    public static boolean registerForEvent(String username, String eventId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(REGISTRATIONS));
            for (String l : lines) {
                String[] p = parseCSV(l);
                if (p.length >= 2 && p[0].equals(username) && p[1].equals(eventId)) return false;
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter(REGISTRATIONS, true))) {
                pw.println(escape(username) + "," + escape(eventId));
            }
            // Award points for registering
            incrementMemberPoints(username, 5);
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public static boolean unregisterForEvent(String username, String eventId) {
    boolean removed = false;
    try {
        File inFile = new File(REGISTRATIONS);
        File tempFile = new File(REGISTRATIONS + ".tmp");

        try (BufferedReader br = new BufferedReader(new FileReader(inFile));
             PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {

            String line;

            while ((line = br.readLine()) != null) {
                String[] p = parseCSV(line);

                // If match found, skip it
                if (p.length >= 2 && p[0].equals(username) && p[1].equals(eventId)) {
                    removed = true;
                    // don't write it
                } else {
                    pw.println(line);
                }
            }
        }

        Files.move(tempFile.toPath(), inFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
    // Deduct 5 points
    if (removed) {
        incrementMemberPoints(username, -5);
    }

    return removed;
}

    public static List<String> listRegistrations(String username) {
        List<String> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(REGISTRATIONS))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = parseCSV(line);
                if (p.length >= 2 && p[0].equals(username)) out.add(p[1]);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return out;
    }

    public static List<String> listRegisteredEvents(String username) {
        return listRegistrations(username);
    }

    //notifications
    public static boolean saveNotification(String target, String message, String sender) {
        String id = Long.toString(System.currentTimeMillis());
        String ts = Long.toString(System.currentTimeMillis());
        try (PrintWriter pw = new PrintWriter(new FileWriter(NOTIFICATIONS, true))) {
            pw.println(escape(id) + "," + escape(ts) + "," + escape(target) + "," + escape(message) + "," + escape(sender));
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public static List<String[]> listNotifications() {
        List<String[]> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(NOTIFICATIONS))) {
            String line;
            while ((line = br.readLine()) != null) out.add(parseCSV(line));
        } catch (IOException e) { e.printStackTrace(); }
        return out;
    }
    public static List<String[]> listNotificationsForUser(String username) {
        List<String[]> out = new ArrayList<>();
        List<String> memberships = listMemberships(username);
        for (String[] row : listNotifications()) {
            if (row.length < 5) continue;
            String target = row[2];
            if ("ALL".equalsIgnoreCase(target) || target.equals(username) || memberships.contains(target)) out.add(row);
        }
        return out;
    }

    // NEW: List notifications visible to an admin in Data Manager. If admin assigned to club -> only that club's notifications
    public static List<String[]> listNotificationsForAdmin(String adminUsername) {
        String clubId = getAdminAssignedClub(adminUsername);
        if (clubId == null || clubId.isEmpty()) return listNotifications(); // main admin
        List<String[]> out = new ArrayList<>();
        Set<String> members = new HashSet<>(listMembersOfClub(clubId));
        for (String[] row : listNotifications()) {
            if (row.length < 5) continue;
            String target = row[2];
            if (target.equals(clubId) || members.contains(target)) out.add(row);
        }
        return out;
    }

    public static boolean isAdminAllowedToSendTo(String adminUsername, String target) {
        String clubId = getAdminAssignedClub(adminUsername);
        if (clubId == null || clubId.isEmpty()) return true;
        if (clubId.equals(target)) return true;
        List<String> members = listMembersOfClub(clubId);
        return members.contains(target);
    }

    //points system
    public static Integer getMemberPoints(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(POINTS))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = parseCSV(line);
                if (p.length >= 2 && p[0].equals(username)) {
                    try { return Integer.parseInt(p[1]); } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    public static boolean setMemberPoints(String username, int points) {
        try {
            File inFile = new File(POINTS);
            File tempFile = new File(POINTS + ".tmp");
            boolean found = false;
            try (BufferedReader br = new BufferedReader(new FileReader(inFile));
                 PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = parseCSV(line);
                    if (p.length >= 1 && p[0].equals(username)) {
                        pw.println(escape(username) + "," + points);
                        found = true;
                    } else pw.println(line);
                }
                if (!found) {
                    pw.println(escape(username) + "," + points);
                }
            }
            Files.move(tempFile.toPath(), inFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public static boolean incrementMemberPoints(String username, int delta) {
        Integer cur = getMemberPoints(username);
        if (cur == null) cur = 0;
        return setMemberPoints(username, cur + delta);
    }

    public static Map<String,Integer> listMemberPoints() {
        Map<String,Integer> out = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(POINTS))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = parseCSV(line);
                if (p.length >= 2) {
                    try {
                        out.put(p[0], Integer.parseInt(p[1]));
                    } catch (NumberFormatException ex) { out.put(p[0], 0); }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return out;
    }

    public static List<String> listMembersOfClub(String clubId) {
        List<String> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(MEMBERSHIPS))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = parseCSV(line);
                if (p.length >= 2 && p[1].equals(clubId)) out.add(p[0]);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return out;
    }
    public static List<String> listMembersForAdmin(String adminUsername) {
        String clubId = getAdminAssignedClub(adminUsername);
        if (clubId == null || clubId.isEmpty()) {
            Set<String> s = new LinkedHashSet<>();
            try (BufferedReader br = new BufferedReader(new FileReader(MEMBERSHIPS))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = parseCSV(line);
                    if (p.length >= 2) s.add(p[0]);
                }
            } catch (IOException e) { e.printStackTrace(); }
            return new ArrayList<>(s);
        } else {
            return listMembersOfClub(clubId);
        }
    }

    //admin assignments storage
    public static boolean assignAdminToClub(String adminUsername, String clubId) {
        try {
            File inFile = new File(ADMIN_ASSIGNMENTS);
            File tempFile = new File(ADMIN_ASSIGNMENTS + ".tmp");
            boolean found = false;
            try (BufferedReader br = new BufferedReader(new FileReader(inFile));
                 PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = parseCSV(line);
                    if (p.length >= 1 && p[0].equals(adminUsername)) {
                        pw.println(escape(adminUsername) + "," + escape(clubId));
                        found = true;
                    } else pw.println(line);
                }
                if (!found) {
                    pw.println(escape(adminUsername) + "," + escape(clubId));
                }
            }
            Files.move(tempFile.toPath(), inFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }
    public static String getAdminAssignedClub(String adminUsername) {
        try (BufferedReader br = new BufferedReader(new FileReader(ADMIN_ASSIGNMENTS))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = parseCSV(line);
                if (p.length >= 2 && p[0].equals(adminUsername)) return p[1];
            }
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    //helpers 
    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\"", "\"\"").replace("\n", " ").replace("\r", " ");
    }

    private static String[] parseCSV(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i=0;i<line.length();i++) {
            char c = line.charAt(i);
            if (c == '"' ) {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                parts.add(cur.toString());
                cur.setLength(0);
            } else cur.append(c);
        }
        parts.add(cur.toString());
        return parts.toArray(new String[0]);
    }

    private interface LineMatcher { boolean matches(String[] parts); }

    private static boolean removeLineFromFile(String path, LineMatcher matcher) {
        try {
            File inFile = new File(path);
            File tempFile = new File(path + ".tmp");
            try (BufferedReader br = new BufferedReader(new FileReader(inFile));
                 PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = parseCSV(line);
                    if (matcher.matches(p)) {
                        // skip
                    } else pw.println(line);
                }
            }
            Files.move(tempFile.toPath(), inFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }
    public static Event getEventById(String id) {
    for (Event e : listEvents()) { 
        if (e.getId().equals(id)) return e;
    }
    return null;
}
}  

