package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {

    public static final String LOGIN = "LoginPage";
    public static final String REGISTER = "RegisterPage";
    public static final String RESET = "ResetPasswordPage";

    public static final String MEMBER_DASH = "MemberDashboard";
    public static final String MEMBER_VIEW = "MemberViewClubs";
    public static final String MEMBER_EVENTS = "MemberEvents";
    public static final String MEMBER_PROGRESS = "MemberProgress";
    public static final String MEMBER_NOTIFICATION = "MemberNotification";

    public static final String ADMIN_DASH = "AdminDashboard";
    public static final String ADMIN_VIEW_MEMBERS = "AdminViewMembers";
    public static final String ADMIN_APPROVAL = "AdminMemberApproval";
    public static final String ADMIN_EVENTS = "AdminEventManagement";
    public static final String ADMIN_NOTIFICATION = "AdminNotification";
    public static final String ADMIN_DATA = "AdminDataManagement";
    public static final String ADMIN_MEMBER_PROGRESS = "AdminMemberProgress";

    public static final String PRESIDENT_DASH = "PresidentDashboard";
    public static final String PRESIDENT_VIEW_MEMBERS = "PresidentViewMembers";
    public static final String PRESIDENT_APPROVAL = "PresidentMemberApproval";
    public static final String PRESIDENT_EVENTS = "PresidentEventManagement";
    public static final String PRESIDENT_NOTIFICATION = "PresidentNotification";
    public static final String PRESIDENT_MEMBER_PROGRESS = "PresidentMemberProgress";

    private CardLayout cardLayout;
    private JPanel container;

    private final Map<String, JPanel> pages = new HashMap<>();

    private User currentUser = null;

    public MainFrame() {
        setTitle("ULAB Club Management System");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load dummy data
        DataStore.init();

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        addPage(new LoginPage(this), LOGIN);
        addPage(new RegisterPage(this), REGISTER);
        addPage(new ResetPasswordPage(this), RESET);

        addPage(new MemberDashboard(this), MEMBER_DASH);
        addPage(new MemberViewClubs(this), MEMBER_VIEW);
        addPage(new MemberEvents(this), MEMBER_EVENTS);
        addPage(new MemberProgress(this), MEMBER_PROGRESS);
        addPage(new MemberNotification(this), MEMBER_NOTIFICATION);

        addPage(new AdminDashboard(this), ADMIN_DASH);
        addPage(new AdminViewMembers(this), ADMIN_VIEW_MEMBERS);
        addPage(new AdminMemberApproval(this), ADMIN_APPROVAL);
        addPage(new AdminEventManagement(this), ADMIN_EVENTS);
        addPage(new AdminNotification(this), ADMIN_NOTIFICATION);
        addPage(new AdminDataManagement(this), ADMIN_DATA);
        addPage(new AdminMemberProgress(this), ADMIN_MEMBER_PROGRESS);

        addPage(new PresidentDashboard(this), PRESIDENT_DASH);
        addPage(new PresidentMemberApproval(this), PRESIDENT_APPROVAL);
        addPage(new PresidentEventManagement(this), PRESIDENT_EVENTS);
        addPage(new PresidentNotification(this), PRESIDENT_NOTIFICATION);
        addPage(new PresidentMemberProgress(this), PRESIDENT_MEMBER_PROGRESS);
        addPage(new PresidentViewMembers(this), PRESIDENT_VIEW_MEMBERS);

        add(container);
        showPage(LOGIN);
    }

    private void addPage(JPanel panel, String key) {
        container.add(panel, key);
        pages.put(key, panel);
    }

 
    public void showPage(String page) {
        try {
            JPanel p = pages.get(page);
            if (p == null) {
                throw new IllegalArgumentException("Page not registered: " + page);
            }

            cardLayout.show(container, page);

            if (p instanceof Refreshable) {
                ((Refreshable) p).refresh();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Page not found: " + page + "\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
