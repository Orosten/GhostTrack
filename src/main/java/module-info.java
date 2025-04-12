module fr.esiee.ghosttrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens fr.esiee.ghosttrack to javafx.fxml;
    opens fr.esiee.ghosttrack.model to javafx.fxml;
    opens fr.esiee.ghosttrack.service to javafx.fxml;
    opens fr.esiee.ghosttrack.controller to javafx.fxml;
    opens fr.esiee.ghosttrack.dao to javafx.fxml;
    opens fr.esiee.ghosttrack.db to javafx.fxml;
    opens fr.esiee.ghosttrack.util to javafx.fxml;

    exports fr.esiee.ghosttrack;
    exports fr.esiee.ghosttrack.model;
    exports fr.esiee.ghosttrack.service;
    exports fr.esiee.ghosttrack.controller;
    exports fr.esiee.ghosttrack.dao;
    exports fr.esiee.ghosttrack.db;
    exports fr.esiee.ghosttrack.util;
}