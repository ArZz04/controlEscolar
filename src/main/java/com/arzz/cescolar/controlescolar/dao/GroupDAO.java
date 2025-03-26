package com.arzz.cescolar.controlescolar.dao;

import com.arzz.cescolar.controlescolar.db.ConnectionDB;
import com.arzz.cescolar.controlescolar.schemas.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {

    // Obtener todos los grupos
    public List<Group> getAllGroups() {
        List<Group> groups = new ArrayList<>();
        String query = "SELECT * FROM groups";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int groupId = resultSet.getInt("id_group");
                String groupName = resultSet.getString("group_name");

                Group group = new Group(groupId, groupName);
                groups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groups;
    }

    // Insertar un nuevo grupo
    public void addGroup(Group group) {
        String query = "INSERT INTO groups (group_name) VALUES (?)";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, group.getGroupName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Actualizar un grupo
    public void updateGroup(Group group) {
        String query = "UPDATE groups SET group_name = ? WHERE id_group = ?";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, group.getGroupName());
            preparedStatement.setInt(2, group.getGroupId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Eliminar un grupo
    public void deleteGroup(int groupId) {
        String query = "DELETE FROM groups WHERE id_group = ?";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, groupId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
