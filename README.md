# uspace

Gestion de croisières spatiales : réservations, comptes, équipements et navettes d’urgence.

**Java · Hibernate/H2 · Vue · JUnit · Cucumber · Cypress**

Copie portfolio d’un projet scolaire de Juan José Castilla Manrique ([OneCosmicDev](https://github.com/OneCosmicDev)). Les contributions de l’équipe et le matériel fourni par le cours sont crédités ci-dessous.

## Ma contribution — Juan José Castilla Manrique

J'ai travaillé sur plusieurs fonctionnalités métier au fil des quatre travaux pratiques. L'historique local permet de relier les éléments suivants à mes commits.

- **Catégories de voyageurs** : introduction de catégories polymorphes et de règles de réservation propres à chaque catégorie. TP1, commit `e57b10f`.
- **Comptes et réservation** : implémentation du domaine Account, des repositories en mémoire et Hibernate, de la couche application et de l'API REST, puis intégration des comptes aux réservations. TP2, commits `013aed4`, `b4c3480`, `69caf17` et `6a1ba24`.
- **Suppression de compte** : implémentation de la fonctionnalité avec des scénarios Cucumber. TP3, commit `0326a08`.
- **Hyperpropulsion et tests d'acceptation** : contribution à la gestion des modules d'hyperpropulsion et aux scénarios Cucumber de l'expérience zéro gravité. TP3, commits `5e985dd` et `a99f7ff`.
- **Manifeste des navettes d'urgence** : implémentation du modèle d'allocation des passagers, de son intégration au domaine Cruise, du service, des DTO et de la ressource API. Ajout de tests du domaine, de composants, de scénarios Cucumber et de tests Cypress et Vitest. TP4, commits `63d140c` et `831eb24`.

Les identifiants de commits renvoient aux historiques locaux des différents TP ; ils ne sont pas tous contenus dans l'historique de la seule version TP4. Les PR d'origine ne sont actuellement pas consultables avec l'accès GitHub disponible.

## Équipe et crédits

Les historiques examinés identifient Juan José Castilla Manrique et Patrick Coleman parmi les contributeurs aux travaux. Les noms JuanAstroDev et The_OnlyJuanDev correspondent au compte [OneCosmicDev](https://github.com/OneCosmicDev).

Le squelette initial et les ressources pédagogiques sont fournis par le cours IFT-4006. L'architecture et l'application complètes résultent de cette base et du travail collectif ; les fonctionnalités ci-dessus décrivent mon apport spécifique.


Les références de PR et de commits pointent vers les dépôts pédagogiques d’origine, dont l’accès peut être restreint. La présente copie possède son propre historique de publication.

## Démarrer le projet

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

## État de cette publication

Cette version présente le travail scolaire et ses limites. Elle ne correspond pas à un service hébergé ni à un engagement de maintenance. Voir [PROVENANCE.md](PROVENANCE.md) pour la source, les adaptations de publication et les références vers le code.

## Vérifications du 6 octobre 2026

255 tests Java et 10 tests Vitest réussis. Compilation de l’interface Vue réussie. Les scénarios Cypress n’ont pas été exécutés.
