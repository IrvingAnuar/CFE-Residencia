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

describe('Employee e2e test', () => {
  const employeePageUrl = '/employee';
  const employeePageUrlPattern = new RegExp('/employee(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const employeeSample = {"createdDate":"2024-09-08T12:01:44.507Z"};

  let employee;
  // let status;
  // let user;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/statuses',
      body: {"name":"before"},
    }).then(({ body }) => {
      status = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"bmw.","firstName":"Inés","lastName":"Archuleta Puente","email":"Estela.RamirezBahena86@gmail.com","imageUrl":"woot except","langKey":"ultimately"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/employees+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/employees').as('postEntityRequest');
    cy.intercept('DELETE', '/api/employees/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [status],
    });

  });
   */

  afterEach(() => {
    if (employee) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/employees/${employee.id}`,
      }).then(() => {
        employee = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (status) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/statuses/${status.id}`,
      }).then(() => {
        status = undefined;
      });
    }
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('Employees menu should load Employees page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('employee');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Employee').should('exist');
    cy.url().should('match', employeePageUrlPattern);
  });

  describe('Employee page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(employeePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Employee page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/employee/new$'));
        cy.getEntityCreateUpdateHeading('Employee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/employees',
          body: {
            ...employeeSample,
            status: status,
            created: user,
          },
        }).then(({ body }) => {
          employee = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/employees+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [employee],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(employeePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(employeePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Employee page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('employee');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);
      });

      it('edit button click should load edit Employee page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Employee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);
      });

      it('edit button click should load edit Employee page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Employee');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Employee', () => {
        cy.intercept('GET', '/api/employees/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('employee').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);

        employee = undefined;
      });
    });
  });

  describe('new Employee page', () => {
    beforeEach(() => {
      cy.visit(`${employeePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Employee');
    });

    it.skip('should create an instance of Employee', () => {
      cy.get(`[data-cy="clave"]`).type('17566');
      cy.get(`[data-cy="clave"]`).should('have.value', '17566');

      cy.get(`[data-cy="name"]`).type('piracy');
      cy.get(`[data-cy="name"]`).should('have.value', 'piracy');

      cy.get(`[data-cy="firstSurname"]`).type('briefly');
      cy.get(`[data-cy="firstSurname"]`).should('have.value', 'briefly');

      cy.get(`[data-cy="secondSurname"]`).type('fooey ack clapboard');
      cy.get(`[data-cy="secondSurname"]`).should('have.value', 'fooey ack clapboard');

      cy.get(`[data-cy="createdDate"]`).type('2024-09-08T10:54');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-09-08T10:54');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2024-09-08T07:58');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2024-09-08T07:58');

      cy.get(`[data-cy="status"]`).select(1);
      cy.get(`[data-cy="created"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        employee = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', employeePageUrlPattern);
    });
  });
});
