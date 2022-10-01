module CS3233.Module.Project {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;
    opens sample;
    opens sample.model;
    opens sample.controllers;
    requires org.controlsfx.controls;
}