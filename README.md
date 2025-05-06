# Gestionnaire de Dépenses avec Jetpack Compose

## Développement d’applications mobiles

---

## Objectif
Développer une application Android permettant aux utilisateurs de suivre leurs dépenses et revenus, avec des fonctionnalités de gestion et de persistance des données.

**Concepts Utilisés :**
- Programmation Orientée Objet (POO)
- Authentification avec persistance des données
- Navigation entre écrans
- Architecture MVVM
- Persistance des données avec SharedPreferences
- Tests unitaires

---

## 1. Description de l'Application

**Nom de l'Application :** Gestionnaire de Dépenses

L’application permettra de :
- Se connecter via un écran de login.
- Ajouter, afficher, modifier et supprimer des transactions.
- Catégoriser les transactions (dépenses/revenus).
- Sauvegarder les données localement à l'aide de SharedPreferences.
- Naviguer entre plusieurs écrans : Login → Accueil → Liste des Transactions → Détails d’une Transaction.

---

## 2. Fonctionnalités Principales

### 2.1. Écran de Login
- Entrées utilisateur : Email et mot de passe (stockés en dur pour la validation).
- Validation des identifiants :
  - Afficher un message d'erreur si les informations sont incorrectes.
- Persistance de l'état de connexion :
  - Utilisation de SharedPreferences.
  - Redirection automatique si l’utilisateur est déjà connecté.

### 2.2. Écran d'Accueil
- Message de bienvenue avec le nom de l'utilisateur.
- Boutons pour :
  - Accéder à la liste des transactions.
  - Se déconnecter (efface l’état de connexion et les données sauvegardées).

### 2.3. Liste des Transactions
- Affichage des transactions sous forme de liste.
- Filtrage par type : Dépense ou Revenu.
- Affichage par transaction :
  - Montant
  - Catégorie
  - Date
- Boutons pour :
  - Modifier une transaction
  - Supprimer une transaction
- Bouton pour ajouter une nouvelle transaction.

### 2.4. Détails d’une Transaction
- Modification d'une transaction existante :
  - Montant
  - Description
  - Catégorie (Alimentation, Transport, Logement, etc.)
  - Type (Dépense ou Revenu)
- Utilisation recommandée des énumérations (`enum`).

### 2.5. Ajouter une Nouvelle Transaction
- Formulaire avec :
  - Montant ($)
  - Description
  - Catégorie (Drop Down)
  - Type (Dépense ou Revenu - Drop Down)
- Persistance locale après enregistrement.

### 2.6. Persistance des Données
- Stockage local avec SharedPreferences en format JSON.
- Sérialisation et désérialisation des données avec Gson.
- Conservation des données après fermeture de l'application.

---

## 3. Architecture de l'Application

### MVVM

**Model :**
- Classe `Transaction` : id, montant, description, catégorie, type, date.

**ViewModel :**
- `TransactionViewModel` pour la gestion des transactions.
- `AuthViewModel` pour la gestion de l'authentification.

**View (Composable) :**
- Écrans composables utilisant les ViewModels.

---

## 4. Ajout des Tests Unitaires

**Outils Utilisés :**
- JUnit : tests des ViewModels et logique métier.
- Mockito : mocks pour interactions entre composants.

**Tests Réalisés :**
- Ajout et modification de transactions.
- Suppression de transactions.
- Filtrage des transactions.
- Sauvegarde et restauration de données après fermeture de l'application.
