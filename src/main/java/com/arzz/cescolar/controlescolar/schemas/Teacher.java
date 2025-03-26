package com.arzz.cescolar.controlescolar.schemas;

public class Teacher extends User {
    private int teacherId;
    private int subjectId;

    // Constructor
    public Teacher(int userId, String firstName, String lastName, String middleName, String birthDate, String email, String phone, int teacherId, int subjectId) {
        super(userId, firstName, lastName, middleName, birthDate, email, phone);
        this.teacherId = teacherId;
        this.subjectId = subjectId;
    }

    // Getters and Setters
    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}

