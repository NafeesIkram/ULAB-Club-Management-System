package com.mycompany.designproject_1f;

public class Club {
    private final String id;
    private final String name;
    private final String description;

    public Club(String id, String name, String desc) {
        this.id = id; this.name = name; this.description = desc;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}

