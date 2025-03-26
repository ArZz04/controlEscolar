package com.arzz.cescolar.controlescolar;

import com.arzz.cescolar.controlescolar.wControllers.studentFController;
import com.arzz.cescolar.controlescolar.wControllers.subjectFController;
import com.arzz.cescolar.controlescolar.wControllers.teacherFController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Path;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
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
        ObservableList<String> items = FXCollections.observableArrayList(
                "Ana García - Matrícula: A12345 • 3er Semestre",
                "Carlos López - Matrícula: A12346 • 2do Semestre",
                "María Rodríguez - Matrícula: A12347 • 1er Semestre",
                "Juan Pérez - Matrícula: A12348 • 4to Semestre",
                "Sofía Martínez - Matrícula: A12349 • 2do Semestre"
        );
        itemListView.setItems(items);
    }

    private void loadMaestrosData() {
        ObservableList<String> items = FXCollections.observableArrayList(
                "Dr. Roberto Sánchez - Departamento: Matemáticas • Tiempo Completo",
                "Mtra. Laura Jiménez - Departamento: Literatura • Medio Tiempo",
                "Ing. Miguel Ángel Torres - Departamento: Ingeniería • Tiempo Completo",
                "Dra. Patricia Vega - Departamento: Ciencias • Tiempo Completo"
        );
        itemListView.setItems(items);
    }

    private void loadMateriasData() {
        ObservableList<String> items = FXCollections.observableArrayList(
                "Matemáticas 101 - Créditos: 8 • Obligatoria",
                "Literatura Contemporánea - Créditos: 6 • Optativa",
                "Física Básica - Créditos: 8 • Obligatoria",
                "Programación Avanzada - Créditos: 10 • Obligatoria",
                "Historia del Arte - Créditos: 4 • Optativa",
                "Química Orgánica - Créditos: 8 • Obligatoria"
        );
        itemListView.setItems(items);
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