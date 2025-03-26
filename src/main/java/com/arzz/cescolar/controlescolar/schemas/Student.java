package com.arzz.cescolar.controlescolar.schemas;

public class Student extends User {
    private int studentId;
    private int groupId;

    // Constructor
    public Student(int userId, String firstName, String lastName, String middleName, String birthDate, String email, String phone, int studentId, int groupId) {
        super(userId, firstName, lastName, middleName, birthDate, email, phone);
        this.studentId = studentId;
        this.groupId = groupId;
    }

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
