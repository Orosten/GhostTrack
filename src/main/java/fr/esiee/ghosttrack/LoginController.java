package fr.esiee.ghosttrack;

import fr.esiee.ghosttrack.model.User;
import fr.esiee.ghosttrack.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();

        if (login.isEmpty() || password.isEmpty()) {
            showAlert("Erreur de connexion", "Veuillez remplir tous les champs.");
            return;
        }

        // Utilisation du service d'authentification avec la base de données
        User authenticatedUser = AuthService.authenticate(login, password);

        if (authenticatedUser != null) {
            try {
                // Utiliser la méthode définie dans GhostTrackApplication pour charger la vue
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                GhostTrackApplication.loadView(stage, authenticatedUser);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de charger la page.");
            }
        } else {
            showAlert("Erreur de connexion", "Identifiants incorrects.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}