package fr.esiee.ghosttrack.controller;

import fr.esiee.ghosttrack.dao.ManifestationDAO;
import fr.esiee.ghosttrack.dao.RapportDAO;
import fr.esiee.ghosttrack.model.Manifestation;
import fr.esiee.ghosttrack.model.Rapport;
import fr.esiee.ghosttrack.model.User;
import fr.esiee.ghosttrack.util.PDFGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur pour la vue agent
 */
public class AgentController implements Initializable {

    @FXML private TableView<Manifestation> tableManifestations;
    @FXML private TableColumn<Manifestation, String> colTrajet;
    @FXML private TableColumn<Manifestation, String> colTypeFantome;
    @FXML private TableColumn<Manifestation, String> colDate;
    @FXML private TableColumn<Manifestation, String> colDescription;
    @FXML private TableColumn<Manifestation, String> colMesure;

    @FXML private TextArea txtRapport;
    @FXML private Button btnGenererRapport;
    @FXML private Label lblSelectedManifestation;

    // L'utilisateur connecté
    private User currentUser;

    // Liste observable pour le tableau
    private ObservableList<Manifestation> manifestationsData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation des colonnes du tableau
        colTrajet.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTrajet().getNom()));
        colTypeFantome.setCellValueFactory(new PropertyValueFactory<>("typeFantome"));
        colDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatDateTime(cellData.getValue().getDateHeure())));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colMesure.setCellValueFactory(cellData -> {
            Manifestation m = cellData.getValue();
            return new SimpleStringProperty(m.getMesure() != null ? m.getMesure().getDescription() : "Non définie");
        });

        // Événement de sélection pour activer/désactiver les boutons et afficher l'info
        tableManifestations.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    boolean disable = newValue == null;
                    btnGenererRapport.setDisable(disable);

                    if (newValue != null) {
                        lblSelectedManifestation.setText(newValue.getTypeFantome() + " - " + newValue.getTrajet().getNom());

                        // Vérifier si un rapport existe déjà
                        boolean rapportExists = RapportDAO.existsForManifestation(newValue.getIdManifestation());
                        if (rapportExists) {
                            btnGenererRapport.setDisable(true);
                            showAlert(Alert.AlertType.INFORMATION, "Information",
                                    "Cette manifestation a déjà un rapport associé.");
                        }
                    } else {
                        lblSelectedManifestation.setText("");
                    }
                }
        );

        // Désactivation initiale du bouton
        btnGenererRapport.setDisable(true);
    }

    /**
     * Définit l'utilisateur connecté et charge ses manifestations assignées
     * @param user L'utilisateur connecté
     */
    public void setUser(User user) {
        this.currentUser = user;
        if (user != null) {
            chargerManifestationsAssignees(user.getIdUtilisateur());
        }
    }

    /**
     * Charge les manifestations assignées à l'agent
     */
    private void chargerManifestationsAssignees(int idAgent) {
        List<Manifestation> manifestations = ManifestationDAO.getManifestationsByAgent(idAgent);
        // Filtrer pour ne garder que les manifestations en cours
        manifestations = manifestations.stream()
                .filter(Manifestation::isEnCours)
                .toList();

        manifestationsData.clear();
        manifestationsData.addAll(manifestations);
        tableManifestations.setItems(manifestationsData);
    }

    /**
     * Génère un rapport PDF pour la manifestation sélectionnée
     */
    @FXML
    private void onGenererRapport(ActionEvent event) {
        Manifestation manifestation = tableManifestations.getSelectionModel().getSelectedItem();
        String contenuRapport = txtRapport.getText().trim();

        if (manifestation == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une manifestation.");
            return;
        }

        if (contenuRapport.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez saisir le contenu du rapport.");
            return;
        }

        // Demander l'emplacement du fichier PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le rapport PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf")
        );

        String fileName = "rapport_" + manifestation.getIdManifestation() + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
        fileChooser.setInitialFileName(fileName);

        File file = fileChooser.showSaveDialog(txtRapport.getScene().getWindow());
        if (file == null) {
            return;
        }

        try {
            // Génération du PDF
            String pdfPath = file.getAbsolutePath();
            PDFGenerator.generateRapportPDF(manifestation, contenuRapport, currentUser, pdfPath);

            // Création du rapport en base
            Rapport rapport = new Rapport(
                    LocalDateTime.now(),
                    contenuRapport,
                    pdfPath,
                    manifestation.getIdManifestation(),
                    currentUser.getIdUtilisateur()
            );

            int idRapport = RapportDAO.create(rapport);
            if (idRapport > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès",
                        "Rapport généré avec succès !\nEmplacement : " + pdfPath);

                // Rafraîchir la liste des manifestations
                chargerManifestationsAssignees(currentUser.getIdUtilisateur());

                // Réinitialiser le formulaire
                txtRapport.clear();
                lblSelectedManifestation.setText("");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "Erreur lors de l'enregistrement du rapport en base de données.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }

    /**
     * Formate une date pour l'affichage
     */
    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }

    /**
     * Affiche une alerte
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}