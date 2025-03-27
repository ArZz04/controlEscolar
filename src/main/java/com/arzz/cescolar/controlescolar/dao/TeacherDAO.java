package com.arzz.cescolar.controlescolar.dao;

import com.arzz.cescolar.controlescolar.db.ConnectionDB;
import com.arzz.cescolar.controlescolar.schemas.Teacher;
import com.arzz.cescolar.controlescolar.schemas.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {

    // Obtener todos los maestros
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        String query = "SELECT u.FIRST_NAME, u.LAST_NAME, u.GENDER, u.EMAIL, u.PHONE, u.BIRTH_DATE, t.SUBJECT_ID, s.NAME " +
                "FROM User u " +
                "JOIN Teachers t ON u.USER_ID = t.USER_ID " +
                "JOIN Subjects s ON t.SUBJECT_ID = s.SUBJECT_ID"; // Asegúrate de que el JOIN esté correcto según tu modelo de datos

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String firstName = resultSet.getString("FIRST_NAME");
                String lastName = resultSet.getString("LAST_NAME");
                String gender = resultSet.getString("GENDER");
                String email = resultSet.getString("EMAIL");
                String phone = resultSet.getString("PHONE");
                String birthDate = resultSet.getString("BIRTH_DATE");
                int subjectId = resultSet.getInt("SUBJECT_ID");
                String subjectName = resultSet.getString("NAME");

                // Crear el objeto Teacher con todos los parámetros necesarios
                Teacher teacher = new Teacher(firstName, lastName, birthDate, gender, email, phone, subjectId, subjectName);
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teachers;
    }



    // Insertar un nuevo maestro
    public void addTeacher(Teacher teacher) {
        // Insertar en la tabla User primero
        String insertUserQuery = "INSERT INTO User (FIRST_NAME, LAST_NAME, GENDER, EMAIL, PHONE, BIRTH_DATE) VALUES (?, ?, ?, ?, ?, ?)";

        String gender = teacher.getGender();
        if (!(gender.equals("Male") || gender.equals("Female") || gender.equals("Other") || gender.equals("Not specified"))) {
            throw new IllegalArgumentException("Invalid gender value: " + gender);
        }

        // Convertir birthDate (String) a LocalDate
        LocalDate birthDate = LocalDate.parse(teacher.getBirthDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Date sqlBirthDate = Date.valueOf(birthDate);

        int userId = -1; // Variable para almacenar el userId

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, teacher.getFirstName());
            preparedStatement.setString(2, teacher.getLastName());
            preparedStatement.setString(3, teacher.getGender());
            preparedStatement.setString(4, teacher.getEmail());
            preparedStatement.setString(5, teacher.getPhone());
            preparedStatement.setDate(6, sqlBirthDate);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                // Obtener el userId generado automáticamente
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1); // Asignar el userId generado por la base de datos
                    }
                }
            }

            if (userId == -1) {
                throw new SQLException("Failed to obtain user ID for new teacher.");
            }

            // Insertar en la tabla Teachers con el userId y subjectId
            String insertTeacherQuery = "INSERT INTO Teachers (USER_ID, SUBJECT_ID) VALUES (?, ?)";
            try (PreparedStatement preparedStatementTeacher = connection.prepareStatement(insertTeacherQuery)) {
                preparedStatementTeacher.setInt(1, userId);  // Asignar el userId
                preparedStatementTeacher.setInt(2, teacher.getSubjectId());  // Asignar el subjectId
                preparedStatementTeacher.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Actualizar un maestro
    public void updateTeacher(Teacher teacher) {
        String query = "UPDATE teachers SET FIRST_NAME = ?, LAST_NAME = ?, GENDER = ?, BIRTH_DATE = ?, EMAIL = ?, PHONE = ?, SUBJECT_ID = ? WHERE ID_TEACHER = ?";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, teacher.getFirstName());  // Asignar primer nombre
            preparedStatement.setString(2, teacher.getLastName());   // Asignar apellido
            preparedStatement.setString(3, teacher.getGender());     // Asignar género
            preparedStatement.setString(4, teacher.getBirthDate());  // Asignar fecha de nacimiento
            preparedStatement.setString(5, teacher.getEmail());      // Asignar correo electrónico
            preparedStatement.setString(6, teacher.getPhone());      // Asignar número de teléfono
            preparedStatement.setInt(7, teacher.getSubjectId());     // Asignar ID de materia
            preparedStatement.setInt(8, teacher.getTeacherId());     // Asignar ID del maestro para actualizar

            preparedStatement.executeUpdate();  // Ejecutar la actualización

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
