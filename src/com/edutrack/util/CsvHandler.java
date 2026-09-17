package com.edutrack.util;

import com.edutrack.model.AcademicRecord;
import com.edutrack.model.Student;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility for parsing and serializing student and academic data from/to CSV files.
 */
public class CsvHandler {

    private static final String CSV_HEADER = "reg_number,name,email,department,semester,cgpa,mentor_name," +
            "course_code,internal_test_1,internal_test_2,assignment_score,lab_score,quiz_score," +
            "attendance_percentage,study_hours_per_week,backlogs_count";

    public static List<Student> loadStudents(File file) throws IOException {
        List<Student> students = new ArrayList<>();
        if (!file.exists()) {
            return students;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // Header
            if (line == null) return students;

            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] tokens = line.split(",", -1);
                if (tokens.length < 7) {
                    System.err.printf("Warning: Skipping malformed line %d: %s%n", lineNum, line);
                    continue;
                }

                try {
                    String reg = tokens[0].trim();
                    String name = tokens[1].trim();
                    String email = tokens[2].trim();
                    String dept = tokens[3].trim();
                    int sem = Integer.parseInt(tokens[4].trim());
                    double cgpa = Double.parseDouble(tokens[5].trim());
                    String mentor = tokens[6].trim();

                    Student student = new Student(reg, name, email, dept, sem, cgpa, mentor);

                    if (tokens.length >= 16 && !tokens[7].trim().isEmpty()) {
                        String course = tokens[7].trim();
                        double it1 = Double.parseDouble(tokens[8].trim());
                        double it2 = Double.parseDouble(tokens[9].trim());
                        double asgn = Double.parseDouble(tokens[10].trim());
                        double lab = Double.parseDouble(tokens[11].trim());
                        double quiz = Double.parseDouble(tokens[12].trim());
                        double att = Double.parseDouble(tokens[13].trim());
                        double hours = Double.parseDouble(tokens[14].trim());
                        int backlogs = Integer.parseInt(tokens[15].trim());

                        AcademicRecord record = new AcademicRecord(course, it1, it2, asgn, lab, quiz, att, hours, backlogs);
                        student.setAcademicRecord(record);
                    }

                    students.add(student);
                } catch (Exception e) {
                    System.err.printf("Warning: Error parsing line %d: %s (%s)%n", lineNum, line, e.getMessage());
                }
            }
        }
        return students;
    }

    public static void saveStudents(File file, List<Student> students) throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(CSV_HEADER);
            writer.newLine();

            for (Student s : students) {
                AcademicRecord r = s.getAcademicRecord();
                String line;
                if (r != null) {
                    line = String.format("%s,%s,%s,%s,%d,%.2f,%s,%s,%.1f,%.1f,%.1f,%.1f,%.1f,%.1f,%.1f,%d",
                            s.getRegNumber(), s.getName(), s.getEmail(), s.getDepartment(), s.getSemester(), s.getCgpa(), s.getMentorName(),
                            r.getCourseCode(), r.getInternalTest1(), r.getInternalTest2(), r.getAssignmentScore(),
                            r.getLabScore(), r.getQuizScore(), r.getAttendancePercentage(), r.getStudyHoursPerWeek(), r.getBacklogsCount());
                } else {
                    line = String.format("%s,%s,%s,%s,%d,%.2f,%s,,,,,,,,,",
                            s.getRegNumber(), s.getName(), s.getEmail(), s.getDepartment(), s.getSemester(), s.getCgpa(), s.getMentorName());
                }
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
