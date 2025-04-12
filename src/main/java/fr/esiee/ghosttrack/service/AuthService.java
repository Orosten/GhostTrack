package fr.esiee.ghosttrack.service;

import fr.esiee.ghosttrack.dao.UserDAO;
import fr.esiee.ghosttrack.model.User;

/**
 * Service d'authentification pour GhostTrack
 * Utilise maintenant la base de données pour authentifier les utilisateurs
 */
public class AuthService {

    /**
     * Authentifie un utilisateur avec ses identifiants
     * @param login Identifiant de l'utilisateur
     * @param password Mot de passe de l'utilisateur
     * @return L'utilisateur authentifié ou null si l'authentification échoue
     */
    public static User authenticate(String login, String password) {
        // Utiliser le DAO pour accéder à la base de données
        return UserDAO.authenticate(login, password);
    }

    /**
     * Vérifie le mot de passe d'un utilisateur
     * À terme, devrait utiliser un algorithme de hachage sécurisé
     */
    private static boolean checkPassword(String inputPassword, String storedPassword) {
        // À implémenter avec une méthode de hachage sécurisée
        return inputPassword.equals(storedPassword);
    }
}