package com.edutrack.service;

import com.edutrack.exception.AuthenticationException;
import com.edutrack.model.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Authentication and authorization service managing user sessions and RBAC.
 */
public class AuthService {

    private final Map<String, User> userRegistry = new ConcurrentHashMap<>();
    private User currentUser;

    public AuthService() {
        seedDefaultAccounts();
    }

    private void seedDefaultAccounts() {
        // Faculty Account
        register(new FacultyUser("faculty", hashPassword("admin123"), "Dr. K. Raman", "k.raman@vit.ac.in", "Computer Science", "FAC101"));
        // Admin Account
        register(new AdminUser("admin", hashPassword("root123"), "System Administrator", "admin@vit.ac.in"));
        // Student Account
        register(new StudentUser("student", hashPassword("student123"), "Aarav Sharma", "aarav.sharma2023@vitstudent.ac.in", "23BCE1001"));
    }

    public void register(User user) {
        userRegistry.put(user.getUsername().toLowerCase(), user);
    }

    public User login(String username, String password) throws AuthenticationException {
        if (username == null || password == null) {
            throw new AuthenticationException("Username and password cannot be null");
        }

        User user = userRegistry.get(username.trim().toLowerCase());
        if (user == null) {
            throw new AuthenticationException("Invalid username or credentials");
        }

        String hashedInput = hashPassword(password.trim());
        if (!user.getPasswordHash().equals(hashedInput)) {
            throw new AuthenticationException("Invalid username or password");
        }

        this.currentUser = user;
        return user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public boolean hasRole(UserRole role) {
        return currentUser != null && currentUser.getRole() == role;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm missing", e);
        }
    }
}
