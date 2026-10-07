package com.mycompany.designproject_1f;

public class User {
    private final String username;
    private final String password;
    private final String email;
    private final String role;

    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role == null ? "MEMBER" : role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    public boolean isAdmin() { return "ADMIN".equalsIgnoreCase(role); }
    public boolean isPresident() { return "PRESIDENT".equalsIgnoreCase(role); }
}

