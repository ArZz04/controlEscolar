package com.arzz.cescolar.controlescolar.wControllers;

import com.arzz.cescolar.controlescolar.dao.GroupDAO;
import com.arzz.cescolar.controlescolar.dao.StudentDAO;
import com.arzz.cescolar.controlescolar.dao.SubjectDAO;
import com.arzz.cescolar.controlescolar.schemas.Group;
import com.arzz.cescolar.controlescolar.schemas.Student;
import com.arzz.cescolar.controlescolar.schemas.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class studentFController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private ComboBox<String> semesterComboBox;
    @FXML private ComboBox<String > groupsComboBox;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        // Initialize ComboBox values
        genderComboBox.getItems().addAll("Male", "Female", "Other", "Not specified");

        semesterComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

        loadGroupsToComboBox();

        // Set default values
        birthDatePicker.setValue(LocalDate.now().minusYears(18));
    }

    private void loadGroupsToComboBox() {
        GroupDAO groupDAO = new GroupDAO(); // Instancia del DAO
        ObservableList<String> groupNames = FXCollections.observableArrayList();

        try {
            List<Group> groups = groupDAO.getAllGroups(); // Obtener grupos desde la BD

            if (groups.isEmpty()) {
                groupNames.add("No hay grupos disponibles");
            } else {
                for (Group group : groups) { // Recorrer la lista de grupos
                    groupNames.add(group.getGroupName()); // Usar getGroupName() para obtener el nombre del grupo
                }
            }
        } catch (Exception e) {
            groupNames.add("⚠ Error al cargar grupos");
            System.err.println("Error al obtener grupos: " + e.getMessage());
            e.printStackTrace();
        }

        groupsComboBox.setItems(groupNames); // Asignar los datos al ComboBox
    }

    @FXML
    private void handleSave() {
        // Validar campos obligatorios
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
            showAlert("Error", "Campos requeridos", "Por favor complete todos los campos obligatorios (Nombre y Apellido).");
            return;
        }

        if (semesterComboBox.getSelectionModel().isEmpty() || groupsComboBox.getSelectionModel().isEmpty()) {
            showAlert("Error", "Campos requeridos", "Por favor complete todos los campos obligatorios (Semestre y Grupo).");
            return;
        }

        if (emailField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            showAlert("Error", "Campos requeridos", "Por favor complete todos los campos obligatorios (Correo y Teléfono).");
            return;
        }

        // Obtener el género
        String gender = genderComboBox.getSelectionModel().getSelectedItem();
        if (gender == null) {
            showAlert("Error", "Género no seleccionado", "Por favor seleccione un género.");
            return;
        }

        // Verificar la fecha de nacimiento
        if (birthDatePicker.getValue() == null) {
            showAlert("Error", "Fecha no seleccionada", "Por favor seleccione una fecha de nacimiento.");
            return;
        }

        // Intentar parsear el semestre
        Integer semester = null;
        try {
            semester = Integer.parseInt(semesterComboBox.getValue());
        } catch (NumberFormatException e) {
            showAlert("Error", "Semestre no válido", "Por favor seleccione un semestre válido.");
            return;
        }

        // Crear el objeto Student con los datos del formulario
        Student student = new Student(
                firstNameField.getText(),
                lastNameField.getText(),
                birthDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),  // Ajuste aquí
                gender,
                emailField.getText(),
                phoneField.getText(),
                0,  // Este es un valor placeholder para studentId, cambia según tu lógica
                Integer.parseInt(semesterComboBox.getValue()),  // Esto es el grade
                groupsComboBox.getSelectionModel().getSelectedIndex() + 1  // Esto es el groupId
        );

        // Llamar al método addStudent de StudentDAO
        StudentDAO studentDAO = new StudentDAO();

        try {
            // Intentar guardar el estudiante
            studentDAO.addStudent(student);

            // Si no hubo excepciones, mostrar mensaje de éxito
            showAlert("Éxito", "Alumno guardado", "El alumno ha sido guardado exitosamente.");
        } catch (Exception e) {
            // Capturar cualquier excepción que ocurra al guardar el estudiante
            showAlert("Error", "Guardar alumno", "Ocurrió un error al guardar el alumno: " + e.getMessage());
            e.printStackTrace();  // Esto es útil para depurar el error
        }
        // Cerrar el formulario
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