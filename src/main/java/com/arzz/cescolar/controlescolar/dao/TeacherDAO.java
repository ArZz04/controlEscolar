package com.arzz.cescolar.controlescolar.dao;

import com.arzz.cescolar.controlescolar.db.ConnectionDB;
import com.arzz.cescolar.controlescolar.schemas.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {

    // Obtener todos los maestros
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        String query = "SELECT * FROM teachers";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int userId = resultSet.getInt("id_teacher");  // Asumiendo que id_teacher se usa también como userId
                String firstName = resultSet.getString("name");
                String lastName = resultSet.getString("surname");
                String middleName = resultSet.getString("middle_name");
                String birthDate = resultSet.getString("birth_date");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                int teacherId = resultSet.getInt("id_teacher");
                int subjectId = resultSet.getInt("subject_id");

                Teacher teacher = new Teacher(userId, firstName, lastName, middleName, birthDate, email, phone, teacherId, subjectId);
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teachers;
    }

    // Insertar un nuevo maestro
    public void addTeacher(Teacher teacher) {
        String query = "INSERT INTO teachers (name, surname, middle_name, birth_date, email, phone, subject_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, teacher.getFirstName());
            preparedStatement.setString(2, teacher.getLastName());
            preparedStatement.setString(3, teacher.getMiddleName());
            preparedStatement.setString(4, teacher.getBirthDate());
            preparedStatement.setString(5, teacher.getEmail());
            preparedStatement.setString(6, teacher.getPhone());
            preparedStatement.setInt(7, teacher.getSubjectId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Actualizar un maestro
    public void updateTeacher(Teacher teacher) {
        String query = "UPDATE teachers SET name = ?, surname = ?, middle_name = ?, birth_date = ?, email = ?, phone = ?, subject_id = ? WHERE id_teacher = ?";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, teacher.getFirstName());
            preparedStatement.setString(2, teacher.getLastName());
            preparedStatement.setString(3, teacher.getMiddleName());
            preparedStatement.setString(4, teacher.getBirthDate());
            preparedStatement.setString(5, teacher.getEmail());
            preparedStatement.setString(6, teacher.getPhone());
            preparedStatement.setInt(7, teacher.getSubjectId());
            preparedStatement.setInt(8, teacher.getTeacherId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Eliminar un maestro
    public void deleteTeacher(int teacherId) {
        String query = "DELETE FROM teachers WHERE id_teacher = ?";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, teacherId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
