module dk.dtu.compute.se.pisd.roborally.StartRoboRall {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires java.sql;
    requires org.jetbrains.annotations;
    requires com.google.gson;
    requires com.google.common;
    // requires com.google.guava;

    opens dk.dtu.compute.se.pisd.roborally to javafx.fxml;
    opens dk.dtu.compute.se.pisd.roborally.controller to javafx.fxml;
    opens dk.dtu.compute.se.pisd.roborally.view to javafx.fxml;

    exports dk.dtu.compute.se.pisd.roborally;
    exports dk.dtu.compute.se.pisd.roborally.controller;
    exports dk.dtu.compute.se.pisd.roborally.model;
    exports dk.dtu.compute.se.pisd.roborally.view;
}
