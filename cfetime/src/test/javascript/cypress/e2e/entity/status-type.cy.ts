import { entityTableSelector, entityDetailsButtonSelector, entityDetailsBackButtonSelector } from '../../support/entity';

describe('StatusType e2e test', () => {
  const statusTypePageUrl = '/status-type';
  const statusTypePageUrlPattern = new RegExp('/status-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const statusTypeSample = { name: 'socialism amongst' };

  let statusType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/status-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/status-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/status-types/*').as('deleteEntityRequest');
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

  it('StatusTypes menu should load StatusTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('status-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StatusType').should('exist');
    cy.url().should('match', statusTypePageUrlPattern);
  });

  describe('StatusType page', () => {
    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(statusTypePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details StatusType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('statusType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statusTypePageUrlPattern);
      });
    });
  });
});
