package com.arzz.cescolar.controlescolar.dao;

import com.arzz.cescolar.controlescolar.db.ConnectionDB;
import com.arzz.cescolar.controlescolar.schemas.Student;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Obtener todos los estudiantes
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        // Consulta con JOIN para obtener los detalles del estudiante, incluyendo el nombre del grupo y el grade
        String query = "SELECT u.FIRST_NAME, u.LAST_NAME, u.GENDER, u.EMAIL, u.PHONE, u.BIRTH_DATE, s.STUDENT_ID, s.GRADE, s.GROUP_ID, g.GROUP_NAME " +
                "FROM User u " +
                "JOIN Students s ON u.USER_ID = s.USER_ID " +  // Relacionar con la tabla de estudiantes
                "JOIN `Groups_Table` g ON s.GROUP_ID = g.GROUP_ID";  // Relacionar con la tabla de grupos (usar `Groups` con comillas invertidas)

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
                int studentId = resultSet.getInt("STUDENT_ID"); // Obtener studentId
                int grade = resultSet.getInt("GRADE"); // Obtener grade
                int groupId = resultSet.getInt("GROUP_ID"); // Obtener groupId

                // Crear el objeto Student con todos los parámetros necesarios
                Student student = new Student(firstName, lastName, birthDate, gender, email, phone, studentId, grade, groupId);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Insertar un nuevo estudiante
    public void addStudent(Student student) {

        String insertUserQuery = "INSERT INTO User (FIRST_NAME, LAST_NAME, GENDER, EMAIL, PHONE, BIRTH_DATE) VALUES (?, ?, ?, ?, ?, ?)";

        String gender = student.getGender();
        if (!(gender.equals("Male") || gender.equals("Female") || gender.equals("Other") || gender.equals("Not specified"))) {
            throw new IllegalArgumentException("Invalid gender value: " + gender);
        }

        // Convertir birthDate (String) a LocalDate
        LocalDate birthDate = LocalDate.parse(student.getBirthDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Date sqlBirthDate = Date.valueOf(birthDate);

        int userId = -1; // Variable para almacenar el userId

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getGender());
            preparedStatement.setString(4, student.getEmail());
            preparedStatement.setString(5, student.getPhone());
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

            // Insertar en la tabla Student con el userId, grade y groupId
            String insertStudentQuery = "INSERT INTO Students (USER_ID, GRADE, GROUP_ID) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatementStudent = connection.prepareStatement(insertStudentQuery)) {
                preparedStatementStudent.setInt(1, userId);  // Asignar el userId
                preparedStatementStudent.setInt(2, student.getGrade());  // Asignar el grade del estudiante
                preparedStatementStudent.setInt(3, student.getGroupId());  // Asignar el groupId del estudiante
                preparedStatementStudent.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Actualizar un estudiante
    public void updateStudent(Student student) {
        String query = "UPDATE students SET first_name = ?, last_name = ?, birth_date = ?, gender = ? , email = ?, phone = ?, id_group = ? WHERE id_student = ?";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(4, student.getBirthDate());
            preparedStatement.setString(4, student.getGender());
            preparedStatement.setString(5, student.getEmail());
            preparedStatement.setString(6, student.getPhone());
            preparedStatement.setInt(7, student.getGroupId());
            preparedStatement.setInt(8, student.getStudentId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Eliminar un estudiante
    public void deleteStudent(int studentId) {
        String query = "DELETE FROM Students WHERE id_student = ?";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, studentId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

