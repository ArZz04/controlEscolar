package com.arzz.cescolar.controlescolar.schemas;

public class Teacher extends User {
    private int teacherId;
    private int subjectId;
    private String subjectName;  // Si necesitas guardar el nombre del subject.

    // Constructor sin teacherId porque se asignará después en la base de datos
    public Teacher(String firstName, String lastName, String birthDate, String gender, String email, String phone, int subjectId) {
        // Llamada al constructor de la clase padre (User)
        super(firstName, lastName, birthDate, gender, email, phone);
        this.subjectId = subjectId;
    }

    public Teacher(String firstName, String lastName, String birthDate, String gender, String email, String phone, int subjectId, String subjectName) {
        super(firstName, lastName, birthDate, gender, email, phone);
        this.subjectId = subjectId;
        this.subjectName = subjectName;  // Si necesitas guardar el nombre del subject.
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
