import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle-usage.reducer';

export const VehicleUsageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleUsageEntity = useAppSelector(state => state.vehicleUsage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleUsageDetailsHeading">
          <Translate contentKey="cfetimeApp.vehicleUsage.detail.title">VehicleUsage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="cfetimeApp.vehicleUsage.id">Id</Translate>
            </span>
          </dt>
          <dd>{vehicleUsageEntity.id}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="cfetimeApp.vehicleUsage.startDate">Start Date</Translate>
            </span>
            <UncontrolledTooltip target="startDate">
              <Translate contentKey="cfetimeApp.vehicleUsage.help.startDate" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {vehicleUsageEntity.startDate ? <TextFormat value={vehicleUsageEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="cfetimeApp.vehicleUsage.endDate">End Date</Translate>
            </span>
            <UncontrolledTooltip target="endDate">
              <Translate contentKey="cfetimeApp.vehicleUsage.help.endDate" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {vehicleUsageEntity.endDate ? <TextFormat value={vehicleUsageEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="comments">
              <Translate contentKey="cfetimeApp.vehicleUsage.comments">Comments</Translate>
            </span>
            <UncontrolledTooltip target="comments">
              <Translate contentKey="cfetimeApp.vehicleUsage.help.comments" />
            </UncontrolledTooltip>
          </dt>
          <dd>{vehicleUsageEntity.comments}</dd>
          <dt>
            <Translate contentKey="cfetimeApp.vehicleUsage.vehicle">Vehicle</Translate>
          </dt>
          <dd>{vehicleUsageEntity.vehicle ? vehicleUsageEntity.vehicle.plates : ''}</dd>
          <dt>
            <Translate contentKey="cfetimeApp.vehicleUsage.employee">Employee</Translate>
          </dt>
          <dd>{vehicleUsageEntity.employee ? vehicleUsageEntity.employee.name : ''}</dd>
          <dt>
            <Translate contentKey="cfetimeApp.vehicleUsage.status">Status</Translate>
          </dt>
          <dd>{vehicleUsageEntity.status ? vehicleUsageEntity.status.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/vehicle-usage" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle-usage/${vehicleUsageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleUsageDetail;
