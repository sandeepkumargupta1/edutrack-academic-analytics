package com.edutrack;

import com.edutrack.util.AnsiColor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Lightweight, self-contained automated unit test runner.
 * Allows executing tests in terminal environments without requiring external JARs.
 */
public class TestRunner {

    public static class Assertion {
        public static void assertTrue(boolean condition, String message) {
            if (!condition) throw new AssertionError(message);
        }

        public static void assertFalse(boolean condition, String message) {
            if (condition) throw new AssertionError(message);
        }

        public static void assertEquals(Object expected, Object actual, String message) {
            if (expected == null && actual == null) return;
            if (expected != null && expected.equals(actual)) return;
            throw new AssertionError(String.format("%s [Expected: %s, Actual: %s]", message, expected, actual));
        }

        public static void assertEquals(double expected, double actual, double delta, String message) {
            if (Math.abs(expected - actual) > delta) {
                throw new AssertionError(String.format("%s [Expected: %.4f, Actual: %.4f, Delta: %.4f]", message, expected, actual, delta));
            }
        }

        public static void assertNotNull(Object obj, String message) {
            if (obj == null) throw new AssertionError(message);
        }
    }

    public static void main(String[] args) {
        System.out.println(AnsiColor.bold("\n================================================================================"));
        System.out.println(AnsiColor.bold("                   EDUTRACK AUTOMATED UNIT TEST RUNNER"));
        System.out.println(AnsiColor.bold("================================================================================"));

        List<Class<?>> testClasses = new ArrayList<>();
        testClasses.add(com.edutrack.service.AuthServiceTest.class);
        testClasses.add(com.edutrack.service.StudentServiceTest.class);
        testClasses.add(com.edutrack.service.AnalyticsEngineTest.class);
        testClasses.add(com.edutrack.service.InterventionServiceTest.class);
        testClasses.add(com.edutrack.util.CsvHandlerTest.class);

        int totalTests = 0;
        int passedTests = 0;
        int failedTests = 0;

        for (Class<?> clazz : testClasses) {
            System.out.println("\n" + AnsiColor.cyan("Running: " + clazz.getSimpleName()));
            Method[] methods = clazz.getDeclaredMethods();

            for (Method m : methods) {
                if (m.getName().startsWith("test")) {
                    totalTests++;
                    try {
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        m.invoke(instance);
                        System.out.printf("  ✔ %-45s %s%n", m.getName(), AnsiColor.green("[PASSED]"));
                        passedTests++;
                    } catch (Exception e) {
                        Throwable cause = e.getCause() != null ? e.getCause() : e;
                        System.out.printf("  ✘ %-45s %s%n", m.getName(), AnsiColor.red("[FAILED]"));
                        System.out.println(AnsiColor.red("     -> " + cause.getMessage()));
                        failedTests++;
                    }
                }
            }
        }

        System.out.println(AnsiColor.bold("\n================================================================================"));
        System.out.printf(" TOTAL TESTS: %d | PASSED: %s | FAILED: %s%n",
                totalTests, AnsiColor.green(String.valueOf(passedTests)),
                failedTests > 0 ? AnsiColor.red(String.valueOf(failedTests)) : "0");
        System.out.println(AnsiColor.bold("================================================================================"));

        if (failedTests > 0) {
            System.exit(1);
        } else {
            System.out.println(AnsiColor.green("ALL TESTS PASSED SUCCESSFULLY! (100% Pass Rate)\n"));
            System.exit(0);
        }
    }
}
