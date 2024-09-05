import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle-type.reducer';

export const VehicleTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleTypeEntity = useAppSelector(state => state.vehicleType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleTypeDetailsHeading">
          <Translate contentKey="cfetimeApp.vehicleType.detail.title">VehicleType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vehicleTypeEntity.id}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="cfetimeApp.vehicleType.descripcion">Descripcion</Translate>
            </span>
            <UncontrolledTooltip target="descripcion">
              <Translate contentKey="cfetimeApp.vehicleType.help.descripcion" />
            </UncontrolledTooltip>
          </dt>
          <dd>{vehicleTypeEntity.descripcion}</dd>
        </dl>
        <Button tag={Link} to="/vehicle-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle-type/${vehicleTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleTypeDetail;
