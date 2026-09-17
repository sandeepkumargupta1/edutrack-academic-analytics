package com.edutrack.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility for formatting data lists into structured, bordered ASCII CLI tables.
 */
public class TableRenderer {

    public static String renderTable(List<String> headers, List<List<String>> rows) {
        if (headers == null || headers.isEmpty()) {
            return "";
        }

        int colCount = headers.size();
        int[] colWidths = new int[colCount];

        for (int i = 0; i < colCount; i++) {
            colWidths[i] = stripAnsi(headers.get(i)).length();
        }

        if (rows != null) {
            for (List<String> row : rows) {
                for (int i = 0; i < colCount && i < row.size(); i++) {
                    String cell = row.get(i);
                    int len = (cell != null) ? stripAnsi(cell).length() : 4;
                    if (len > colWidths[i]) {
                        colWidths[i] = len;
                    }
                }
            }
        }

        // Add 2 padding spaces
        for (int i = 0; i < colCount; i++) {
            colWidths[i] += 2;
        }

        StringBuilder sb = new StringBuilder();
        String separator = buildSeparator(colWidths);

        sb.append(separator).append("\n");

        // Header Row
        sb.append("|");
        for (int i = 0; i < colCount; i++) {
            sb.append(padCenter(headers.get(i), colWidths[i])).append("|");
        }
        sb.append("\n");
        sb.append(separator).append("\n");

        // Data Rows
        if (rows != null && !rows.isEmpty()) {
            for (List<String> row : rows) {
                sb.append("|");
                for (int i = 0; i < colCount; i++) {
                    String val = (i < row.size() && row.get(i) != null) ? row.get(i) : "-";
                    sb.append(padLeftRight(val, colWidths[i])).append("|");
                }
                sb.append("\n");
            }
        } else {
            sb.append("|").append(padCenter("No records found", totalWidth(colWidths))).append("|\n");
        }

        sb.append(separator).append("\n");
        return sb.toString();
    }

    private static String buildSeparator(int[] colWidths) {
        StringBuilder sb = new StringBuilder("+");
        for (int w : colWidths) {
            for (int i = 0; i < w; i++) sb.append("-");
            sb.append("+");
        }
        return sb.toString();
    }

    private static String padCenter(String text, int width) {
        int visibleLen = stripAnsi(text).length();
        int pad = width - visibleLen;
        if (pad <= 0) return text;
        int left = pad / 2;
        int right = pad - left;
        return " ".repeat(left) + text + " ".repeat(right);
    }

    private static String padLeftRight(String text, int width) {
        int visibleLen = stripAnsi(text).length();
        int pad = width - visibleLen;
        if (pad <= 0) return text;
        return " " + text + " ".repeat(pad - 1);
    }

    private static int totalWidth(int[] colWidths) {
        int t = colWidths.length - 1;
        for (int w : colWidths) t += w;
        return t;
    }

    public static String stripAnsi(String input) {
        if (input == null) return "";
        return input.replaceAll("\u001B\\[[;\\d]*m", "");
    }
}
