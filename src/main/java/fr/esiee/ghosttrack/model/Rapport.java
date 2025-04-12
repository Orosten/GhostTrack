package fr.esiee.ghosttrack.model;

import java.time.LocalDateTime;

/**
 * Classe représentant un rapport d'intervention dans le système GhostTrack
 */
public class Rapport {
    private int idRapport;
    private LocalDateTime dateRapport;
    private String contenu;
    private String pdfPath;
    private int idManifestation;
    private int idAgent;

    // Références aux objets associés (optionnels, pour faciliter l'affichage)
    private Manifestation manifestation;
    private User agent;

    // Constructeur complet
    public Rapport(int idRapport, LocalDateTime dateRapport, String contenu,
                   String pdfPath, int idManifestation, int idAgent) {
        this.idRapport = idRapport;
        this.dateRapport = dateRapport;
        this.contenu = contenu;
        this.pdfPath = pdfPath;
        this.idManifestation = idManifestation;
        this.idAgent = idAgent;
    }

    // Constructeur sans ID (pour la création)
    public Rapport(LocalDateTime dateRapport, String contenu,
                   String pdfPath, int idManifestation, int idAgent) {
        this.dateRapport = dateRapport;
        this.contenu = contenu;
        this.pdfPath = pdfPath;
        this.idManifestation = idManifestation;
        this.idAgent = idAgent;
    }

    // Getters et Setters
    public int getIdRapport() {
        return idRapport;
    }

    public void setIdRapport(int idRapport) {
        this.idRapport = idRapport;
    }

    public LocalDateTime getDateRapport() {
        return dateRapport;
    }

    public void setDateRapport(LocalDateTime dateRapport) {
        this.dateRapport = dateRapport;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public int getIdManifestation() {
        return idManifestation;
    }

    public void setIdManifestation(int idManifestation) {
        this.idManifestation = idManifestation;
    }

    public int getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(int idAgent) {
        this.idAgent = idAgent;
    }

    public Manifestation getManifestation() {
        return manifestation;
    }

    public void setManifestation(Manifestation manifestation) {
        this.manifestation = manifestation;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return "Rapport du " + dateRapport;
    }
}