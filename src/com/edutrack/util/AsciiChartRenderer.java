package com.edutrack.util;

import java.util.Map;

/**
 * Utility for rendering ASCII visual charts, histograms, and meter bars directly in the terminal.
 */
public class AsciiChartRenderer {

    public static String renderBarChart(String title, Map<String, Integer> data, int maxBarWidth) {
        StringBuilder sb = new StringBuilder();
        sb.append(AnsiColor.bold(title)).append("\n");
        sb.append("-".repeat(50)).append("\n");

        int maxVal = data.values().stream().max(Integer::compareTo).orElse(1);
        if (maxVal == 0) maxVal = 1;

        int maxLabelLen = data.keySet().stream().mapToInt(String::length).max().orElse(10);

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            String label = entry.getKey();
            int val = entry.getValue();
            int barLen = (int) Math.round(((double) val / maxVal) * maxBarWidth);
            if (barLen == 0 && val > 0) barLen = 1;

            String barBlock = "█".repeat(barLen);
            String coloredBar;
            if (label.toLowerCase().contains("high")) {
                coloredBar = AnsiColor.red(barBlock);
            } else if (label.toLowerCase().contains("moderate")) {
                coloredBar = AnsiColor.yellow(barBlock);
            } else if (label.toLowerCase().contains("low")) {
                coloredBar = AnsiColor.green(barBlock);
            } else {
                coloredBar = AnsiColor.cyan(barBlock);
            }

            String paddedLabel = String.format("%-" + maxLabelLen + "s", label);
            sb.append(String.format(" %s | %s %d\n", paddedLabel, coloredBar, val));
        }
        sb.append("-".repeat(50)).append("\n");
        return sb.toString();
    }

    public static String renderRiskGauge(double failureProbability) {
        int totalBlocks = 20;
        int filled = (int) Math.round(failureProbability * totalBlocks);
        int empty = totalBlocks - filled;

        String color = (failureProbability > 0.65) ? AnsiColor.RED
                : (failureProbability > 0.35) ? AnsiColor.YELLOW : AnsiColor.GREEN;

        return String.format("[%s%s%s] %.1f%%",
                color, "█".repeat(filled) + "░".repeat(empty), AnsiColor.RESET, failureProbability * 100.0);
    }
}
