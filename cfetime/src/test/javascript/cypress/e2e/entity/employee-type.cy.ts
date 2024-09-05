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

describe('EmployeeType e2e test', () => {
  const employeeTypePageUrl = '/employee-type';
  const employeeTypePageUrlPattern = new RegExp('/employee-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const employeeTypeSample = {};

  let employeeType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/employee-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/employee-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/employee-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (employeeType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/employee-types/${employeeType.id}`,
      }).then(() => {
        employeeType = undefined;
      });
    }
  });

  it('EmployeeTypes menu should load EmployeeTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('employee-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EmployeeType').should('exist');
    cy.url().should('match', employeeTypePageUrlPattern);
  });

  describe('EmployeeType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(employeeTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EmployeeType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/employee-type/new$'));
        cy.getEntityCreateUpdateHeading('EmployeeType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeeTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/employee-types',
          body: employeeTypeSample,
        }).then(({ body }) => {
          employeeType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/employee-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [employeeType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(employeeTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details EmployeeType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('employeeType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeeTypePageUrlPattern);
      });

      it('edit button click should load edit EmployeeType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EmployeeType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeeTypePageUrlPattern);
      });

      it('edit button click should load edit EmployeeType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EmployeeType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeeTypePageUrlPattern);
      });

      it('last delete button click should delete instance of EmployeeType', () => {
        cy.intercept('GET', '/api/employee-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('employeeType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeeTypePageUrlPattern);

        employeeType = undefined;
      });
    });
  });

  describe('new EmployeeType page', () => {
    beforeEach(() => {
      cy.visit(`${employeeTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EmployeeType');
    });

    it('should create an instance of EmployeeType', () => {
      cy.get(`[data-cy="name"]`).type('hops from');
      cy.get(`[data-cy="name"]`).should('have.value', 'hops from');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        employeeType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', employeeTypePageUrlPattern);
    });
  });
});
