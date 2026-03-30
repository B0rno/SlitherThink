# SlitherThink

**SlitherThink** est un jeu de puzzle logique SlitherLink développé en Java avec JavaFX. 

![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.6-007396?logo=java)
![Maven](https://img.shields.io/badge/Maven-3.6+-C71A36?logo=apache-maven)
![JUnit](https://img.shields.io/badge/JUnit-5-25A162?logo=junit5)

---

## Table des matières

- Description
- Fonctionnalités
- Technologies
- Prérequis
- Installation
- Lancement du jeu
- Structure du projet
- Architecture
- Tests
- Créateurs

---

## Description

**SlitherThink** est une implémentation du célèbre puzzle SlitherLink où le joueur doit tracer une boucle fermée unique en respectant les indices numériques placés sur la grille. Chaque chiffre indique combien de segments doivent entourer cette case.

Projet académique réalisé dans le cadre du module GL2 (L3 Informatique)
- Université : Le Mans Université
- Période : Janvier 2026 - Avril 2026

---

## Fonctionnalités

### Modes de jeu
- **Mode Aventure** : 12 parties progressives avec difficulté croissante
- **Tutoriel interactif** : Grille 4x4 pour apprendre les règles
- **Sauvegarde automatique** : Progression et scores enregistrés

### Système d'aide intelligent
- **16 techniques de résolution** cataloguées par niveau (débutant, basique, confirmé)
- **Aides contextuelles** pendant la partie (3 aides maximum par défaut)
- **Page Techniques** : descriptions détaillées de chaque méthode

### Progression et scores
- **Chronométrage** avec pause/reprise
- **Système d'étoiles** basé sur le temps et le nombre d'aides
  - 3 étoiles : temps inférieur ou égal à 5 min ET 0 aides
  - 2 étoiles : temps inférieur ou égal à 5 min ET maximum 3 aides
  - 1 étoile : partie complétée
- **Leaderboards** : Top 10 par partie, trié par étoiles puis temps
- **Sauvegarde automatique** dans Score.csv

### Interface utilisateur
- **Interface JavaFX** en plein écran
- **Grille interactive** avec système de clic pour tracer/effacer les traits
- **Chronomètre** affiché en temps réel
- **Page de fin** avec statistiques et high scores du niveau

---

## Technologies

### Core
- **Java 17** - Langage de programmation
- **JavaFX 21.0.6** - Framework d'interface graphique
- **Maven** - Gestion de projet et build

### Bibliothèques
- **GSON 2.13.2** - Manipulation de fichiers JSON (grilles de jeu)
- **OpenCSV 5.8** - Gestion des scores CSV
- **JUnit 5** - Tests unitaires (75 tests)

---

## Prérequis

Avant de commencer, assurez-vous d'avoir installé :

### 1. **Java 17** (ou supérieur)
- **Télécharger** : [oracle.com/java](https://www.oracle.com/java/technologies/downloads/)
- **Vérifier** l'installation :
  ```bash
  java -version
  javac -version
  ```

### 2. **Maven 3.6** (ou supérieur)
- **Télécharger** : [maven.apache.org](https://maven.apache.org/download.cgi)
- **Vérifier** l'installation :
  ```bash
  mvn -version
  ```

---

## Installation

### Étape 1 : Cloner le projet

```bash
git clone https://github.com/votre-username/SlitherThink.git
cd SlitherThink
```

---

### Étape 2 : Compiler le projet

```bash
mvn clean package
```

Cela génère le fichier JAR exécutable dans le dossier `target/`.

> **Info** : Maven télécharge automatiquement toutes les dépendances (JavaFX, GSON, OpenCSV, JUnit).

> **Optionnel** : Pour nettoyer et recompiler depuis zéro :
> ```bash
> mvn clean install
> ```

---

## Lancement du jeu

### Méthode 1 : Via le fichier JAR (recommandé)

```bash
java -jar target/SlitherThink-1.0.jar
```

Le jeu se lance en plein écran automatiquement.

---

### Méthode 2 : Via Maven

```bash
mvn javafx:run
```


---

## Structure du projet

```
SlitherThink/
├── src/
│   ├── main/
│   │   ├── java/com/lmu/SlitherThink/
│   │   │   ├── Grille/          # Matrice, Case, Trait, ValeurTrait
│   │   │   ├── Partie/          # Moteur de jeu, Score, Profil, EtatPartie
│   │   │   ├── Helper/          # 16 techniques d'aide
│   │   │   ├── boutonsAction/   # Contrôleurs JavaFX
│   │   │   └── save/            # Gestion CSV et JSON
│   │   └── resources/
│   │       ├── fxml/            # Fichiers d'interface JavaFX
│   │       ├── GrilleJson/      # Grilles de jeu (tutoriel + 12 parties)
│   │       ├── images/          # Assets graphiques
│   │       ├── technique.json   # Descriptions des 16 techniques
│   │       └── save/            # Templates de sauvegarde
│   └── test/                    # 75 tests unitaires JUnit 5
├── save/                        # Données actives (créé au premier lancement)
│   ├── Score.csv               # Scores des joueurs
│   ├── saveJoueur/             # Progression par profil
│   └── saveGrille/             # Parties en cours
├── pom.xml                      # Configuration Maven
└── README.md
```
---

## Architecture

### Design Patterns utilisés

- **Observer** : Communication entre la partie et l'interface
  - Notifications de victoire, changements d'état, aides utilisées
- **State** : Gestion des états de la partie
  - INIT, EN_COURS, PAUSE, TERMINE
- **Strategy** : Système d'aides avec 16 techniques interchangeables
  - Chaque technique implémente `StrategieAide`
- **Facade** : Simplification de l'API de la matrice via la classe `Partie`
- **Singleton** : Chargement unique des descriptions JSON

### Packages principaux

| Package | Responsabilité |
|---------|----------------|
| **Grille** | Représentation de la grille et des traits |
| **Partie** | Moteur de jeu, score, profil joueur |
| **Helper** | Intelligence avec techniques de résolution |
| **boutonsAction** | Contrôleurs JavaFX pour chaque page |
| **save** | Persistance des données (CSV et JSON) |

---

## Tests

Le projet contient **75 tests unitaires** couvrant :

- Logique de grille (Matrice, Case, Trait, ValeurTrait)
- Système de score et chronométrage avec pause/reprise
- États de partie et transitions
- Système d'aides et techniques
- Intégration complète

### Exécuter les tests

```bash
mvn test
```

### Exécuter les tests avec rapport détaillé

```bash
mvn test -Dtest=NomDuTest
```

> **Résultat attendu** : Tests run: 75, Failures: 0, Errors: 0, Skipped: 0

---



## Créateurs

- **Enzo Desfaudais** (B0rno) - enzo.desfaudais1@gmail.com
- **Thibaut Gasnier** (tibogas) - tibo.gasnier@gmail.com
- **Nathan Pasquier** (Natp24109)
- **Lucas Abeka-Doth** (lucasabeka) - lucasabekadoth@gmail.com
- **Logan Evenisse** (llogeve2) - evel0577@gmail.com
- **Ibrahim Sapiev** (isapiev)
- **Ilann Thourault** (IlannThourault) - ilann.thourault@gmail.com
- **Arthur Baralle** (Asorya) - arthur.baralle@gmail.com

Projet académique - GL2 (L3 Informatique)
Le Mans Université - 2026