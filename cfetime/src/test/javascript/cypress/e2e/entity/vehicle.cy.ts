import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Vehicle e2e test', () => {
  const vehiclePageUrl = '/vehicle';
  const vehiclePageUrlPattern = new RegExp('/vehicle(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const vehicleSample = { descripcion: 'likewise', model: 'tailgate', plates: 'brr than chubby' };

  let vehicle;
  let vehicleType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/vehicle-types',
      body: { descripcion: 'radicalise' },
    }).then(({ body }) => {
      vehicleType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/vehicles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vehicles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vehicles/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/vehicle-types', {
      statusCode: 200,
      body: [vehicleType],
    });
  });

  afterEach(() => {
    if (vehicle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vehicles/${vehicle.id}`,
      }).then(() => {
        vehicle = undefined;
      });
    }
  });

  afterEach(() => {
    if (vehicleType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vehicle-types/${vehicleType.id}`,
      }).then(() => {
        vehicleType = undefined;
      });
    }
  });

  it('Vehicles menu should load Vehicles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vehicle');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Vehicle').should('exist');
    cy.url().should('match', vehiclePageUrlPattern);
  });

  describe('Vehicle page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vehiclePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Vehicle page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vehicle/new$'));
        cy.getEntityCreateUpdateHeading('Vehicle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', vehiclePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vehicles',
          body: {
            ...vehicleSample,
            vehicleType: vehicleType,
          },
        }).then(({ body }) => {
          vehicle = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vehicles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [vehicle],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(vehiclePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Vehicle page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vehicle');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', vehiclePageUrlPattern);
      });

      it('edit button click should load edit Vehicle page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Vehicle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', vehiclePageUrlPattern);
      });

      it('edit button click should load edit Vehicle page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Vehicle');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', vehiclePageUrlPattern);
      });

      it('last delete button click should delete instance of Vehicle', () => {
        cy.intercept('GET', '/api/vehicles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('vehicle').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', vehiclePageUrlPattern);

        vehicle = undefined;
      });
    });
  });

  describe('new Vehicle page', () => {
    beforeEach(() => {
      cy.visit(`${vehiclePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Vehicle');
    });

    it('should create an instance of Vehicle', () => {
      cy.get(`[data-cy="descripcion"]`).type('woot');
      cy.get(`[data-cy="descripcion"]`).should('have.value', 'woot');

      cy.get(`[data-cy="model"]`).type('drat high-rise');
      cy.get(`[data-cy="model"]`).should('have.value', 'drat high-rise');

      cy.get(`[data-cy="plates"]`).type('directive on unfortunately');
      cy.get(`[data-cy="plates"]`).should('have.value', 'directive on unfortunately');

      cy.get(`[data-cy="vehicleType"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        vehicle = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', vehiclePageUrlPattern);
    });
  });
});
