package fr.esiee.ghosttrack.dao;

import fr.esiee.ghosttrack.db.DatabaseConfig;
import fr.esiee.ghosttrack.model.Manifestation;
import fr.esiee.ghosttrack.model.Rapport;
import fr.esiee.ghosttrack.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object pour la classe Rapport
 */
public class RapportDAO {

    /**
     * Récupère tous les rapports
     * @return Liste de tous les rapports
     */
    public static List<Rapport> getAll() {
        List<Rapport> rapports = new ArrayList<>();
        String query = "SELECT r.*, u.login, m.typeFantome, m.description as manifestationDescription " +
                "FROM RAPPORT r " +
                "JOIN UTILISATEUR u ON r.idAgent = u.idUtilisateur " +
                "JOIN MANIFESTATION m ON r.idManifestation = m.idManifestation " +
                "ORDER BY r.dateRapport DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                rapports.add(mapResultSetToRapport(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des rapports: " + e.getMessage());
        }

        return rapports;
    }

    /**
     * Récupère un rapport par son ID
     * @param id ID du rapport
     * @return Rapport trouvé ou null si aucun
     */
    public static Rapport getById(int id) {
        String query = "SELECT r.*, u.login, m.typeFantome, m.description as manifestationDescription " +
                "FROM RAPPORT r " +
                "JOIN UTILISATEUR u ON r.idAgent = u.idUtilisateur " +
                "JOIN MANIFESTATION m ON r.idManifestation = m.idManifestation " +
                "WHERE r.idRapport = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToRapport(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du rapport: " + e.getMessage());
        }

        return null;
    }

    /**
     * Récupère les rapports d'une manifestation
     * @param idManifestation ID de la manifestation
     * @return Liste des rapports de la manifestation
     */
    public static List<Rapport> getRapportsByManifestation(int idManifestation) {
        List<Rapport> rapports = new ArrayList<>();
        String query = "SELECT r.*, u.login, m.typeFantome, m.description as manifestationDescription " +
                "FROM RAPPORT r " +
                "JOIN UTILISATEUR u ON r.idAgent = u.idUtilisateur " +
                "JOIN MANIFESTATION m ON r.idManifestation = m.idManifestation " +
                "WHERE r.idManifestation = ? " +
                "ORDER BY r.dateRapport DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idManifestation);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                rapports.add(mapResultSetToRapport(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des rapports: " + e.getMessage());
        }

        return rapports;
    }

    /**
     * Récupère les rapports rédigés par un agent
     * @param idAgent ID de l'agent
     * @return Liste des rapports de l'agent
     */
    public static List<Rapport> getRapportsByAgent(int idAgent) {
        List<Rapport> rapports = new ArrayList<>();
        String query = "SELECT r.*, u.login, m.typeFantome, m.description as manifestationDescription " +
                "FROM RAPPORT r " +
                "JOIN UTILISATEUR u ON r.idAgent = u.idUtilisateur " +
                "JOIN MANIFESTATION m ON r.idManifestation = m.idManifestation " +
                "WHERE r.idAgent = ? " +
                "ORDER BY r.dateRapport DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idAgent);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                rapports.add(mapResultSetToRapport(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des rapports: " + e.getMessage());
        }

        return rapports;
    }

    /**
     * Vérifie si une manifestation a déjà un rapport
     * @param idManifestation ID de la manifestation
     * @return true si un rapport existe, false sinon
     */
    public static boolean existsForManifestation(int idManifestation) {
        String query = "SELECT COUNT(*) FROM RAPPORT WHERE idManifestation = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idManifestation);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification d'existence de rapport: " + e.getMessage());
        }

        return false;
    }

    /**
     * Crée un nouveau rapport
     * @param rapport Le rapport à créer
     * @return ID du rapport créé ou -1 si échec
     */
    public static int create(Rapport rapport) {
        String query = "INSERT INTO RAPPORT (dateRapport, contenu, pdfPath, idManifestation, idAgent) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(rapport.getDateRapport()));
            pstmt.setString(2, rapport.getContenu());
            pstmt.setString(3, rapport.getPdfPath());
            pstmt.setInt(4, rapport.getIdManifestation());
            pstmt.setInt(5, rapport.getIdAgent());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                // Marquer la manifestation comme résolue
                Manifestation manifestation = ManifestationDAO.getById(rapport.getIdManifestation());
                if (manifestation != null && manifestation.isEnCours()) {
                    manifestation.setStatut(Manifestation.STATUT_RESOLUE);
                    ManifestationDAO.update(manifestation);
                }

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du rapport: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Met à jour un rapport existant
     * @param rapport Le rapport à mettre à jour
     * @return true si succès, false sinon
     */
    public static boolean update(Rapport rapport) {
        String query = "UPDATE RAPPORT SET dateRapport = ?, contenu = ?, pdfPath = ? " +
                "WHERE idRapport = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(rapport.getDateRapport()));
            pstmt.setString(2, rapport.getContenu());
            pstmt.setString(3, rapport.getPdfPath());
            pstmt.setInt(4, rapport.getIdRapport());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du rapport: " + e.getMessage());
            return false;
        }
    }

    /**
     * Convertit un ResultSet en objet Rapport avec les objets associés
     */
    private static Rapport mapResultSetToRapport(ResultSet rs) throws SQLException {
        Rapport rapport = new Rapport(
                rs.getInt("idRapport"),
                rs.getTimestamp("dateRapport").toLocalDateTime(),
                rs.getString("contenu"),
                rs.getString("pdfPath"),
                rs.getInt("idManifestation"),
                rs.getInt("idAgent")
        );

        // Ajout des objets associés
        User agent = new User(
                rs.getInt("idAgent"),
                rs.getString("login"),
                "",  // On ne récupère pas le mot de passe
                "AGENT"
        );
        rapport.setAgent(agent);

        // On ne crée qu'une référence légère à la manifestation
        Manifestation manifestation = new Manifestation(
                rs.getInt("idManifestation"),
                null,  // Pas besoin de dateHeure
                rs.getString("typeFantome"),
                rs.getString("manifestationDescription"),
                "",  // Pas besoin de statut
                0,   // Pas besoin d'idTrajet
                null, // Pas besoin d'idAgent
                null  // Pas besoin d'idMesure
        );
        rapport.setManifestation(manifestation);

        return rapport;
    }
}