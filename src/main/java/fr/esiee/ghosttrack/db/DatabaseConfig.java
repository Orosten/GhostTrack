package fr.esiee.ghosttrack.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Configuration de la connexion à la base de données
 */
public class DatabaseConfig {
    // Paramètres de connexion à la base de données
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ghosttrack";
    private static final String DB_USER = "root";     // À remplacer par votre utilisateur MySQL
    private static final String DB_PASSWORD = "root";     // À remplacer par votre mot de passe MySQL

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    // Singleton pour la connexion
    private static Connection connection = null;

    /**
     * Établit une connexion à la base de données si elle n'existe pas déjà
     * @return Connection - la connexion à la base de données
     * @throws SQLException en cas d'erreur de connexion
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Charger le driver JDBC
                Class.forName(DB_DRIVER);

                // Établir la connexion
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Le driver MySQL n'a pas été trouvé", e);
            }
        }
        return connection;
    }

    /**
     * Ferme la connexion à la base de données
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }
}