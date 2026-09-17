package com.edutrack.service;

import com.edutrack.TestRunner.Assertion;
import com.edutrack.exception.AuthenticationException;
import com.edutrack.model.User;
import com.edutrack.model.UserRole;

public class AuthServiceTest {

    public void testValidFacultyLogin() throws AuthenticationException {
        AuthService auth = new AuthService();
        User user = auth.login("faculty", "admin123");
        Assertion.assertNotNull(user, "User should not be null after login");
        Assertion.assertEquals(UserRole.FACULTY, user.getRole(), "User role should be FACULTY");
        Assertion.assertTrue(auth.isAuthenticated(), "Auth status should be true");
    }

    public void testInvalidPasswordRejection() {
        AuthService auth = new AuthService();
        boolean caught = false;
        try {
            auth.login("faculty", "wrongpassword");
        } catch (AuthenticationException e) {
            caught = true;
        }
        Assertion.assertTrue(caught, "Should throw AuthenticationException on incorrect password");
    }

    public void testUnknownUserRejection() {
        AuthService auth = new AuthService();
        boolean caught = false;
        try {
            auth.login("non_existent_user", "password");
        } catch (AuthenticationException e) {
            caught = true;
        }
        Assertion.assertTrue(caught, "Should throw AuthenticationException for non-existent username");
    }

    public void testPasswordHashingConsistency() {
        String hash1 = AuthService.hashPassword("testpass");
        String hash2 = AuthService.hashPassword("testpass");
        Assertion.assertEquals(hash1, hash2, "Hashing identical strings should produce identical digests");
    }

    public void testRoleAuthorization() throws AuthenticationException {
        AuthService auth = new AuthService();
        auth.login("admin", "root123");
        Assertion.assertTrue(auth.hasRole(UserRole.ADMIN), "Admin user must have ADMIN role");
        Assertion.assertFalse(auth.hasRole(UserRole.STUDENT), "Admin user must not have STUDENT role");
    }
}
