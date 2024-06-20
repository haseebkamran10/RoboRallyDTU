module dk.dtu.compute.se.pisd.roborally.RoboRally {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires java.sql;
    requires org.jetbrains.annotations;
    requires spring.context;
    requires spring.web;
    requires spring.beans;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.core;
    requires org.slf4j;
    requires spring.webmvc;
    requires spring.data.jpa;
    requires java.instrument;
    requires spring.messaging;
    requires spring.websocket;
    requires spring.tx;
    requires spring.orm;
    requires com.fasterxml.jackson.databind;

    opens dk.dtu.compute.se.pisd.roborally to spring.core, javafx.graphics, javafx.fxml;
    opens dk.dtu.compute.se.pisd.roborally.config to spring.core, spring.beans, spring.context;
    opens dk.dtu.compute.se.pisd.roborally.view to javafx.fxml, spring.core;
    opens dk.dtu.compute.se.pisd.roborally.model to spring.core;
    opens dk.dtu.compute.se.pisd.roborally.service to spring.core;

    exports dk.dtu.compute.se.pisd.roborally;
    exports dk.dtu.compute.se.pisd.roborally.controller;
    exports dk.dtu.compute.se.pisd.roborally.model;
    exports dk.dtu.compute.se.pisd.roborally.view;
    exports dk.dtu.compute.se.pisd.roborally.service;
}
