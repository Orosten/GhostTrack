# Maquette écrite de GhostTrack
Gabriel Namias 11/02/2025

### Vue Connexion
- Permet aux utilisateurs (admin/agents) de s'authentifier.
- Redirige vers leurs vues associées.
- Authentification classique (login/mot de passe).
- Gestion des erreurs en cas de mauvais identifiants.

### Vue Administrateur Système
- Liste des trajets et leur statut :
  - **Couleur verte** : Rien à signaler.
  - **Couleur violette** : Manifestation en cours.

- Possibilité pour l’administrateur d’interagir avec chaque trajet :
  - Assigner un agent à une manifestation.
  - Associer une mesure préconisée.

- Page d'archives avec les rapports associés à chaque manifestation rencontrée.

  - Recherche par date, type de phénomène ou agent responsable.

### Vue Agent de Terrain
- Page initiale avec les tickets ouverts par les administrateurs.
  - Seules les manifestations en cours où l’agent est affecté sont visibles.

- Chaque manifestation est un objet pouvant être refermé par l’agent avec un rapport en PDF.
  - Modèles prédéfinis de rapports ou possibilité de champ libre.