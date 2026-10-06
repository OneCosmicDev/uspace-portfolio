const CABIN_ATTRIBUTIONS_URL = '/cruises/JUPITER_MOONS_EXPLORATION_2085/cabins*';

describe('Cabin Attributions', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it("n'affiche pas de tableau s'il n'y a pas de cabines attribuées", () => {
    cy.intercept('GET', CABIN_ATTRIBUTIONS_URL, { body: { cabins: [] } });

    cy.get('[data-cy="fetch-cabin-attribution"]').click();

    cy.get('[data-cy="cabin-attribution-details"]').should('not.exist');
  });

  it('affiche les cabines attribuées dans un tableau dans l\'ordre reçu', () => {
    const cabins = [
      { cabinId: 'K1', category: 'DELUXE', bookingId: 'booking-001' },
      { cabinId: 'K2', category: 'DELUXE', bookingId: 'booking-002' },
    ];
    cy.intercept('GET', CABIN_ATTRIBUTIONS_URL, { body: { cabins } });

    cy.get('[data-cy="fetch-cabin-attribution"]').click();

    cy.get('[data-cy="cabin-attribution-details"]').should('have.length', 2);
    cy.get('[data-cy="cabin-attribution-details"]').eq(0).within(() => {
      cy.get('.cabin-id-item').should('have.text', 'K1');
      cy.get('.cabin-category-item').should('have.text', 'DELUXE');
      cy.get('.cabin-bookingId-item').should('have.text', 'booking-001');
    });
    cy.get('[data-cy="cabin-attribution-details"]').eq(1).within(() => {
      cy.get('.cabin-id-item').should('have.text', 'K2');
      cy.get('.cabin-bookingId-item').should('have.text', 'booking-002');
    });
  });

  it('envoie le critère "bookingDateTime" par défaut', () => {
    cy.intercept('GET', CABIN_ATTRIBUTIONS_URL, (req) => {
      expect(req.query['criteria']).to.equal('bookingDateTime');
      req.reply({ body: { cabins: [] } });
    });

    cy.get('[data-cy="fetch-cabin-attribution"]').click();
  });

  it('envoie le critère "travelers" quand il est sélectionné', () => {
    cy.intercept('GET', CABIN_ATTRIBUTIONS_URL, (req) => {
      expect(req.query['criteria']).to.equal('travelers');
      req.reply({ body: { cabins: [] } });
    });

    cy.get('select#criteria').select('travelers');
    cy.get('[data-cy="fetch-cabin-attribution"]').click();
  });
});