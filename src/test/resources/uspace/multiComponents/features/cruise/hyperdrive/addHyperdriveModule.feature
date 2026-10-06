#language: fr

Fonctionnalité: Ajouter un module d'hyperpropulsion

    Scénario: Un module d'hyperpropulsion valide est ajouté
        Quand on ajoute le module d'hyperpropulsion "HY-123-Z", le niveau de puissance 30, 1 jour d'activation à partir du "2085-01-26"
        Alors le code de statut 201 est obtenu
        Et le module d'hyperpropulsion "HY-123-Z" existe