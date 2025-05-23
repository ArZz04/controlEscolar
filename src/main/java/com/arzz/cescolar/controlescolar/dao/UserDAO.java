package com.arzz.cescolar.controlescolar.dao;

import com.arzz.cescolar.controlescolar.db.ConnectionDB;
import com.arzz.cescolar.controlescolar.schemas.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Método para obtener todos los usuarios
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String middleName = resultSet.getString("middle_name");
                String birthDate = resultSet.getString("birth_date");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");

                // Crear un nuevo objeto User y agregarlo a la lista
                User user = new User( firstName, lastName, middleName, birthDate, email, phone);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public int addUser(User user) {
        String query = "INSERT INTO User (FIRST_NAME, LAST_NAME, GENDER, EMAIL, PHONE) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getGender());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPhone());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);  // Retorna el userId generado
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Retorna -1 si no se pudo generar el userId
    }



    // Otros métodos CRUD (Insert, Update, Delete) irían aquí
}
