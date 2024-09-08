import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './employee-type.reducer';

export const EmployeeTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const employeeTypeEntity = useAppSelector(state => state.employeeType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="employeeTypeDetailsHeading">
          <Translate contentKey="cfetimeApp.employeeType.detail.title">EmployeeType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="cfetimeApp.employeeType.id">Id</Translate>
            </span>
          </dt>
          <dd>{employeeTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cfetimeApp.employeeType.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="cfetimeApp.employeeType.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{employeeTypeEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/employee-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/employee-type/${employeeTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmployeeTypeDetail;
