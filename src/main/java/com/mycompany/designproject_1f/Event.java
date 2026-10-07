package com.mycompany.designproject_1f;

public class Event {
    private final String id;
    private final String title;
    private final String date;
    private final String status;
    public Event(String id, String title, String date, String status) {
        this.id = id; this.title = title; this.date = date; this.status = status;
    }
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
}
