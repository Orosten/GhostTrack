package fr.esiee.ghosttrack;

import fr.esiee.ghosttrack.controller.AdminController;
import fr.esiee.ghosttrack.controller.AgentController;
import fr.esiee.ghosttrack.db.DatabaseConfig;
import fr.esiee.ghosttrack.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GhostTrackApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Test de la connexion à la base de données au démarrage
            DatabaseConfig.getConnection();

            FXMLLoader fxmlLoader = new FXMLLoader(GhostTrackApplication.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 300);
            stage.setTitle("GhostTrack - Connexion");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.err.println("Erreur au démarrage de l'application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Charge la vue appropriée en fonction du rôle de l'utilisateur
     * @param stage Le stage actuel
     * @param user L'utilisateur authentifié
     * @throws IOException En cas d'erreur de chargement de la vue
     */
    public static void loadView(Stage stage, User user) throws IOException {
        String viewFile;
        String title;

        if (user.isAdmin()) {
            viewFile = "admin-view.fxml";
            title = "GhostTrack - Administration";
        } else {
            viewFile = "agent-view.fxml";
            title = "GhostTrack - Agent";
        }

        FXMLLoader loader = new FXMLLoader(GhostTrackApplication.class.getResource(viewFile));
        Parent root = loader.load();

        // Passage de l'utilisateur connecté au contrôleur
        if (user.isAdmin()) {
            AdminController controller = loader.getController();
            controller.setUser(user);
        } else {
            AgentController controller = loader.getController();
            controller.setUser(user);
        }

        Scene scene = new Scene(root, 900, 700);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    /**
     * Ferme la connexion à la base de données lors de l'arrêt de l'application
     */
    @Override
    public void stop() throws Exception {
        DatabaseConfig.closeConnection();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}