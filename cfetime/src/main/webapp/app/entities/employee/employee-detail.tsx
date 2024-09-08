import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './employee.reducer';

export const EmployeeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const employeeEntity = useAppSelector(state => state.employee.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="employeeDetailsHeading">
          <Translate contentKey="cfetimeApp.employee.detail.title">Employee</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="cfetimeApp.employee.id">Id</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.id}</dd>
          <dt>
            <span id="clave">
              <Translate contentKey="cfetimeApp.employee.clave">Clave</Translate>
            </span>
            <UncontrolledTooltip target="clave">
              <Translate contentKey="cfetimeApp.employee.help.clave" />
            </UncontrolledTooltip>
          </dt>
          <dd>{employeeEntity.clave}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cfetimeApp.employee.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="cfetimeApp.employee.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{employeeEntity.name}</dd>
          <dt>
            <span id="firstSurname">
              <Translate contentKey="cfetimeApp.employee.firstSurname">First Surname</Translate>
            </span>
            <UncontrolledTooltip target="firstSurname">
              <Translate contentKey="cfetimeApp.employee.help.firstSurname" />
            </UncontrolledTooltip>
          </dt>
          <dd>{employeeEntity.firstSurname}</dd>
          <dt>
            <span id="secondSurname">
              <Translate contentKey="cfetimeApp.employee.secondSurname">Second Surname</Translate>
            </span>
            <UncontrolledTooltip target="secondSurname">
              <Translate contentKey="cfetimeApp.employee.help.secondSurname" />
            </UncontrolledTooltip>
          </dt>
          <dd>{employeeEntity.secondSurname}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="cfetimeApp.employee.createdDate">Created Date</Translate>
            </span>
            <UncontrolledTooltip target="createdDate">
              <Translate contentKey="cfetimeApp.employee.help.createdDate" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {employeeEntity.createdDate ? <TextFormat value={employeeEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="cfetimeApp.employee.lastModifiedDate">Last Modified Date</Translate>
            </span>
            <UncontrolledTooltip target="lastModifiedDate">
              <Translate contentKey="cfetimeApp.employee.help.lastModifiedDate" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {employeeEntity.lastModifiedDate ? (
              <TextFormat value={employeeEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="cfetimeApp.employee.user">User</Translate>
          </dt>
          <dd>{employeeEntity.user ? employeeEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="cfetimeApp.employee.status">Status</Translate>
          </dt>
          <dd>{employeeEntity.status ? employeeEntity.status.name : ''}</dd>
          <dt>
            <Translate contentKey="cfetimeApp.employee.created">Created</Translate>
          </dt>
          <dd>{employeeEntity.created ? employeeEntity.created.login : ''}</dd>
          <dt>
            <Translate contentKey="cfetimeApp.employee.lastModified">Last Modified</Translate>
          </dt>
          <dd>{employeeEntity.lastModified ? employeeEntity.lastModified.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/employee" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/employee/${employeeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmployeeDetail;
