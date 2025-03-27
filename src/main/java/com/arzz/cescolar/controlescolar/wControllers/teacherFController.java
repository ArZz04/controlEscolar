package com.arzz.cescolar.controlescolar.wControllers;

import com.arzz.cescolar.controlescolar.dao.SubjectDAO;
import com.arzz.cescolar.controlescolar.dao.TeacherDAO;
import com.arzz.cescolar.controlescolar.schemas.Subject;
import com.arzz.cescolar.controlescolar.schemas.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class teacherFController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField matriculaField;
    @FXML private ComboBox<String> programComboBox;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        // Inicializar valores en genderComboBox
        genderComboBox.getItems().addAll("Male", "Female", "Other", "Not specified");

        // Llenar programComboBox con materias desde la base de datos
        loadSubjectsToComboBox();

        // Establecer valor predeterminado para birthDatePicker (18 años atrás)
        birthDatePicker.setValue(LocalDate.now().minusYears(18));
    }

    private void loadSubjectsToComboBox() {
        SubjectDAO subjectDAO = new SubjectDAO(); // Instancia del DAO
        ObservableList<String> subjectNames = FXCollections.observableArrayList();

        try {
            List<Subject> subjects = subjectDAO.getAllSubjects(); // Obtener materias desde la BD

            if (subjects.isEmpty()) {
                subjectNames.add("No hay materias disponibles");
            } else {
                for (Subject subject : subjects) {
                    subjectNames.add(subject.getName()); // Agregar solo el nombre de la materia
                }
            }
        } catch (Exception e) {
            subjectNames.add("⚠ Error al cargar materias");
            System.err.println("Error al obtener materias: " + e.getMessage());
            e.printStackTrace();
        }

        programComboBox.setItems(subjectNames); // Asignar los datos al ComboBox
    }

    @FXML
    private void handleSave() {
        // Validar campos obligatorios
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                matriculaField.getText().isEmpty() || emailField.getText().isEmpty() ||
                phoneField.getText().isEmpty() || programComboBox.getSelectionModel().isEmpty()) {
            showAlert("Error", "Campos requeridos", "Por favor complete todos los campos obligatorios.");
            return;
        }

        // Convertir la fecha seleccionada en DatePicker a String en formato "DD/MM/AAAA"
        LocalDate birthDate = birthDatePicker.getValue();
        if (birthDate == null) {
            showAlert("Error", "Fecha de nacimiento requerida", "Por favor, ingrese la fecha de nacimiento.");
            return; // Salir si la fecha no está proporcionada
        }
        String birthDateString = birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Obtener el nombre de la materia seleccionada del ComboBox
        String selectedSubjectName = programComboBox.getSelectionModel().getSelectedItem();

        // Obtener el Subject correspondiente de la base de datos (usando el nombre de la materia)
        SubjectDAO subjectDAO = new SubjectDAO();
        Subject selectedSubject = subjectDAO.getAllSubjects().stream()
                .filter(subject -> subject.getName().equals(selectedSubjectName))
                .findFirst()
                .orElse(null);  // Devuelve null si no encuentra la materia

        if (selectedSubject == null) {
            showAlert("Error", "Materia no encontrada", "La materia seleccionada no existe.");
            return;
        }

        // Obtener el género, suponiendo que se tiene un campo de selección de género
        String gender = genderComboBox.getSelectionModel().getSelectedItem();
        if (gender == null) {
            showAlert("Error", "Género no seleccionado", "Por favor seleccione un género.");
            return;
        }

        // Crear objeto Teacher con los datos del formulario
        Teacher teacher = new Teacher(
                firstNameField.getText(),
                lastNameField.getText(),
                birthDateString,  // Puede ser un DatePicker convertido a String
                gender,  // Género seleccionado
                emailField.getText(),
                phoneField.getText(),
                selectedSubject.getSubjectId()  // ID de la materia seleccionada
        );

        TeacherDAO teacherDAO = new TeacherDAO(); // Crear DAO

        try {
            teacherDAO.addTeacher(teacher);
            showAlert("Éxito", "Docente guardado", "El docente ha sido guardado exitosamente.");
            stage.close(); // Cerrar el formulario después de guardar
        } catch (Exception e) {
            showAlert("Error", "Error al guardar", "Ocurrió un error al guardar el docente: " + e.getMessage());
            e.printStackTrace(); // Mostrar detalles del error en la consola
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