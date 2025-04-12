package fr.esiee.ghosttrack.dao;

import fr.esiee.ghosttrack.db.DatabaseConfig;
import fr.esiee.ghosttrack.model.Trajet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object pour la classe Trajet
 */
public class TrajetDAO {

    /**
     * Récupère tous les trajets
     * @return Liste de tous les trajets
     */
    public static List<Trajet> getAll() {
        List<Trajet> trajets = new ArrayList<>();
        String query = "SELECT * FROM TRAJET ORDER BY nom";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                trajets.add(mapResultSetToTrajet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des trajets: " + e.getMessage());
        }

        return trajets;
    }

    /**
     * Récupère les trajets avec manifestations en cours
     * @return Liste des trajets perturbés
     */
    public static List<Trajet> getTrajetsPerturbations() {
        List<Trajet> trajets = new ArrayList<>();
        String query = "SELECT * FROM TRAJET WHERE statut = 'PERTURBATION' ORDER BY nom";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                trajets.add(mapResultSetToTrajet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des trajets perturbés: " + e.getMessage());
        }

        return trajets;
    }

    /**
     * Récupère un trajet par son ID
     * @param id ID du trajet
     * @return Trajet trouvé ou null si aucun
     */
    public static Trajet getById(int id) {
        String query = "SELECT * FROM TRAJET WHERE idTrajet = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToTrajet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du trajet: " + e.getMessage());
        }

        return null;
    }

    /**
     * Met à jour le statut d'un trajet
     * @param idTrajet ID du trajet
     * @param statut Nouveau statut
     * @return true si succès, false sinon
     */
    public static boolean updateStatut(int idTrajet, String statut) {
        String query = "UPDATE TRAJET SET statut = ? WHERE idTrajet = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, statut);
            pstmt.setInt(2, idTrajet);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du statut du trajet: " + e.getMessage());
            return false;
        }
    }

    /**
     * Crée un nouveau trajet
     * @param trajet Trajet à créer
     * @return ID du trajet créé ou -1 si échec
     */
    public static int create(Trajet trajet) {
        String query = "INSERT INTO TRAJET (nom, statut) VALUES (?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, trajet.getNom());
            pstmt.setString(2, trajet.getStatut());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du trajet: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Convertit un ResultSet en objet Trajet
     */
    private static Trajet mapResultSetToTrajet(ResultSet rs) throws SQLException {
        return new Trajet(
                rs.getInt("idTrajet"),
                rs.getString("nom"),
                rs.getString("statut")
        );
    }
}