package com.arzz.cescolar.controlescolar.schemas;

public class Subject {
    private int subjectId;
    private String name;

    // Constructor
    public Subject(int subjectId, String name) {
        this.subjectId = subjectId;
        this.name = name;
    }

    // Constructor para nuevas materias (sin ID)
    public Subject(String name) {
        this.name = name;
    }

    // Getters and Setters
    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

