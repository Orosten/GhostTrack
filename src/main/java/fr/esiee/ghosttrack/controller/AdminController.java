package fr.esiee.ghosttrack.controller;

import fr.esiee.ghosttrack.dao.ManifestationDAO;
import fr.esiee.ghosttrack.dao.MesureDAO;
import fr.esiee.ghosttrack.dao.RapportDAO;
import fr.esiee.ghosttrack.dao.UserDAO;
import fr.esiee.ghosttrack.model.Manifestation;
import fr.esiee.ghosttrack.model.Mesure;
import fr.esiee.ghosttrack.model.Rapport;
import fr.esiee.ghosttrack.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Contrôleur pour la vue administrateur
 */
public class AdminController implements Initializable {

    // Onglet Manifestations en cours
    @FXML private TableView<Manifestation> tableManifestations;
    @FXML private TableColumn<Manifestation, String> colTrajet;
    @FXML private TableColumn<Manifestation, String> colTypeFantome;
    @FXML private TableColumn<Manifestation, String> colDate;
    @FXML private TableColumn<Manifestation, String> colDescription;
    @FXML private TableColumn<Manifestation, String> colAgent;
    @FXML private TableColumn<Manifestation, String> colMesure;

    @FXML private ComboBox<User> comboAgents;
    @FXML private ComboBox<Mesure> comboMesures;
    @FXML private Button btnAssignerAgent;
    @FXML private Button btnAssignerMesure;

    // Onglet Historique des rapports
    @FXML private TableView<Rapport> tableRapports;
    @FXML private TableColumn<Rapport, String> colRapportDate;
    @FXML private TableColumn<Rapport, String> colRapportAgent;
    @FXML private TableColumn<Rapport, String> colRapportType;
    @FXML private TableColumn<Rapport, String> colRapportDescription;

    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private TextField txtTypeFantome;
    @FXML private ComboBox<User> comboAgentFiltre;
    @FXML private Button btnRechercher;
    @FXML private Button btnReinitialiser;
    @FXML private TextArea txtContenuRapport;

    // L'utilisateur connecté
    private User currentUser;

    // Listes observables pour les tableaux
    private ObservableList<Manifestation> manifestationsData = FXCollections.observableArrayList();
    private ObservableList<Rapport> rapportsData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation des colonnes du tableau des manifestations
        colTrajet.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTrajet().getNom()));
        colTypeFantome.setCellValueFactory(new PropertyValueFactory<>("typeFantome"));
        colDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatDateTime(cellData.getValue().getDateHeure())));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colAgent.setCellValueFactory(cellData -> {
            Manifestation m = cellData.getValue();
            return new SimpleStringProperty(m.getAgent() != null ? m.getAgent().getLogin() : "Non assigné");
        });
        colMesure.setCellValueFactory(cellData -> {
            Manifestation m = cellData.getValue();
            return new SimpleStringProperty(m.getMesure() != null ? m.getMesure().getDescription() : "Non définie");
        });

        // Colorisation des lignes selon le statut du trajet
        tableManifestations.setRowFactory(tv -> new TableRow<Manifestation>() {
            @Override
            protected void updateItem(Manifestation item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (item.getTrajet().isPerturbation()) {
                    setStyle("-fx-background-color: #e6b3ff;"); // Violet clair
                } else {
                    setStyle("-fx-background-color: #b3ffb3;"); // Vert clair
                }
            }
        });

        // Initialisation des colonnes du tableau des rapports
        colRapportDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatDateTime(cellData.getValue().getDateRapport())));
        colRapportAgent.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAgent().getLogin()));
        colRapportType.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getManifestation().getTypeFantome()));
        colRapportDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getManifestation().getDescription()));

        // Événement de sélection pour afficher le contenu du rapport
        tableRapports.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        txtContenuRapport.setText(newValue.getContenu());
                    } else {
                        txtContenuRapport.clear();
                    }
                }
        );

        // Événement de sélection pour activer/désactiver les boutons d'assignation
        tableManifestations.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    boolean disable = newValue == null;
                    btnAssignerAgent.setDisable(disable);
                    btnAssignerMesure.setDisable(disable);
                }
        );

        // Initialisation des contrôles de filtrage
        dateDebut.setValue(LocalDate.now().minusMonths(1));
        dateFin.setValue(LocalDate.now());

        // Chargement des données initiales
        chargerAgents();
        chargerMesures();
        chargerManifestationsEnCours();
        chargerRapports();
    }

    /**
     * Définit l'utilisateur connecté
     * @param user L'utilisateur connecté
     */
    public void setUser(User user) {
        this.currentUser = user;
    }

    /**
     * Charge la liste des agents pour les combos
     */
    private void chargerAgents() {
        List<User> agents = UserDAO.getAllAgents();
        comboAgents.setItems(FXCollections.observableArrayList(agents));

        // Ajout d'une option "Tous les agents" pour le filtre
        List<User> agentsFiltre = agents;
        User tousAgents = new User(-1, "Tous les agents", "", "AGENT");
        agentsFiltre.add(0, tousAgents);
        comboAgentFiltre.setItems(FXCollections.observableArrayList(agentsFiltre));
        comboAgentFiltre.getSelectionModel().selectFirst();
    }

    /**
     * Charge la liste des mesures disponibles
     */
    private void chargerMesures() {
        List<Mesure> mesures = MesureDAO.getAll();
        comboMesures.setItems(FXCollections.observableArrayList(mesures));
    }

    /**
     * Charge les manifestations en cours
     */
    private void chargerManifestationsEnCours() {
        List<Manifestation> manifestations = ManifestationDAO.getManifestationsEnCours();
        manifestationsData.clear();
        manifestationsData.addAll(manifestations);
        tableManifestations.setItems(manifestationsData);
    }

    /**
     * Charge tous les rapports
     */
    private void chargerRapports() {
        List<Rapport> rapports = RapportDAO.getAll();
        rapportsData.clear();
        rapportsData.addAll(rapports);
        tableRapports.setItems(rapportsData);
    }

    /**
     * Assigne un agent à la manifestation sélectionnée
     */
    @FXML
    private void onAssignerAgent(ActionEvent event) {
        Manifestation manifestation = tableManifestations.getSelectionModel().getSelectedItem();
        User agent = comboAgents.getSelectionModel().getSelectedItem();

        if (manifestation != null && agent != null) {
            boolean success = ManifestationDAO.assignAgent(manifestation.getIdManifestation(), agent.getIdUtilisateur());
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Agent assigné avec succès !");
                chargerManifestationsEnCours();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'assigner l'agent.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une manifestation et un agent.");
        }
    }

    /**
     * Assigne une mesure à la manifestation sélectionnée
     */
    @FXML
    private void onAssignerMesure(ActionEvent event) {
        Manifestation manifestation = tableManifestations.getSelectionModel().getSelectedItem();
        Mesure mesure = comboMesures.getSelectionModel().getSelectedItem();

        if (manifestation != null && mesure != null) {
            boolean success = ManifestationDAO.assignMesure(manifestation.getIdManifestation(), mesure.getIdMesure());
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Mesure assignée avec succès !");
                chargerManifestationsEnCours();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'assigner la mesure.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une manifestation et une mesure.");
        }
    }

    /**
     * Recherche des rapports selon les critères
     */
    @FXML
    private void onRechercher(ActionEvent event) {
        String typeFantome = txtTypeFantome.getText().trim().isEmpty() ? null : txtTypeFantome.getText().trim();
        LocalDateTime start = dateDebut.getValue() != null ? dateDebut.getValue().atStartOfDay() : null;
        LocalDateTime end = dateFin.getValue() != null ? dateFin.getValue().atTime(23, 59, 59) : null;

        User selectedAgent = comboAgentFiltre.getSelectionModel().getSelectedItem();
        Integer idAgent = (selectedAgent != null && selectedAgent.getIdUtilisateur() != -1) ?
                selectedAgent.getIdUtilisateur() : null;

        // Récupération des manifestations selon les critères
        List<Manifestation> manifestations = ManifestationDAO.search(typeFantome, start, end, idAgent);

        // Récupération des rapports associés à ces manifestations
        List<Integer> idManifestations = manifestations.stream()
                .map(Manifestation::getIdManifestation)
                .collect(Collectors.toList());

        rapportsData.clear();
        for (Integer idManifestation : idManifestations) {
            List<Rapport> rapports = RapportDAO.getRapportsByManifestation(idManifestation);
            rapportsData.addAll(rapports);
        }

        tableRapports.setItems(rapportsData);
    }

    /**
     * Réinitialise les filtres de recherche
     */
    @FXML
    private void onReinitialiser(ActionEvent event) {
        txtTypeFantome.clear();
        dateDebut.setValue(LocalDate.now().minusMonths(1));
        dateFin.setValue(LocalDate.now());
        comboAgentFiltre.getSelectionModel().selectFirst();

        chargerRapports();
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