module fr.esiee.ghosttrack {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.esiee.ghosttrack to javafx.fxml;
    exports fr.esiee.ghosttrack;
}