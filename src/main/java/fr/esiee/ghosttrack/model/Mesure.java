package fr.esiee.ghosttrack.model;

/**
 * Classe représentant une mesure préconisée dans le système GhostTrack
 */
public class Mesure {
    private int idMesure;
    private String description;

    // Constructeur complet
    public Mesure(int idMesure, String description) {
        this.idMesure = idMesure;
        this.description = description;
    }

    // Constructeur sans ID (pour la création)
    public Mesure(String description) {
        this.description = description;
    }

    // Getters et Setters
    public int getIdMesure() {
        return idMesure;
    }

    public void setIdMesure(int idMesure) {
        this.idMesure = idMesure;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}