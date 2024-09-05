import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVehicle } from 'app/shared/model/vehicle.model';
import { getEntities as getVehicles } from 'app/entities/vehicle/vehicle.reducer';
import { IEmployee } from 'app/shared/model/employee.model';
import { getEntities as getEmployees } from 'app/entities/employee/employee.reducer';
import { IStatus } from 'app/shared/model/status.model';
import { getEntities as getStatuses } from 'app/entities/status/status.reducer';
import { IVehicleUsage } from 'app/shared/model/vehicle-usage.model';
import { getEntity, updateEntity, createEntity, reset } from './vehicle-usage.reducer';

export const VehicleUsageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vehicles = useAppSelector(state => state.vehicle.entities);
  const employees = useAppSelector(state => state.employee.entities);
  const statuses = useAppSelector(state => state.status.entities);
  const vehicleUsageEntity = useAppSelector(state => state.vehicleUsage.entity);
  const loading = useAppSelector(state => state.vehicleUsage.loading);
  const updating = useAppSelector(state => state.vehicleUsage.updating);
  const updateSuccess = useAppSelector(state => state.vehicleUsage.updateSuccess);

  const handleClose = () => {
    navigate('/vehicle-usage');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getVehicles({}));
    dispatch(getEmployees({}));
    dispatch(getStatuses({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);

    const entity = {
      ...vehicleUsageEntity,
      ...values,
      vehicle: vehicles.find(it => it.id.toString() === values.vehicle?.toString()),
      employee: employees.find(it => it.id.toString() === values.employee?.toString()),
      status: statuses.find(it => it.id.toString() === values.status?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          startDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
        }
      : {
          ...vehicleUsageEntity,
          startDate: convertDateTimeFromServer(vehicleUsageEntity.startDate),
          endDate: convertDateTimeFromServer(vehicleUsageEntity.endDate),
          vehicle: vehicleUsageEntity?.vehicle?.id,
          employee: vehicleUsageEntity?.employee?.id,
          status: vehicleUsageEntity?.status?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cfetimeApp.vehicleUsage.home.createOrEditLabel" data-cy="VehicleUsageCreateUpdateHeading">
            <Translate contentKey="cfetimeApp.vehicleUsage.home.createOrEditLabel">Create or edit a VehicleUsage</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="vehicle-usage-id"
                  label={translate('cfetimeApp.vehicleUsage.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cfetimeApp.vehicleUsage.startDate')}
                id="vehicle-usage-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="startDateLabel">
                <Translate contentKey="cfetimeApp.vehicleUsage.help.startDate" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cfetimeApp.vehicleUsage.endDate')}
                id="vehicle-usage-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <UncontrolledTooltip target="endDateLabel">
                <Translate contentKey="cfetimeApp.vehicleUsage.help.endDate" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cfetimeApp.vehicleUsage.comments')}
                id="vehicle-usage-comments"
                name="comments"
                data-cy="comments"
                type="text"
              />
              <UncontrolledTooltip target="commentsLabel">
                <Translate contentKey="cfetimeApp.vehicleUsage.help.comments" />
              </UncontrolledTooltip>
              <ValidatedField
                id="vehicle-usage-vehicle"
                name="vehicle"
                data-cy="vehicle"
                label={translate('cfetimeApp.vehicleUsage.vehicle')}
                type="select"
                required
              >
                <option value="" key="0" />
                {vehicles
                  ? vehicles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.plates}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="vehicle-usage-employee"
                name="employee"
                data-cy="employee"
                label={translate('cfetimeApp.vehicleUsage.employee')}
                type="select"
                required
              >
                <option value="" key="0" />
                {employees
                  ? employees.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="vehicle-usage-status"
                name="status"
                data-cy="status"
                label={translate('cfetimeApp.vehicleUsage.status')}
                type="select"
                required
              >
                <option value="" key="0" />
                {statuses
                  ? statuses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vehicle-usage" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default VehicleUsageUpdate;
