package fr.esiee.ghosttrack.util;

import fr.esiee.ghosttrack.model.Manifestation;
import fr.esiee.ghosttrack.model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitaire pour la génération de PDF
 * Note: Dans un cas réel, on utiliserait une bibliothèque comme iText ou Apache PDFBox
 * Pour ce prototype, nous allons simplement simuler la génération de PDF
 */
public class PDFGenerator {

    /**
     * Génère un PDF de rapport pour une manifestation
     * @param manifestation Manifestation concernée
     * @param contenu Contenu du rapport
     * @param agent Agent ayant rédigé le rapport
     * @param filePath Chemin du fichier PDF à générer
     * @throws IOException Si une erreur survient lors de l'écriture du fichier
     */
    public static void generateRapportPDF(Manifestation manifestation, String contenu, User agent, String filePath) throws IOException {
        // Dans une implémentation réelle, on utiliserait une bibliothèque PDF
        // Pour ce prototype, on va juste créer un fichier texte avec extension .pdf

        StringBuilder pdfContent = new StringBuilder();
        pdfContent.append("=== RAPPORT DE MANIFESTATION PARANORMALE ===\n\n");
        pdfContent.append("Date du rapport: ").append(formatDateTime(LocalDateTime.now())).append("\n\n");

        pdfContent.append("DÉTAILS DE LA MANIFESTATION\n");
        pdfContent.append("---------------------------\n");
        pdfContent.append("Identifiant: ").append(manifestation.getIdManifestation()).append("\n");
        pdfContent.append("Type de fantôme: ").append(manifestation.getTypeFantome()).append("\n");
        pdfContent.append("Date et heure: ").append(formatDateTime(manifestation.getDateHeure())).append("\n");
        pdfContent.append("Trajet: ").append(manifestation.getTrajet().getNom()).append("\n");
        pdfContent.append("Description: ").append(manifestation.getDescription()).append("\n");

        if (manifestation.getMesure() != null) {
            pdfContent.append("Mesure préconisée: ").append(manifestation.getMesure().getDescription()).append("\n");
        } else {
            pdfContent.append("Mesure préconisée: Non définie\n");
        }

        pdfContent.append("\nAGENT RESPONSABLE\n");
        pdfContent.append("-----------------\n");
        pdfContent.append("Identifiant: ").append(agent.getIdUtilisateur()).append("\n");
        pdfContent.append("Login: ").append(agent.getLogin()).append("\n\n");

        pdfContent.append("CONTENU DU RAPPORT\n");
        pdfContent.append("------------------\n");
        pdfContent.append(contenu).append("\n\n");

        pdfContent.append("STATUT\n");
        pdfContent.append("------\n");
        pdfContent.append("Cette manifestation est désormais marquée comme RÉSOLUE.\n\n");

        pdfContent.append("=== FIN DU RAPPORT ===");

        // Écriture dans le fichier
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfContent.toString().getBytes());
        }
    }

    /**
     * Formate une date pour l'affichage
     */
    private static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }
}