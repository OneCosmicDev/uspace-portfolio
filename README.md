# USpace — Gestion de croisières spatiales

J’ai travaillé sur USpace avec Patrick Coleman dans le cadre du cours IFT-4006 à l’Université Laval. Au fil de quatre travaux pratiques, j’ai fait évoluer une application de gestion de croisières spatiales en ajoutant des fonctionnalités métier et leurs tests.

**Université Laval · IFT-4006 · Hiver 2026**

**Début documenté : 26 janvier 2026** — [repères chronologiques](PROVENANCE.md#repères-chronologiques)

**Technologies : Java · Hibernate/H2 · Vue · JUnit · Mockito · Cucumber · Cypress**

## Ce que fait le projet

USpace permet de gérer des croisières, des voyageurs, des réservations et plusieurs ressources nécessaires au voyage. Le domaine comprend notamment les comptes, les cabines, les activités et les modules d’hyperpropulsion. La dernière version ajoute une interface Vue et la consultation des manifestes de navettes d’urgence. Le backend Java sépare les règles métier, les services applicatifs, l’API et les mécanismes de persistance.

## Ma contribution

Mes contributions se répartissent sur les quatre travaux pratiques.

- J’ai introduit des catégories polymorphes de voyageurs et leurs règles de réservation.
- J’ai implémenté le domaine des comptes, les repositories en mémoire et Hibernate, les services et l’API, puis intégré les comptes au processus de réservation.
- J’ai ajouté la suppression de compte et ses scénarios d’acceptation Cucumber.
- J’ai contribué à la gestion des modules d’hyperpropulsion et aux tests d’acceptation de l’expérience zéro gravité.
- J’ai développé le manifeste des navettes d’urgence : allocation des passagers, intégration au domaine, service applicatif, DTO et ressource API. J’ai accompagné cette fonctionnalité de tests du domaine, de composants, de scénarios Cucumber et de tests Cypress et Vitest.

Je détaille les fichiers et les références de mon travail dans [CONTRIBUTIONS.md](CONTRIBUTIONS.md).

## Ce que j’ai appris

J’ai appris à faire évoluer un domaine métier sans répartir ses règles dans toutes les couches de l’application. Le travail sur les catégories de voyageurs, les comptes et les navettes m’a amené à utiliser le polymorphisme, les objets de valeur et les repositories pour exprimer les responsabilités de chaque élément.

J’ai également appris à choisir un niveau de test selon ce que je veux vérifier. Un test unitaire cible une règle métier, un test de composant vérifie une collaboration et un scénario d’acceptation décrit un comportement complet. Le passage d’une persistance en mémoire à Hibernate m’a permis d’observer concrètement l’intérêt de séparer le domaine de son stockage.

## Mon équipe et le code fourni

J’ai réalisé les travaux pratiques avec Patrick Coleman. Nous avons fait évoluer le squelette et les ressources pédagogiques fournis dans le cours IFT-4006. Je présente ici la version du TP4, avec le détail de mes contributions aux étapes précédentes.

## Lancer le projet

Prérequis : Java 21, Maven 3, Node.js 22.19 ou supérieur et npm.

Backend, depuis la racine :

```sh
mvn compile
mvn exec:java
```

Frontend, depuis `webapp/` :

```sh
npm ci
npm run dev
```

L’API est disponible à `http://localhost:8181`, l’interface à `http://localhost:5173`. La configuration Hibernate incluse utilise H2 en mémoire.

```sh
mvn test
```

Depuis `webapp/`, `npm run test:unit -- --run` lance Vitest. Les tests Cypress nécessitent le backend et l’interface démarrés.

## État du projet

Les **255 tests Java** et les **10 tests Vitest passent**, et l’interface Vue compile. Les scénarios Cypress restent à exécuter.
