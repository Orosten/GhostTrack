package fr.esiee.ghosttrack.model;

/**
 * Classe représentant un trajet ferroviaire dans le système GhostTrack
 */
public class Trajet {
    private int idTrajet;
    private String nom;
    private String statut;

    // Constantes pour les valeurs de statut
    public static final String STATUT_NORMAL = "NORMAL";
    public static final String STATUT_PERTURBATION = "PERTURBATION";

    // Constructeur complet
    public Trajet(int idTrajet, String nom, String statut) {
        this.idTrajet = idTrajet;
        this.nom = nom;
        this.statut = statut;
    }

    // Constructeur sans ID (pour la création)
    public Trajet(String nom, String statut) {
        this.nom = nom;
        this.statut = statut;
    }

    // Getters et Setters
    public int getIdTrajet() {
        return idTrajet;
    }

    public void setIdTrajet(int idTrajet) {
        this.idTrajet = idTrajet;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    /**
     * Vérifie si le trajet est perturbé
     * @return true si le trajet est perturbé, false sinon
     */
    public boolean isPerturbation() {
        return STATUT_PERTURBATION.equals(this.statut);
    }

    @Override
    public String toString() {
        return nom + " (" + statut + ")";
    }
}