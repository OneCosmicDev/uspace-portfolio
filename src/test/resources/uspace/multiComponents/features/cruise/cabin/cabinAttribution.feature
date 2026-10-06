#language: fr

Fonctionnalité: Attribution de cabines

    Scénario: Un critère non-supporté retourne une erreur
        Quand on récupère les attributions de cabine avec le critère "non-supporté"
        Alors le code de statut 400 est obtenu
        Et l'erreur "INVALID_CABIN_ATTRIBUTION_CRITERIA" est obtenue

    Scénario: Les cabines sont attribuées selon la date de réservation
        Etant donné une réservation "DELUXE" à "2084-01-01T08:00" avec 1 adulte, 0 enfant et 0 senior
        Et une réservation "DELUXE" à "2084-06-01T08:00" avec 1 adulte, 0 enfant et 0 senior
        Quand on récupère les attributions de cabine avec le critère "bookingDateTime"
        Alors le code de statut 200 est obtenu
        Et la première attribution de cabine correspond à la réservation numéro 1

    Scénario: Les cabines sont attribuées selon le nombre de voyageurs
        Etant donné une réservation "DELUXE" à "2084-01-01T08:00" avec 1 adulte, 0 enfant et 0 senior
        Et une réservation "DELUXE" à "2084-01-01T09:00" avec 3 adulte, 0 enfant et 0 senior
        Quand on récupère les attributions de cabine avec le critère "travelers"
        Alors le code de statut 200 est obtenu
        Et la première attribution de cabine correspond à la réservation numéro 2

    Scénario: Aucune cabine n'est attribuée s'il n'y a pas de réservations
        Quand on récupère les attributions de cabine avec le critère "bookingDateTime"
        Alors le code de statut 200 est obtenu
        Et aucune attribution de cabine n'est retournée