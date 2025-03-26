package com.arzz.cescolar.controlescolar.dao;

import com.arzz.cescolar.controlescolar.db.ConnectionDB;
import com.arzz.cescolar.controlescolar.schemas.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    // Obtener todas las materias
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String query = "SELECT * FROM subjects";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int subjectId = resultSet.getInt("id_subject");
                String name = resultSet.getString("subject_name");

                Subject subject = new Subject(subjectId, name);
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subjects;
    }

    // Insertar una nueva materia
    public void addSubject(Subject subject) {
        String query = "INSERT INTO subjects (subject_name) VALUES (?)";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, subject.getName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Actualizar una materia
    public void updateSubject(Subject subject) {
        String query = "UPDATE subjects SET subject_name = ? WHERE id_subject = ?";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, subject.getName());
            preparedStatement.setInt(2, subject.getSubjectId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Eliminar una materia
    public void deleteSubject(int subjectId) {
        String query = "DELETE FROM subjects WHERE id_subject = ?";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, subjectId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
