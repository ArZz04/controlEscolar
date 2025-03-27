package com.arzz.cescolar.controlescolar.wControllers;

import com.arzz.cescolar.controlescolar.dao.SubjectDAO;
import com.arzz.cescolar.controlescolar.schemas.Subject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class subjectFController {

    @FXML private TextField subjectName;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleSave() {
        // Validar campos obligatorios
        if (subjectName.getText().isEmpty()) {
            showAlert("Error", "Campos requeridos", "Por favor complete todos los campos obligatorios.");
            return;
        }

        // Crear la materia
        Subject subject = new Subject(subjectName.getText());
        SubjectDAO subjectDAO = new SubjectDAO();

        try {
            // Guardar la materia en la base de datos
            subjectDAO.addSubject(subject);
            showAlert("Éxito", "Materia guardada", "La materia ha sido guardada exitosamente.");

            // Cerrar el formulario
            stage.close();
        } catch (Exception e) {
            showAlert("Error", "Error al guardar", "Ocurrió un error al guardar la materia: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}