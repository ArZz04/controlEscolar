module com.arzz.cescolar.controlescolar {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.arzz.cescolar.controlescolar to javafx.fxml;
    opens com.arzz.cescolar.controlescolar.wControllers to javafx.fxml;
    exports com.arzz.cescolar.controlescolar;
}