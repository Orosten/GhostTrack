package fr.esiee.ghosttrack.service;

import fr.esiee.ghosttrack.model.User;

/**
 * Service d'authentification pour GhostTrack
 * Cette classe sera remplacée par une implémentation avec accès à la base de données
 */
public class AuthService {

    /**
     * Authentifie un utilisateur avec ses identifiants
     * @param login Identifiant de l'utilisateur
     * @param password Mot de passe de l'utilisateur
     * @return L'utilisateur authentifié ou null si l'authentification échoue
     */
    public static User authenticate(String login, String password) {
        // Implémentation temporaire pour les tests
        // À remplacer par une authentification réelle avec la base de données
        if ("admin".equals(login) && "admin123".equals(password)) {
            return new User(1, login, password, "ADMIN");
        } else if ("agent".equals(login) && "agent123".equals(password)) {
            return new User(2, login, password, "AGENT");
        }

        return null; // Authentification échouée
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