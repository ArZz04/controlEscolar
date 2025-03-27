package com.arzz.cescolar.controlescolar;

import com.arzz.cescolar.controlescolar.dao.StudentDAO;
import com.arzz.cescolar.controlescolar.dao.SubjectDAO;
import com.arzz.cescolar.controlescolar.dao.TeacherDAO;
import com.arzz.cescolar.controlescolar.schemas.Student;
import com.arzz.cescolar.controlescolar.schemas.Subject;
import com.arzz.cescolar.controlescolar.schemas.Teacher;
import com.arzz.cescolar.controlescolar.wControllers.studentFController;
import com.arzz.cescolar.controlescolar.wControllers.subjectFController;
import com.arzz.cescolar.controlescolar.wControllers.teacherFController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class AppController {
    @FXML private Button btnAlumnos;
    @FXML private Button btnMaestros;
    @FXML private Button btnMaterias;
    @FXML private AnchorPane sidePanel;
    @FXML private Label sidePanelTitle;
    @FXML private ImageView sidePanelIcon;
    @FXML private TextField searchField;
    @FXML private ListView<String> itemListView;

    @FXML
    private ComboBox<String> userComboBox;

    @FXML
    private VBox materiasList;


    private String currentPanel = null;

    @FXML
    private Button btnAddNew;

    @FXML
    private BorderPane mainPane;

    @FXML
    public void initialize() {

        // Initialize components
        sidePanel.setVisible(false);

        // Set up search field listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterItems(newValue);
        });

        // Configurar evento de btnAddNew
        btnAddNew.setOnAction(event -> {
            switch (currentPanel) {
                case "ALUMNOS" -> openStudentFWindow();
                case "MAESTROS" -> openTeacherFWindow();  // Nuevo método para abrir formulario de maestros
                case "MATERIAS" -> openSubjectFWindow();
            }
        });

        loadAlumnosData();
    }

    @FXML
    private void handleAssignMaterias() {
        String selectedUser = userComboBox.getValue();

        if (selectedUser != null) {
            // Aquí puedes agregar la lógica para asignar materias al usuario seleccionado
            // Por ejemplo, agregamos una nueva materia a la lista de materias asignadas.
            Label newMateriaLabel = new Label("Nueva Materia asignada a " + selectedUser);
            materiasList.getChildren().add(newMateriaLabel);
        } else {
            // Si no se selecciona un usuario, mostrar un mensaje
            System.out.println("Por favor, selecciona un usuario.");
        }
    }


    @FXML
    private void handleAlumnosAction() {
        toggleSidePanel("ALUMNOS");
        if (sidePanel.isVisible() && "ALUMNOS".equals(currentPanel)) {
            loadAlumnosData();
        }
    }

    @FXML
    private void handleMaestrosAction() {
        toggleSidePanel("MAESTROS");
        if (sidePanel.isVisible() && "MAESTROS".equals(currentPanel)) {
            loadMaestrosData();
        }
    }

    @FXML
    private void handleMateriasAction() {
        toggleSidePanel("MATERIAS");
        if (sidePanel.isVisible() && "MATERIAS".equals(currentPanel)) {
            loadMateriasData();
        }
    }

    @FXML
    private void handleCloseSidePanel() {
        sidePanel.setVisible(false);
        currentPanel = null;
    }



    private void toggleSidePanel(String title) {
        if (sidePanel.isVisible() && title.equals(currentPanel)) {
            sidePanel.setVisible(false);
            currentPanel = null;
        } else {
            sidePanel.setVisible(true);
            sidePanelTitle.setText(title);
            currentPanel = title;
        }
    }

    private void loadAlumnosData() {
        ObservableList<String> items = FXCollections.observableArrayList();


        // Crear instancia de StudentsDAO para obtener los estudiantes
        StudentDAO studentsDAO = new StudentDAO();
        List<Student> students = studentsDAO.getAllStudents(); // Obtener estudiantes desde el DAO

        if (students.isEmpty()) {
            items.add("No hay alumnos disponibles.");  // Mostrar mensaje si no hay alumnos
        } else {
            for (Student student : students) {
                // Formato de cada alumno: "Nombre Apellido - Matrícula: [STUDENT_ID] • [GRADE] Semestre"
                String studentData = student.getFirstName() + " " + student.getLastName() +
                        " - ID: " + student.getStudentId() +
                        " • " + student.getGrade() + "er Semestre";
                items.add(studentData);
            }
        }

        // Asignar los items al ListView
        itemListView.setItems(items);
        userComboBox.setItems(items);
    }

    private void loadMaestrosData() {
        // Obtener la lista de maestros desde el DAO
        TeacherDAO teacherDAO = new TeacherDAO();
        List<Teacher> teachers = teacherDAO.getAllTeachers();  // Obtiene todos los maestros desde la base de datos

        // Crear la lista observable para la ListView
        ObservableList<String> items = FXCollections.observableArrayList();

        if (teachers.isEmpty()) {
            items.add("No hay maestros disponibles.");  // Mostrar mensaje si no hay maestros
        } else {
            // Iterar sobre la lista de maestros y agregar las cadenas formateadas a la lista observable
            for (Teacher teacher : teachers) {
                String teacherInfo = String.format("%s %s - Departamento: %s • %s",
                        teacher.getFirstName(),
                        teacher.getLastName(),
                        teacher.getSubjectName(),  // Ahora tienes el nombre del subject disponible
                        teacher.getEmail());

                items.add(teacherInfo);  // Agregar a la lista observable
            }
        }

        // Asignar la lista observable a la ListView
        itemListView.setItems(items);
    }

    private void loadMateriasData() {
        SubjectDAO subjectDAO = new SubjectDAO(); // Instancia del DAO
        ObservableList<String> items = FXCollections.observableArrayList();

        try {
            List<Subject> subjects = subjectDAO.getAllSubjects(); // Obtener materias desde la BD

            if (subjects.isEmpty()) {
                items.add("No hay materias disponibles.");
            } else {
                for (Subject subject : subjects) {
                    // Formato personalizado para cada materia
                    String formattedSubject = subject.getName() + " - Créditos: 5 • Obligatoria"; // Ajusta según tu BD
                    items.add(formattedSubject);
                }
            }
        } catch (Exception e) {
            // Si ocurre un error (ej. no hay conexión), mostrar un mensaje en la lista
            items.add("⚠ Error al conectar con la base de datos.");
            System.err.println("Error al obtener materias: " + e.getMessage()); // Para depuración
            e.printStackTrace();
        }

        itemListView.setItems(items); // Establecer los datos en el ListView
    }

    private void openStudentFWindow() {
        try {
            // Obtener la ventana principal
            Stage primaryStage = (Stage) mainPane.getScene().getWindow();
            primaryStage.hide(); // Ocultar la ventana principal

            // Cargar el formulario del estudiante
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/arzz/cescolar/controlescolar/student-form.fxml"));
            Parent root = loader.load();

            studentFController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Agregar Nuevo Alumno");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            controller.setStage(stage);

            stage.showAndWait(); // Esperar hasta que se cierre el formulario

            // Volver a mostrar la ventana principal después de cerrar el formulario
            primaryStage.show();

            // Refrescar la lista de alumnos si es necesario
            if (currentPanel != null && currentPanel.equals("ALUMNOS")) {
                loadAlumnosData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openTeacherFWindow() {
        try {

            Stage primaryStage = (Stage) mainPane.getScene().getWindow();
            primaryStage.hide(); // Ocultar la ventana principal

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/arzz/cescolar/controlescolar/teacher-form.fxml"));
            Parent root = loader.load();

            // Obtener el controlador
            teacherFController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Agregar Nuevo Docente");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            // Pasar el Stage al controlador
            controller.setStage(stage);

            stage.showAndWait(); // Esperar hasta que se cierre el formulario

            // Volver a mostrar la ventana principal después de cerrar el formulario
            primaryStage.show();

            // Refrescar la lista de alumnos si es necesario
            if (currentPanel != null && currentPanel.equals("MAESTROS")) {
                loadMaestrosData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void openSubjectFWindow() {
        try {
            // Obtener la ventana principal
            Stage primaryStage = (Stage) mainPane.getScene().getWindow();
            primaryStage.hide(); // Ocultar la ventana principal

            // Cargar el formulario del estudiante
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/arzz/cescolar/controlescolar/subject-form.fxml"));
            Parent root = loader.load();

            subjectFController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Agregar Nueva Materia");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            controller.setStage(stage);

            stage.showAndWait(); // Esperar hasta que se cierre el formulario

            // Volver a mostrar la ventana principal después de cerrar el formulario
            primaryStage.show();

            // Refrescar la lista de alumnos si es necesario
            if (currentPanel != null && currentPanel.equals("SUBJECTS")) {
                loadMateriasData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterItems(String searchText) {
        if (currentPanel == null) return;

        ObservableList<String> filteredItems = FXCollections.observableArrayList();

        // Get the original list based on current panel
        ObservableList<String> originalItems = FXCollections.observableArrayList();
        switch (currentPanel) {
            case "ALUMNOS":
                loadAlumnosData();
                originalItems = itemListView.getItems();
                break;
            case "MAESTROS":
                loadMaestrosData();
                originalItems = itemListView.getItems();
                break;
            case "MATERIAS":
                loadMateriasData();
                originalItems = itemListView.getItems();
                break;
        }

        // Filter items
        if (searchText == null || searchText.isEmpty()) {
            filteredItems = originalItems;
        } else {
            for (String item : originalItems) {
                if (item.toLowerCase().contains(searchText.toLowerCase())) {
                    filteredItems.add(item);
                }
            }
        }

        itemListView.setItems(filteredItems);
    }
}