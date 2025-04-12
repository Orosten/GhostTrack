package fr.esiee.ghosttrack.dao;

import fr.esiee.ghosttrack.db.DatabaseConfig;
import fr.esiee.ghosttrack.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object pour la classe User
 */
public class UserDAO {

    /**
     * Trouve un utilisateur par son login
     * @param login Login de l'utilisateur
     * @return User trouvé ou null si aucun utilisateur n'a ce login
     */
    public static User findByLogin(String login) {
        String query = "SELECT * FROM UTILISATEUR WHERE login = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("idUtilisateur"),
                        rs.getString("login"),
                        rs.getString("motDePasse"),
                        rs.getString("role")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur: " + e.getMessage());
        }

        return null;
    }

    /**
     * Authentifie un utilisateur avec login et mot de passe
     * @param login Login de l'utilisateur
     * @param password Mot de passe (non crypté pour le moment)
     * @return User authentifié ou null si échec
     */
    public static User authenticate(String login, String password) {
        String query = "SELECT * FROM UTILISATEUR WHERE login = ? AND motDePasse = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, login);
            pstmt.setString(2, password);  // À remplacer par une vérification de hash en production

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("idUtilisateur"),
                        rs.getString("login"),
                        rs.getString("motDePasse"),
                        rs.getString("role")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification: " + e.getMessage());
        }

        return null;
    }

    /**
     * Récupère tous les utilisateurs qui sont des agents
     * @return Liste des agents
     */
    public static List<User> getAllAgents() {
        List<User> agents = new ArrayList<>();
        String query = "SELECT * FROM UTILISATEUR WHERE role = 'AGENT'";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                agents.add(new User(
                        rs.getInt("idUtilisateur"),
                        rs.getString("login"),
                        rs.getString("motDePasse"),
                        rs.getString("role")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des agents: " + e.getMessage());
        }

        return agents;
    }

    /**
     * Crée un nouvel utilisateur dans la base de données
     * @param user L'utilisateur à créer
     * @return True si création réussie, False sinon
     */
    public static boolean create(User user) {
        String query = "INSERT INTO UTILISATEUR (login, motDePasse, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getMotDePasse());  // À remplacer par un hash en production
            pstmt.setString(3, user.getRole());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur: " + e.getMessage());
            return false;
        }
    }
}