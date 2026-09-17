package com.edutrack.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class demonstrating OOP Abstraction and Encapsulation.
 * Defines shared identity, credentials, and access control properties.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private final UserRole role;
    private final LocalDateTime createdAt;

    public User(String username, String passwordHash, String fullName, String email, UserRole role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password hash cannot be empty");
        }
        this.username = username.trim();
        this.passwordHash = passwordHash;
        this.fullName = fullName != null ? fullName.trim() : "";
        this.email = email != null ? email.trim() : "";
        this.role = Objects.requireNonNull(role, "UserRole cannot be null");
        this.createdAt = LocalDateTime.now();
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password hash cannot be empty");
        }
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Polymorphic method implemented by concrete user subtypes to return authorized capabilities.
     *
     * @return List of capability strings granted to the specific role.
     */
    public abstract List<String> getDashboardCapabilities();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", role.name(), fullName, username);
    }
}
