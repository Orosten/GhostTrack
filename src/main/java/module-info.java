module fr.esiee.ghosttrack {
    requires javafx.controls;
    requires javafx.fxml;

    opens fr.esiee.ghosttrack to javafx.fxml;
    opens fr.esiee.ghosttrack.model to javafx.fxml;
    opens fr.esiee.ghosttrack.service to javafx.fxml;

    exports fr.esiee.ghosttrack;
    exports fr.esiee.ghosttrack.model;
    exports fr.esiee.ghosttrack.service;
}