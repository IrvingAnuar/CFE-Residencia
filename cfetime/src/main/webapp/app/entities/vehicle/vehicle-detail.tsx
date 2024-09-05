import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle.reducer';

export const VehicleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleEntity = useAppSelector(state => state.vehicle.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleDetailsHeading">
          <Translate contentKey="cfetimeApp.vehicle.detail.title">Vehicle</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.id}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="cfetimeApp.vehicle.descripcion">Descripcion</Translate>
            </span>
            <UncontrolledTooltip target="descripcion">
              <Translate contentKey="cfetimeApp.vehicle.help.descripcion" />
            </UncontrolledTooltip>
          </dt>
          <dd>{vehicleEntity.descripcion}</dd>
          <dt>
            <span id="model">
              <Translate contentKey="cfetimeApp.vehicle.model">Model</Translate>
            </span>
            <UncontrolledTooltip target="model">
              <Translate contentKey="cfetimeApp.vehicle.help.model" />
            </UncontrolledTooltip>
          </dt>
          <dd>{vehicleEntity.model}</dd>
          <dt>
            <span id="plates">
              <Translate contentKey="cfetimeApp.vehicle.plates">Plates</Translate>
            </span>
            <UncontrolledTooltip target="plates">
              <Translate contentKey="cfetimeApp.vehicle.help.plates" />
            </UncontrolledTooltip>
          </dt>
          <dd>{vehicleEntity.plates}</dd>
          <dt>
            <Translate contentKey="cfetimeApp.vehicle.vehicleType">Vehicle Type</Translate>
          </dt>
          <dd>{vehicleEntity.vehicleType ? vehicleEntity.vehicleType.descripcion : ''}</dd>
        </dl>
        <Button tag={Link} to="/vehicle" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle/${vehicleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleDetail;
