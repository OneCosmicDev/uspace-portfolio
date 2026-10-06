const EMERGENCY_SHUTTLES_URL = '/cruises/JUPITER_MOONS_EXPLORATION_2085/emergencyShuttles';

describe('Emergency Shuttles Manifest', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it("n'affiche pas les navettes si le manifeste est vide", () => {
    cy.intercept('GET', EMERGENCY_SHUTTLES_URL, { body: { cost: 0, emergencyShuttles: [] } });

    cy.get('[data-cy="fetch-emergency-shuttles-manifest"]').click();

    cy.get('[data-cy="emergency-shuttle"]').should('not.exist');
  });

  it('le bouton devient rouge si le manifeste obtenu est vide', () => {
    cy.intercept('GET', EMERGENCY_SHUTTLES_URL, { body: { cost: 0, emergencyShuttles: [] } });

    cy.get('[data-cy="fetch-emergency-shuttles-manifest"]').click();

    cy.get('[data-cy="fetch-emergency-shuttles-manifest"]').should('have.class', 'red-button');
  });

  it('affiche les navettes dans l\'ordre reçu', () => {
    const shuttles = [
      { type: 'RESCUE_SHIP', travelers: [], crewMembers: [] },
      { type: 'STANDARD_SHUTTLE', travelers: [], crewMembers: [] },
    ];
    cy.intercept('GET', EMERGENCY_SHUTTLES_URL, { body: { cost: 200000, emergencyShuttles: shuttles } });

    cy.get('[data-cy="fetch-emergency-shuttles-manifest"]').click();

    cy.get('[data-cy="emergency-shuttle"]').should('have.length', 2);
    cy.get('[data-cy="emergency-shuttle"]').eq(0).contains('RESCUE_SHIP');
    cy.get('[data-cy="emergency-shuttle"]').eq(1).contains('STANDARD_SHUTTLE');
  });

  it('affiche le coût total du manifeste', () => {
    const shuttles = [{ type: 'RESCUE_SHIP', travelers: [], crewMembers: [] }];
    cy.intercept('GET', EMERGENCY_SHUTTLES_URL, { body: { cost: 150000, emergencyShuttles: shuttles } });

    cy.get('[data-cy="fetch-emergency-shuttles-manifest"]').click();

    cy.get('[data-cy="total-cost"]').should('have.text', '150000');
  });
});
