#language: fr

Fonctionnalité: Manifeste des navettes d'urgence

    Scénario: Aucune navette d'urgence si aucun passager
        Quand on récupère le manifeste des navettes d'urgence
        Alors le code de statut 200 est obtenu
        Et aucune navette d'urgence n'est dans le manifeste
        Et le coût total du manifeste est 0

    Scénario: Un voyageur est assigné à une navette de type RESCUE_SHIP
        Etant donné une réservation avec 1 adulte est créée
        Quand on récupère le manifeste des navettes d'urgence
        Alors le code de statut 200 est obtenu
        Et le manifeste contient une navette de type "RESCUE_SHIP"
        Et la navette contient le voyageur de la réservation

    Scénario: Un membre d'équipage est assigné à une navette de type RESCUE_SHIP
        Etant donné un membre d'équipage est ajouté à la croisière
        Quand on récupère le manifeste des navettes d'urgence
        Alors le code de statut 200 est obtenu
        Et le manifeste contient une navette de type "RESCUE_SHIP"
        Et la navette contient le membre d'équipage

    Scénario: Croisière inexistante
        Quand on récupère le manifeste des navettes d'urgence de la croisière "CRUISE_NOT_FOUND"
        Alors le code de statut 404 est obtenu
        Et l'erreur "CRUISE_NOT_FOUND" est obtenue
