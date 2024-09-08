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

describe('EmployeeAttendance e2e test', () => {
  const employeeAttendancePageUrl = '/employee-attendance';
  const employeeAttendancePageUrlPattern = new RegExp('/employee-attendance(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const employeeAttendanceSample = {"attendanceDate":"2024-09-08","checkInTime":"2024-09-08T09:56:38.990Z"};

  let employeeAttendance;
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
      url: '/api/employees',
      body: {"clave":29302,"name":"generously manifestation","firstSurname":"seriously likewise","secondSurname":"setback boohoo accelerant","createdDate":"2024-09-08T12:56:47.579Z","lastModifiedDate":"2024-09-08T08:55:24.986Z"},
    }).then(({ body }) => {
      employee = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/statuses',
      body: {"name":"censor patiently"},
    }).then(({ body }) => {
      status = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/employee-attendances+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/employee-attendances').as('postEntityRequest');
    cy.intercept('DELETE', '/api/employee-attendances/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
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
    if (employeeAttendance) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/employee-attendances/${employeeAttendance.id}`,
      }).then(() => {
        employeeAttendance = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
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

  it('EmployeeAttendances menu should load EmployeeAttendances page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('employee-attendance');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EmployeeAttendance').should('exist');
    cy.url().should('match', employeeAttendancePageUrlPattern);
  });

  describe('EmployeeAttendance page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(employeeAttendancePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EmployeeAttendance page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/employee-attendance/new$'));
        cy.getEntityCreateUpdateHeading('EmployeeAttendance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeeAttendancePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/employee-attendances',
          body: {
            ...employeeAttendanceSample,
            employee: employee,
            status: status,
          },
        }).then(({ body }) => {
          employeeAttendance = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/employee-attendances+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [employeeAttendance],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(employeeAttendancePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(employeeAttendancePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details EmployeeAttendance page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('employeeAttendance');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeeAttendancePageUrlPattern);
      });

      it('edit button click should load edit EmployeeAttendance page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EmployeeAttendance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeeAttendancePageUrlPattern);
      });

      it('edit button click should load edit EmployeeAttendance page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EmployeeAttendance');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeeAttendancePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of EmployeeAttendance', () => {
        cy.intercept('GET', '/api/employee-attendances/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('employeeAttendance').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeeAttendancePageUrlPattern);

        employeeAttendance = undefined;
      });
    });
  });

  describe('new EmployeeAttendance page', () => {
    beforeEach(() => {
      cy.visit(`${employeeAttendancePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EmployeeAttendance');
    });

    it.skip('should create an instance of EmployeeAttendance', () => {
      cy.get(`[data-cy="attendanceDate"]`).type('2024-09-08');
      cy.get(`[data-cy="attendanceDate"]`).blur();
      cy.get(`[data-cy="attendanceDate"]`).should('have.value', '2024-09-08');

      cy.get(`[data-cy="checkInTime"]`).type('2024-09-08T08:45');
      cy.get(`[data-cy="checkInTime"]`).blur();
      cy.get(`[data-cy="checkInTime"]`).should('have.value', '2024-09-08T08:45');

      cy.get(`[data-cy="checkOutTime"]`).type('2024-09-08T12:19');
      cy.get(`[data-cy="checkOutTime"]`).blur();
      cy.get(`[data-cy="checkOutTime"]`).should('have.value', '2024-09-08T12:19');

      cy.get(`[data-cy="comments"]`).type('above compound adviser');
      cy.get(`[data-cy="comments"]`).should('have.value', 'above compound adviser');

      cy.get(`[data-cy="employee"]`).select(1);
      cy.get(`[data-cy="status"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        employeeAttendance = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', employeeAttendancePageUrlPattern);
    });
  });
});
