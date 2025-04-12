package fr.esiee.ghosttrack.dao;

import fr.esiee.ghosttrack.db.DatabaseConfig;
import fr.esiee.ghosttrack.model.Mesure;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object pour la classe Mesure
 */
public class MesureDAO {

    /**
     * Récupère toutes les mesures
     * @return Liste de toutes les mesures
     */
    public static List<Mesure> getAll() {
        List<Mesure> mesures = new ArrayList<>();
        String query = "SELECT * FROM MESURE ORDER BY description";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                mesures.add(new Mesure(
                        rs.getInt("idMesure"),
                        rs.getString("description")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des mesures: " + e.getMessage());
        }

        return mesures;
    }

    /**
     * Récupère une mesure par son ID
     * @param id ID de la mesure
     * @return Mesure trouvée ou null si aucune
     */
    public static Mesure getById(int id) {
        String query = "SELECT * FROM MESURE WHERE idMesure = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Mesure(
                        rs.getInt("idMesure"),
                        rs.getString("description")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la mesure: " + e.getMessage());
        }

        return null;
    }

    /**
     * Crée une nouvelle mesure
     * @param mesure La mesure à créer
     * @return ID de la mesure créée ou -1 si échec
     */
    public static int create(Mesure mesure) {
        String query = "INSERT INTO MESURE (description) VALUES (?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, mesure.getDescription());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la mesure: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Met à jour une mesure existante
     * @param mesure La mesure à mettre à jour
     * @return true si succès, false sinon
     */
    public static boolean update(Mesure mesure) {
        String query = "UPDATE MESURE SET description = ? WHERE idMesure = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, mesure.getDescription());
            pstmt.setInt(2, mesure.getIdMesure());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la mesure: " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime une mesure existante
     * @param id ID de la mesure à supprimer
     * @return true si succès, false sinon
     */
    public static boolean delete(int id) {
        String query = "DELETE FROM MESURE WHERE idMesure = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la mesure: " + e.getMessage());
            return false;
        }
    }
}