package fr.esiee.ghosttrack.model;

/**
 * Classe représentant un utilisateur dans le système GhostTrack
 */
public class User {
    private int idUtilisateur;
    private String login;
    private String motDePasse;
    private String role;

    // Constructeur complet
    public User(int idUtilisateur, String login, String motDePasse, String role) {
        this.idUtilisateur = idUtilisateur;
        this.login = login;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // Constructeur sans ID (pour la création)
    public User(String login, String motDePasse, String role) {
        this.login = login;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // Getters et Setters
    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Vérifie si l'utilisateur est un administrateur
     * @return true si l'utilisateur est un administrateur, false sinon
     */
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }

    /**
     * Vérifie si l'utilisateur est un agent
     * @return true si l'utilisateur est un agent, false sinon
     */
    public boolean isAgent() {
        return "AGENT".equalsIgnoreCase(this.role);
    }

    @Override
    public String toString() {
        return "User{" +
                "idUtilisateur=" + idUtilisateur +
                ", login='" + login + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}