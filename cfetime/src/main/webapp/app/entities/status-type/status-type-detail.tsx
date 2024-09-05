import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './status-type.reducer';

export const StatusTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const statusTypeEntity = useAppSelector(state => state.statusType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="statusTypeDetailsHeading">
          <Translate contentKey="cfetimeApp.statusType.detail.title">StatusType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{statusTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cfetimeApp.statusType.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="cfetimeApp.statusType.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{statusTypeEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/status-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/status-type/${statusTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StatusTypeDetail;
