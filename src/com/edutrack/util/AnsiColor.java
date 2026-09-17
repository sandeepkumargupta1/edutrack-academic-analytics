package com.edutrack.util;

/**
 * Terminal ANSI styling codes for professional CLI presentation.
 */
public class AnsiColor {
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String DIM = "\u001B[2m";

    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";

    public static String green(String text) { return GREEN + text + RESET; }
    public static String red(String text) { return RED + text + RESET; }
    public static String yellow(String text) { return YELLOW + text + RESET; }
    public static String cyan(String text) { return CYAN + text + RESET; }
    public static String bold(String text) { return BOLD + text + RESET; }
    public static String blue(String text) { return BLUE + text + RESET; }
}
