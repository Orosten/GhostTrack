package fr.esiee.ghosttrack;

import fr.esiee.ghosttrack.model.User;
import fr.esiee.ghosttrack.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

        // Utilisation du service d'authentification
        User authenticatedUser = AuthService.authenticate(login, password);

        if (authenticatedUser != null) {
            try {
                String viewToLoad;
                String windowTitle;

                // Redirection basée sur le rôle de l'utilisateur
                if (authenticatedUser.isAdmin()) {
                    viewToLoad = "admin-view.fxml";
                    windowTitle = "GhostTrack - Administration";
                } else {
                    viewToLoad = "agent-view.fxml";
                    windowTitle = "GhostTrack - Agent";
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource(viewToLoad));
                Parent root = loader.load();

                // TODO: Passer l'utilisateur authentifié au contrôleur suivant

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 800, 600);
                stage.setTitle(windowTitle);
                stage.setScene(scene);
                stage.setResizable(true);
                stage.show();

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