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

describe('VehicleUsage e2e test', () => {
  const vehicleUsagePageUrl = '/vehicle-usage';
  const vehicleUsagePageUrlPattern = new RegExp('/vehicle-usage(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const vehicleUsageSample = {"startDate":"2024-09-04T12:38:35.274Z"};

  let vehicleUsage;
  // let vehicle;
  // let employee;
  // let status;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/vehicles',
      body: {"descripcion":"bah versus","model":"bid","plates":"toy"},
    }).then(({ body }) => {
      vehicle = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/employees',
      body: {"clave":28142,"name":"carelessly","firstSurname":"questionable ack wonderful","secondSurname":"ram","createdDate":"2024-09-04T07:53:06.059Z","lastModifiedDate":"2024-09-04T15:23:06.516Z"},
    }).then(({ body }) => {
      employee = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/statuses',
      body: {"name":"the"},
    }).then(({ body }) => {
      status = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/vehicle-usages+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vehicle-usages').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vehicle-usages/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/vehicles', {
      statusCode: 200,
      body: [vehicle],
    });

    cy.intercept('GET', '/api/employees', {
      statusCode: 200,
      body: [employee],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [status],
    });

  });
   */

  afterEach(() => {
    if (vehicleUsage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vehicle-usages/${vehicleUsage.id}`,
      }).then(() => {
        vehicleUsage = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (vehicle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vehicles/${vehicle.id}`,
      }).then(() => {
        vehicle = undefined;
      });
    }
    if (employee) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/employees/${employee.id}`,
      }).then(() => {
        employee = undefined;
      });
    }
    if (status) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/statuses/${status.id}`,
      }).then(() => {
        status = undefined;
      });
    }
  });
   */

  it('VehicleUsages menu should load VehicleUsages page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vehicle-usage');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VehicleUsage').should('exist');
    cy.url().should('match', vehicleUsagePageUrlPattern);
  });

  describe('VehicleUsage page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vehicleUsagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VehicleUsage page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vehicle-usage/new$'));
        cy.getEntityCreateUpdateHeading('VehicleUsage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleUsagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vehicle-usages',
          body: {
            ...vehicleUsageSample,
            vehicle: vehicle,
            employee: employee,
            status: status,
          },
        }).then(({ body }) => {
          vehicleUsage = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vehicle-usages+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [vehicleUsage],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(vehicleUsagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(vehicleUsagePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details VehicleUsage page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vehicleUsage');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleUsagePageUrlPattern);
      });

      it('edit button click should load edit VehicleUsage page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleUsage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleUsagePageUrlPattern);
      });

      it('edit button click should load edit VehicleUsage page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleUsage');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleUsagePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of VehicleUsage', () => {
        cy.intercept('GET', '/api/vehicle-usages/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('vehicleUsage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleUsagePageUrlPattern);

        vehicleUsage = undefined;
      });
    });
  });

  describe('new VehicleUsage page', () => {
    beforeEach(() => {
      cy.visit(`${vehicleUsagePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VehicleUsage');
    });

    it.skip('should create an instance of VehicleUsage', () => {
      cy.get(`[data-cy="startDate"]`).type('2024-09-04T22:28');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-09-04T22:28');

      cy.get(`[data-cy="endDate"]`).type('2024-09-04T11:15');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-09-04T11:15');

      cy.get(`[data-cy="comments"]`).type('jaggedly');
      cy.get(`[data-cy="comments"]`).should('have.value', 'jaggedly');

      cy.get(`[data-cy="vehicle"]`).select(1);
      cy.get(`[data-cy="employee"]`).select(1);
      cy.get(`[data-cy="status"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        vehicleUsage = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', vehicleUsagePageUrlPattern);
    });
  });
});
