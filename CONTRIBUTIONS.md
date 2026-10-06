# USpace — contribution et crédits

USpace est un projet Java de gestion de croisières spatiales réalisé dans le cadre du cours IFT-4006 à l'Université Laval, à l'hiver 2026. Les travaux pratiques successifs développent le domaine, l'API, la persistance et les tests à partir d'une base fournie par le cours.

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
