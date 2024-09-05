import { entityTableSelector, entityDetailsButtonSelector, entityDetailsBackButtonSelector } from '../../support/entity';

describe('Status e2e test', () => {
  const statusPageUrl = '/status';
  const statusPageUrlPattern = new RegExp('/status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const statusSample = { name: 'favorite' };

  let status;
  let statusType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/status-types',
      body: { name: 'underneath' },
    }).then(({ body }) => {
      statusType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/statuses/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/status-types', {
      statusCode: 200,
      body: [statusType],
    });
  });

  afterEach(() => {
    if (status) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/statuses/${status.id}`,
      }).then(() => {
        status = undefined;
      });
    }
  });

  afterEach(() => {
    if (statusType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/status-types/${statusType.id}`,
      }).then(() => {
        statusType = undefined;
      });
    }
  });

  it('Statuses menu should load Statuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Status').should('exist');
    cy.url().should('match', statusPageUrlPattern);
  });

  describe('Status page', () => {
    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(statusPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Status page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('status');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statusPageUrlPattern);
      });
    });
  });
});
