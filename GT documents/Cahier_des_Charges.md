# Cahier des charges - GhostTrack
Gabriel Namias 22/11/2024
---

## 1. Présentation du projet
- **Nom du projet** : GhostTrack
- **Description** : Application destinée à surveiller et gérer les perturbations causées par des phénomènes paranormaux sur les zones ferroviaires.
- **Objectif principal** : Garantir la sécurité des trajets en gérant les effets fantomatiques en temps réel.

---

## 2. Contexte et problématique
### Contexte
Certaines zones ferroviaires sont sujettes à des phénomènes paranormaux (apparitions fantomatiques) qui perturbent la circulation des trains.

### Problèmes rencontrés
- Ralentissements des trains.
- Baisses de température affectant les équipements.
- Interférences électriques causant des pannes.
- Changement incontrolés d'aiguillage des trains. 

---

## 3. Fonctionnalités
L’application **GhostTrack** doit permettre :
1. **Suivi et gestion des activités paranormales** :
   - Surveillance en temps réel des trajets et zones affectés par des manifestations.
   - Identification et prévision des perturbations potentielles.
2. **Rapports et archives** :
   - Enregistrement des types de fantômes et de leurs caractéristiques.
   - Historique des perturbations et des interventions réalisées.
3. **Gestion des incidents en temps réel** :
   - Activation de contre-mesures comme des alarmes fantomatiques ou des détournements d'énergie.
   - Gestion des agents de terrain déployés pour résoudre les incidents.

---

## 4. Outils techniques
1. **Technologies utilisées** :
   - Langage : Java (JavaFX pour le front)
   - Base de données : MySQL
2. **Modules techniques** :
   - Gestion des utilisateurs (administrateurs, agents).
   - Gestion des manifestations paranormales (CRUD sur les données fantomatiques).
   - Gestion des trajets ferroviaires et des perturbations associées.
   - Rapports d’intervention et historique des incidents.

---

## 5. Contraintes
### Fonctionnelles
- Interface en temps réel pour la surveillance des trajets.

### Techniques
- Application nécessitant une authentification pour pouvoir identifier l'utilisateur. (Agent/Admin)
- Sécurisation des données (gestion des accès).

---

## 6. Livrables
1. **Documentation technique** :
   - Diagrammes UML (cas d’utilisation, classes, séquences).
   - Cahier des charges finalisé.
2. **Code source** :
   - Application Java prête à l’emploi.
3. **Base de données** :
   - Scripts SQL pour initialiser la base et des données fictives de test.

---

## 7. Acteurs

### Administrateur système
- Ajouter et supprimer les agents liés aux manifestations paranormales en cours.
- Gérer les tickets des manifestations paranormales (ouverture, fermeture).
- Consulter l'historique des manifestations paranormales avec filtres.
- Conseiller les contre-mesures nécessaires.

### Agent de terrain paranormal
- Ajouter ou supprimer des rapports de manifestations paranormales.
- Rédiger les rapports des manifestations paranormales en cours.