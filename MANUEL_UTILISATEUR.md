# Manuel Utilisateur - SlitherThink

**Le Mans Université**  
**Licence Informatique - 3ème année**  
**Génie Logiciel 2 - Projet SlitherThink**

**Équipe de développement :**
*   Enzo Desfaudais (B0rno)
*   Thibaut Gasnier (tibogas)
*   Nathan Pasquier (Natp24109)
*   Lucas Abeka-Doth (lucasabeka)
*   Logan Evenisse (llogeve2)
*   Ibrahim Sapiev (isapiev)
*   Ilann Thourault (IlannThourault)
*   Arthur Baralle (Asorya)

**Édition du :** 24 mars 2026

---

## Table des matières
[I - Présentation Générale](#i---présentation-générale)  
&nbsp;&nbsp;&nbsp;&nbsp;[I.1 - Concept de SlitherThink](#i1---concept-de-slitherthink)  
&nbsp;&nbsp;&nbsp;&nbsp;[I.2 - Rôle de ce document](#i2---rôle-de-ce-document)  
[II - Mise en route](#ii---mise-en-route)  
[III - Navigation et Interface](#iii---navigation-et-interface)  
&nbsp;&nbsp;&nbsp;&nbsp;[III.1 - Accueil et Navigation](#iii1---accueil-et-navigation)  
&nbsp;&nbsp;&nbsp;&nbsp;[III.2 - Identification (Pseudo)](#iii2---identification-pseudo)  
&nbsp;&nbsp;&nbsp;&nbsp;[III.3 - Sélection du défi](#iii3---sélection-du-défi)  
&nbsp;&nbsp;&nbsp;&nbsp;[III.4 - Le Plateau de Jeu](#iii4---le-plateau-de-jeu)  
&nbsp;&nbsp;&nbsp;&nbsp;[III.5 - Configuration et Pause](#iii5---configuration-et-pause)  
[IV - Aspects Techniques](#iv---aspects-techniques)  
&nbsp;&nbsp;&nbsp;&nbsp;[IV.1 - Gestion des fichiers de sauvegarde](#iv1---gestion-des-fichiers-de-sauvegarde)  
&nbsp;&nbsp;&nbsp;&nbsp;[IV.2 - Architecture des données (JSON)](#iv2---architecture-des-données-json)

---

## I - Présentation Générale
Bienvenue dans l'univers de **SlitherThink** ! Ce manuel a été conçu comme un compagnon de route pour vous aider à maîtriser toutes les subtilités de notre jeu de réflexion. Que vous fassiez vos premiers pas ou que vous soyez un habitué des casse-têtes logiques, vous trouverez ici toutes les clés pour une expérience optimale.

### I.1 ) Concept de SlitherThink
**SlitherThink** revisite le classique *Slitherlink* en y ajoutant une interface moderne et des outils d'assistance intelligents. Le but est simple mais captivant : relier des points pour former une boucle unique, tout en jonglant avec les contraintes numériques imposées par la grille. Notre application, développée en Java (JavaFX), privilégie la clarté visuelle et la fluidité pour laisser toute la place à votre réflexion.

### I.2 ) Rôle de ce document
Ce manuel vous fournit l'essentiel pour exploiter pleinement les capacités de **SlitherThink** :
*   Procédure de lancement et d'installation.
*   Exploration détaillée des différents menus.
*   Guide de gameplay et description des aides à la résolution.

---

## II - Mise en route
Plusieurs méthodes s'offrent à vous pour lancer l'application selon votre environnement de développement :

**Utilisation de Maven (recommandé) :**
```bash
mvn javafx:run
```

**Utilisation du Makefile :**
```bash
make run
```

**Lancement direct (si JAR disponible) :**
```bash
java -jar SlitherThink.jar
```

---

## III - Navigation et Interface

### III.1 ) Accueil et Navigation
Dès l'ouverture, l'écran d'accueil vous oriente vers les parties essentielles du jeu. C'est ici que vous pourrez lancer une nouvelle aventure, consulter les scores mondiaux ou ajuster vos préférences.

### III.2 ) Identification (Pseudo)
L'accès au jeu nécessite la saisie d'un **Pseudo**. Cette étape est cruciale car elle lie vos exploits en mode Aventure et vos records dans le classement à votre identité de joueur.

### III.3 ) Sélection du défi
Une fois identifié, vous pouvez choisir la nature de votre défi :
*   **Aventure** : Un parcours fléché à travers des niveaux de difficulté croissante.
*   **Partie Libre** : Sélectionnez manuellement votre niveau :
    *   **Facile** : Idéal pour se chauffer l'esprit.
    *   **Moyen** : Pour les joueurs en quête d'équilibre.
    *   **Difficile** : Des grilles denses pour les stratèges aguerris.

### III.4 ) Le Plateau de Jeu
C'est ici que votre logique prend vie.

*   **Le But** : Tracer une boucle fermée géante sans aucun croisement. Les chiffres dans les cases dictent le nombre exact de segments qui doivent les entourer (de 0 à 3).
*   **Interactions à la souris** :
    *   **Clic simple** : Trace un trait (compris dans la boucle).
    *   **Double-clic** : Marque une croix (zone exclue de la boucle).
    *   **Triple-clic** : Efface le segment pour revenir à l'état initial.

**Outils d'assistance :**
*   **Système d'Aide** : Une aide ponctuelle est disponible (3 fois par partie) pour débloquer une situation complexe.
*   **Moniteurs** : Gardez un œil sur votre chronomètre et votre compteur de coups pour optimiser votre score final.

### III.5 ) Configuration et Pause
Le menu **Pause**, accessible en plein jeu, permet d'arrêter temporairement la partie pour accéder aux :
*   **Options** :
    *   **Son** : Gestion de l'audio.
    *   **Aide au placement** : Option "Croix Automatique" pour simplifier la saisie.
    *   **Langue** : Basculez entre le Français et l'Anglais à tout moment.
*   **Actions** : Reprenez votre progression ou quittez vers le menu principal.

---

## IV - Aspects Techniques

### IV.1 ) Gestion des fichiers de sauvegarde
Sous Windows, vos données de profil et vos scores sont stockés localement (généralement dans vos données d'application). Il est déconseillé de modifier ces fichiers manuellement pour éviter toute perte de progression.

### IV.2 ) Architecture des données (JSON)
Les configurations de grilles et les résolutions logiques de **SlitherThink** reposent sur des fichiers **JSON**. Cette structure permet une grande flexibilité dans l'intégration de nouveaux défis et assure la stabilité des ressources graphiques et logiques.

---

Nous espérons que **SlitherThink** vous offrira de longues heures de réflexion stimulante. Bonne chance !
