package com.arzz.cescolar.controlescolar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class cEscolarApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(cEscolarApplication.class.getResource("cEscolar-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);

        // Establecer el ícono de la ventana
        String imagePath = Objects.requireNonNull(
                cEscolarApplication.class.getResource("/com/arzz/cescolar/controlescolar/images/logoAI.png")
        ).toExternalForm();
        stage.getIcons().add(new Image(imagePath));

        stage.setTitle("SysArZz - Control Escolar");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
