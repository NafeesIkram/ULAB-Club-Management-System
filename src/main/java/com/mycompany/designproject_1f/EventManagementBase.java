package com.mycompany.designproject_1f;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class EventManagementBase extends JPanel implements Refreshable {

    protected DefaultListModel<String> model;
    protected JTextField idField, titleField, dateField, statusField;

    protected final MainFrame frame;

    // Subclass must return the left panel
    protected abstract JPanel createLeftPanel();

    protected abstract void onBack();

    protected abstract boolean canModify();

    protected abstract String getPageTitle();

    public EventManagementBase(MainFrame frame) {
        this.frame = frame;
        setLayout(null);

       
        add(createLeftPanel());

        JLabel title = new JLabel(getPageTitle());
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(250, 30, 500, 30);
        add(title);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(760, 25, 100, 30);
        backBtn.addActionListener(e -> onBack());
        add(backBtn);

        JLabel idLbl = new JLabel("ID:");
        idLbl.setBounds(240, 90, 40, 25);
        add(idLbl);

        idField = new JTextField();
        idField.setBounds(290, 90, 100, 25);
        add(idField);

        JLabel tLbl = new JLabel("Title:");
        tLbl.setBounds(410, 90, 50, 25);
        add(tLbl);

        titleField = new JTextField();
        titleField.setBounds(460, 90, 200, 25);
        add(titleField);

        JLabel dLbl = new JLabel("Date:");
        dLbl.setBounds(670, 90, 50, 25);
        add(dLbl);

        dateField = new JTextField();
        dateField.setBounds(720, 90, 150, 25);
        add(dateField);

        JLabel sLbl = new JLabel("Status:");
        sLbl.setBounds(240, 130, 50, 25);
        add(sLbl);

        statusField = new JTextField();
        statusField.setBounds(290, 130, 100, 25);
        add(statusField);

        JButton addBtn = new JButton("Add Event");
        addBtn.setBounds(630, 130, 100, 30);
        add(addBtn);

        JButton editBtn = new JButton("Edit Event");
        editBtn.setBounds(760, 130, 100, 30);
        add(editBtn);

        JButton deleteBtn = new JButton("Delete Event");
        deleteBtn.setBounds(740, 550, 120, 30);
        add(deleteBtn);

        model = new DefaultListModel<>();
        JList<String> eventList = new JList<>(model);
        JScrollPane sp = new JScrollPane(eventList);
        sp.setBounds(250, 180, 610, 360);
        add(sp);

        // ACTIONS
        addBtn.addActionListener(e -> {
            if (!canModify()) {
                JOptionPane.showMessageDialog(this, "You do not have permission.");
                return;
            }

            String id = idField.getText().trim();
            String t = titleField.getText().trim();
            String d = dateField.getText().trim();
            String s = statusField.getText().trim();

            if (id.isEmpty() || t.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Provide id and title");
                return;
            }

            boolean ok = DataStore.saveEvent(id, t, d, s.isEmpty() ? "Open" : s);
            JOptionPane.showMessageDialog(this, ok ? "Saved" : "Error");

            if (ok) loadEvents();
        });

        editBtn.addActionListener(e -> {
            if (!canModify()) {
                JOptionPane.showMessageDialog(this, "You do not have permission.");
                return;
            }

            String id = idField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter event ID to edit");
                return;
            }

            boolean ok = DataStore.editEvent(
                    id,
                    titleField.getText().trim(),
                    dateField.getText().trim(),
                    statusField.getText().trim()
            );

            JOptionPane.showMessageDialog(this, ok ? "Edited" : "Error editing");
            if (ok) loadEvents();
        });

        deleteBtn.addActionListener(e -> {
            if (!canModify()) {
                JOptionPane.showMessageDialog(this, "You do not have permission.");
                return;
            }

            String id = idField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter event ID to delete");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete event " + id + "?",
                    "Confirm", JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;

            boolean ok = DataStore.deleteEvent(id);
            JOptionPane.showMessageDialog(this, ok ? "Deleted" : "Error deleting");

            if (ok) loadEvents();
        });
    }

    public void loadEvents() {
        model.clear();
        List<Event> events = DataStore.listEvents();

        for (Event ev : events)
            model.addElement(ev.getId() + " | " + ev.getTitle() +
                    " — " + ev.getDate() + " (" + ev.getStatus() + ")");

        if (events.isEmpty())
            model.addElement("No events found.");
    }

    @Override
    public void refresh() {
        loadEvents();
    }
}

