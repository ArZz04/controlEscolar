package com.arzz.cescolar.controlescolar.wControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalDate;

public class subjectFController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField matriculaField;
    @FXML private ComboBox<String> semesterComboBox;
    @FXML private ComboBox<String> programComboBox;
    @FXML private TextField groupField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField streetField;
    @FXML private TextField neighborhoodField;
    @FXML private TextField zipCodeField;
    @FXML private TextField cityField;
    @FXML private ComboBox<String> stateComboBox;
    @FXML private TextArea notesTextArea;
    @FXML private ImageView studentPhotoView;

    private File selectedPhotoFile;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        // Initialize ComboBox values
        genderComboBox.getItems().addAll("Masculino", "Femenino", "Otro", "Prefiero no decir");

        semesterComboBox.getItems().addAll("1er Semestre", "2do Semestre", "3er Semestre",
                "4to Semestre", "5to Semestre", "6to Semestre",
                "7mo Semestre", "8vo Semestre", "9no Semestre");

        programComboBox.getItems().addAll("Ingeniería en Sistemas", "Ingeniería Civil",
                "Administración de Empresas", "Contaduría",
                "Medicina", "Derecho", "Psicología");

        stateComboBox.getItems().addAll("Aguascalientes", "Baja California", "Baja California Sur",
                "Campeche", "Chiapas", "Chihuahua", "Coahuila",
                "Colima", "Durango", "Estado de México", "Guanajuato",
                "Guerrero", "Hidalgo", "Jalisco", "Michoacán",
                "Morelos", "Nayarit", "Nuevo León", "Oaxaca",
                "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí",
                "Sinaloa", "Sonora",  "Quintana Roo", "San Luis Potosí",
                "Sinaloa", "Sonora", "Tabasco", "Tamaulipas",
                "Tlaxcala", "Veracruz", "Yucatán", "Zacatecas");

        // Set default values
        birthDatePicker.setValue(LocalDate.now().minusYears(18));
    }

    @FXML
    private void handleUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Foto");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        selectedPhotoFile = fileChooser.showOpenDialog(stage);
        if (selectedPhotoFile != null) {
            Image image = new Image(selectedPhotoFile.toURI().toString());
            studentPhotoView.setImage(image);
        }
    }

    @FXML
    private void handleSave() {
        // Validate required fields
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                matriculaField.getText().isEmpty()) {
            showAlert("Error", "Campos requeridos",
                    "Por favor complete todos los campos obligatorios.");
            return;
        }

        // Here you would save the student data to your database
        // For demonstration, we'll just show a success message
        showAlert("Éxito", "Alumno guardado",
                "El alumno ha sido guardado exitosamente.");

        // Close the form
        stage.close();
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