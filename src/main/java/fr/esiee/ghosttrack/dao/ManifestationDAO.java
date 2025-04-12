package fr.esiee.ghosttrack.dao;

import fr.esiee.ghosttrack.db.DatabaseConfig;
import fr.esiee.ghosttrack.model.Manifestation;
import fr.esiee.ghosttrack.model.Mesure;
import fr.esiee.ghosttrack.model.Trajet;
import fr.esiee.ghosttrack.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object pour la classe Manifestation
 */
public class ManifestationDAO {

    /**
     * Récupère toutes les manifestations
     * @return Liste de toutes les manifestations
     */
    public static List<Manifestation> getAll() {
        List<Manifestation> manifestations = new ArrayList<>();
        String query = "SELECT m.*, t.nom as nomTrajet, t.statut as statutTrajet, " +
                "u.login as loginAgent, mes.description as descriptionMesure " +
                "FROM MANIFESTATION m " +
                "JOIN TRAJET t ON m.idTrajet = t.idTrajet " +
                "LEFT JOIN UTILISATEUR u ON m.idAgent = u.idUtilisateur " +
                "LEFT JOIN MESURE mes ON m.idMesure = mes.idMesure " +
                "ORDER BY m.dateHeure DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                manifestations.add(mapResultSetToManifestation(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des manifestations: " + e.getMessage());
        }

        return manifestations;
    }

    /**
     * Récupère une manifestation par son ID
     * @param id ID de la manifestation
     * @return Manifestation trouvée ou null si aucune
     */
    public static Manifestation getById(int id) {
        String query = "SELECT m.*, t.nom as nomTrajet, t.statut as statutTrajet, " +
                "u.login as loginAgent, mes.description as descriptionMesure " +
                "FROM MANIFESTATION m " +
                "JOIN TRAJET t ON m.idTrajet = t.idTrajet " +
                "LEFT JOIN UTILISATEUR u ON m.idAgent = u.idUtilisateur " +
                "LEFT JOIN MESURE mes ON m.idMesure = mes.idMesure " +
                "WHERE m.idManifestation = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToManifestation(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la manifestation: " + e.getMessage());
        }

        return null;
    }

    /**
     * Récupère les manifestations en cours
     * @return Liste des manifestations en cours
     */
    public static List<Manifestation> getManifestationsEnCours() {
        List<Manifestation> manifestations = new ArrayList<>();
        String query = "SELECT m.*, t.nom as nomTrajet, t.statut as statutTrajet, " +
                "u.login as loginAgent, mes.description as descriptionMesure " +
                "FROM MANIFESTATION m " +
                "JOIN TRAJET t ON m.idTrajet = t.idTrajet " +
                "LEFT JOIN UTILISATEUR u ON m.idAgent = u.idUtilisateur " +
                "LEFT JOIN MESURE mes ON m.idMesure = mes.idMesure " +
                "WHERE m.statut = 'EN_COURS' " +
                "ORDER BY m.dateHeure DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                manifestations.add(mapResultSetToManifestation(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des manifestations en cours: " + e.getMessage());
        }

        return manifestations;
    }

    /**
     * Récupère les manifestations assignées à un agent
     * @param idAgent ID de l'agent
     * @return Liste des manifestations assignées à l'agent
     */
    public static List<Manifestation> getManifestationsByAgent(int idAgent) {
        List<Manifestation> manifestations = new ArrayList<>();
        String query = "SELECT m.*, t.nom as nomTrajet, t.statut as statutTrajet, " +
                "u.login as loginAgent, mes.description as descriptionMesure " +
                "FROM MANIFESTATION m " +
                "JOIN TRAJET t ON m.idTrajet = t.idTrajet " +
                "LEFT JOIN UTILISATEUR u ON m.idAgent = u.idUtilisateur " +
                "LEFT JOIN MESURE mes ON m.idMesure = mes.idMesure " +
                "WHERE m.idAgent = ? " +
                "ORDER BY m.dateHeure DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idAgent);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                manifestations.add(mapResultSetToManifestation(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des manifestations de l'agent: " + e.getMessage());
        }

        return manifestations;
    }

    /**
     * Recherche des manifestations selon des critères
     * @param typeFantome Type de fantôme (peut être null)
     * @param startDate Date de début (peut être null)
     * @param endDate Date de fin (peut être null)
     * @param idAgent ID de l'agent (peut être null)
     * @return Liste des manifestations correspondant aux critères
     */
    public static List<Manifestation> search(String typeFantome, LocalDateTime startDate,
                                             LocalDateTime endDate, Integer idAgent) {
        List<Manifestation> manifestations = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT m.*, t.nom as nomTrajet, t.statut as statutTrajet, " +
                        "u.login as loginAgent, mes.description as descriptionMesure " +
                        "FROM MANIFESTATION m " +
                        "JOIN TRAJET t ON m.idTrajet = t.idTrajet " +
                        "LEFT JOIN UTILISATEUR u ON m.idAgent = u.idUtilisateur " +
                        "LEFT JOIN MESURE mes ON m.idMesure = mes.idMesure " +
                        "WHERE 1=1 "
        );

        List<Object> parameters = new ArrayList<>();

        if (typeFantome != null && !typeFantome.isEmpty()) {
            queryBuilder.append("AND m.typeFantome LIKE ? ");
            parameters.add("%" + typeFantome + "%");
        }

        if (startDate != null) {
            queryBuilder.append("AND m.dateHeure >= ? ");
            parameters.add(Timestamp.valueOf(startDate));
        }

        if (endDate != null) {
            queryBuilder.append("AND m.dateHeure <= ? ");
            parameters.add(Timestamp.valueOf(endDate));
        }

        if (idAgent != null) {
            queryBuilder.append("AND m.idAgent = ? ");
            parameters.add(idAgent);
        }

        queryBuilder.append("ORDER BY m.dateHeure DESC");

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                manifestations.add(mapResultSetToManifestation(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des manifestations: " + e.getMessage());
        }

        return manifestations;
    }

    /**
     * Crée une nouvelle manifestation
     * @param manifestation La manifestation à créer
     * @return ID de la manifestation créée ou -1 si échec
     */
    public static int create(Manifestation manifestation) {
        String query = "INSERT INTO MANIFESTATION (dateHeure, typeFantome, description, statut, idTrajet, idAgent, idMesure) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(manifestation.getDateHeure()));
            pstmt.setString(2, manifestation.getTypeFantome());
            pstmt.setString(3, manifestation.getDescription());
            pstmt.setString(4, manifestation.getStatut());
            pstmt.setInt(5, manifestation.getIdTrajet());

            if (manifestation.getIdAgent() != null) {
                pstmt.setInt(6, manifestation.getIdAgent());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }

            if (manifestation.getIdMesure() != null) {
                pstmt.setInt(7, manifestation.getIdMesure());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    // Mise à jour du statut du trajet associé
                    TrajetDAO.updateStatut(manifestation.getIdTrajet(), Trajet.STATUT_PERTURBATION);
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la manifestation: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Met à jour une manifestation existante
     * @param manifestation La manifestation à mettre à jour
     * @return true si succès, false sinon
     */
    public static boolean update(Manifestation manifestation) {
        String query = "UPDATE MANIFESTATION SET dateHeure = ?, typeFantome = ?, description = ?, " +
                "statut = ?, idTrajet = ?, idAgent = ?, idMesure = ? " +
                "WHERE idManifestation = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(manifestation.getDateHeure()));
            pstmt.setString(2, manifestation.getTypeFantome());
            pstmt.setString(3, manifestation.getDescription());
            pstmt.setString(4, manifestation.getStatut());
            pstmt.setInt(5, manifestation.getIdTrajet());

            if (manifestation.getIdAgent() != null) {
                pstmt.setInt(6, manifestation.getIdAgent());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }

            if (manifestation.getIdMesure() != null) {
                pstmt.setInt(7, manifestation.getIdMesure());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }

            pstmt.setInt(8, manifestation.getIdManifestation());

            int rowsAffected = pstmt.executeUpdate();

            // Vérifier s'il faut mettre à jour le statut du trajet
            if (rowsAffected > 0 && manifestation.isResolue()) {
                // Vérifier s'il reste des manifestations en cours pour ce trajet
                if (!hasManifestationsEnCours(manifestation.getIdTrajet())) {
                    // Si non, remettre le statut du trajet à normal
                    TrajetDAO.updateStatut(manifestation.getIdTrajet(), Trajet.STATUT_NORMAL);
                }
            }

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la manifestation: " + e.getMessage());
            return false;
        }
    }

    /**
     * Assigne un agent à une manifestation
     * @param idManifestation ID de la manifestation
     * @param idAgent ID de l'agent
     * @return true si succès, false sinon
     */
    public static boolean assignAgent(int idManifestation, int idAgent) {
        String query = "UPDATE MANIFESTATION SET idAgent = ? WHERE idManifestation = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idAgent);
            pstmt.setInt(2, idManifestation);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'assignation de l'agent: " + e.getMessage());
            return false;
        }
    }

    /**
     * Assigne une mesure à une manifestation
     * @param idManifestation ID de la manifestation
     * @param idMesure ID de la mesure
     * @return true si succès, false sinon
     */
    public static boolean assignMesure(int idManifestation, int idMesure) {
        String query = "UPDATE MANIFESTATION SET idMesure = ? WHERE idManifestation = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idMesure);
            pstmt.setInt(2, idManifestation);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'assignation de la mesure: " + e.getMessage());
            return false;
        }
    }

    /**
     * Vérifie s'il reste des manifestations en cours pour un trajet donné
     * @param idTrajet ID du trajet
     * @return true s'il existe des manifestations en cours, false sinon
     */
    private static boolean hasManifestationsEnCours(int idTrajet) {
        String query = "SELECT COUNT(*) FROM MANIFESTATION WHERE idTrajet = ? AND statut = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idTrajet);
            pstmt.setString(2, Manifestation.STATUT_EN_COURS);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification des manifestations en cours: " + e.getMessage());
        }

        return false;
    }

    /**
     * Convertit un ResultSet en objet Manifestation avec les objets associés
     */
    private static Manifestation mapResultSetToManifestation(ResultSet rs) throws SQLException {
        Manifestation manifestation = new Manifestation(
                rs.getInt("idManifestation"),
                rs.getTimestamp("dateHeure").toLocalDateTime(),
                rs.getString("typeFantome"),
                rs.getString("description"),
                rs.getString("statut"),
                rs.getInt("idTrajet"),
                (rs.getObject("idAgent") != null) ? rs.getInt("idAgent") : null,
                (rs.getObject("idMesure") != null) ? rs.getInt("idMesure") : null
        );

        // Ajout des objets associés
        Trajet trajet = new Trajet(
                rs.getInt("idTrajet"),
                rs.getString("nomTrajet"),
                rs.getString("statutTrajet")
        );
        manifestation.setTrajet(trajet);

        if (rs.getObject("idAgent") != null) {
            User agent = new User(
                    rs.getInt("idAgent"),
                    rs.getString("loginAgent"),
                    "",  // On ne récupère pas le mot de passe
                    "AGENT"
            );
            manifestation.setAgent(agent);
        }

        if (rs.getObject("idMesure") != null) {
            Mesure mesure = new Mesure(
                    rs.getInt("idMesure"),
                    rs.getString("descriptionMesure")
            );
            manifestation.setMesure(mesure);
        }

        return manifestation;
    }
}