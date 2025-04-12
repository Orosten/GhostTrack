package fr.esiee.ghosttrack.model;

import java.time.LocalDateTime;

/**
 * Classe représentant une manifestation paranormale dans le système GhostTrack
 */
public class Manifestation {
    private int idManifestation;
    private LocalDateTime dateHeure;
    private String typeFantome;
    private String description;
    private String statut;
    private int idTrajet;
    private Integer idAgent; // Peut être null
    private Integer idMesure; // Peut être null

    // Références aux objets associés (optionnels, pour faciliter l'affichage)
    private Trajet trajet;
    private User agent;
    private Mesure mesure;

    // Constantes pour les valeurs de statut
    public static final String STATUT_EN_COURS = "EN_COURS";
    public static final String STATUT_RESOLUE = "RESOLUE";
    public static final String STATUT_NON_RESOLUE = "NON_RESOLUE";

    // Constructeur complet avec ID
    public Manifestation(int idManifestation, LocalDateTime dateHeure, String typeFantome,
                         String description, String statut, int idTrajet,
                         Integer idAgent, Integer idMesure) {
        this.idManifestation = idManifestation;
        this.dateHeure = dateHeure;
        this.typeFantome = typeFantome;
        this.description = description;
        this.statut = statut;
        this.idTrajet = idTrajet;
        this.idAgent = idAgent;
        this.idMesure = idMesure;
    }

    // Constructeur sans ID (pour la création)
    public Manifestation(LocalDateTime dateHeure, String typeFantome, String description,
                         String statut, int idTrajet, Integer idAgent, Integer idMesure) {
        this.dateHeure = dateHeure;
        this.typeFantome = typeFantome;
        this.description = description;
        this.statut = statut;
        this.idTrajet = idTrajet;
        this.idAgent = idAgent;
        this.idMesure = idMesure;
    }

    // Getters et Setters
    public int getIdManifestation() {
        return idManifestation;
    }

    public void setIdManifestation(int idManifestation) {
        this.idManifestation = idManifestation;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public String getTypeFantome() {
        return typeFantome;
    }

    public void setTypeFantome(String typeFantome) {
        this.typeFantome = typeFantome;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getIdTrajet() {
        return idTrajet;
    }

    public void setIdTrajet(int idTrajet) {
        this.idTrajet = idTrajet;
    }

    public Integer getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(Integer idAgent) {
        this.idAgent = idAgent;
    }

    public Integer getIdMesure() {
        return idMesure;
    }

    public void setIdMesure(Integer idMesure) {
        this.idMesure = idMesure;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public Mesure getMesure() {
        return mesure;
    }

    public void setMesure(Mesure mesure) {
        this.mesure = mesure;
    }

    /**
     * Vérifie si la manifestation est en cours
     * @return true si la manifestation est en cours, false sinon
     */
    public boolean isEnCours() {
        return STATUT_EN_COURS.equals(this.statut);
    }

    /**
     * Vérifie si la manifestation est résolue
     * @return true si la manifestation est résolue, false sinon
     */
    public boolean isResolue() {
        return STATUT_RESOLUE.equals(this.statut);
    }

    @Override
    public String toString() {
        return typeFantome + " - " + description;
    }
}