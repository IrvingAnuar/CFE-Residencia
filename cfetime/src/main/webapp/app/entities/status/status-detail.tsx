import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './status.reducer';

export const StatusDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const statusEntity = useAppSelector(state => state.status.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="statusDetailsHeading">
          <Translate contentKey="cfetimeApp.status.detail.title">Status</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{statusEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cfetimeApp.status.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="cfetimeApp.status.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{statusEntity.name}</dd>
          <dt>
            <Translate contentKey="cfetimeApp.status.statusType">Status Type</Translate>
          </dt>
          <dd>{statusEntity.statusType ? statusEntity.statusType.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/status" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/status/${statusEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StatusDetail;
