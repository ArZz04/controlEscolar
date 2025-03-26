package com.arzz.cescolar.controlescolar.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    // URL, usuario y contraseña de la base de datos
    private static final String URL = "jdbc:mysql://10.147.17.135:2005/ControlEscolar";
    private static final String USER = "arzzDev";
    private static final String PASSWORD = "SKYjuan.1KT2507";

    // Variable para almacenar la conexión (Singleton)
    private static Connection connection = null;

    // Constructor privado para evitar instancias
    private ConnectionDB() {}

    // Método para obtener la conexión
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Cargar el driver de MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Crear la conexión
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully!");
            } catch (ClassNotFoundException e) {
                System.out.println("MySQL JDBC Driver not found!");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Connection failed!");
                e.printStackTrace();
                throw e; // Re-throw para que sea manejado por el DAO
            }
        }
        return connection;
    }

    // Método para cerrar la conexión (opcional, en caso de que necesites cerrarla manualmente)
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing the database connection.");
                e.printStackTrace();
            }
        }
    }
}