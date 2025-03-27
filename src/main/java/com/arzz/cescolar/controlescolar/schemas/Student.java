package com.arzz.cescolar.controlescolar.schemas;

public class Student extends User {
    private int studentId;
    private int groupId;
    private int grade;

    // Constructor
    public Student( String firstName, String lastName, String birthDate, String gender, String email, String phone, int studentId, int grade, int groupId) {
        super( firstName, lastName, birthDate, gender, email, phone);
        this.studentId = studentId;
        this.grade = grade;
        this.groupId = groupId;
    }

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
