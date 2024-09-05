import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IStatus } from 'app/shared/model/status.model';
import { getEntities as getStatuses } from 'app/entities/status/status.reducer';
import { IEmployee } from 'app/shared/model/employee.model';
import { getEntity, updateEntity, createEntity, reset } from './employee.reducer';

export const EmployeeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const statuses = useAppSelector(state => state.status.entities);
  const employeeEntity = useAppSelector(state => state.employee.entity);
  const loading = useAppSelector(state => state.employee.loading);
  const updating = useAppSelector(state => state.employee.updating);
  const updateSuccess = useAppSelector(state => state.employee.updateSuccess);

  const handleClose = () => {
    navigate('/employee');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
    if (values.clave !== undefined && typeof values.clave !== 'number') {
      values.clave = Number(values.clave);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...employeeEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      status: statuses.find(it => it.id.toString() === values.status?.toString()),
      created: users.find(it => it.id.toString() === values.created?.toString()),
      lastModified: users.find(it => it.id.toString() === values.lastModified?.toString()),
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
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...employeeEntity,
          createdDate: convertDateTimeFromServer(employeeEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(employeeEntity.lastModifiedDate),
          user: employeeEntity?.user?.id,
          status: employeeEntity?.status?.id,
          created: employeeEntity?.created?.id,
          lastModified: employeeEntity?.lastModified?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cfetimeApp.employee.home.createOrEditLabel" data-cy="EmployeeCreateUpdateHeading">
            <Translate contentKey="cfetimeApp.employee.home.createOrEditLabel">Create or edit a Employee</Translate>
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
                  id="employee-id"
                  label={translate('cfetimeApp.employee.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('cfetimeApp.employee.clave')} id="employee-clave" name="clave" data-cy="clave" type="text" />
              <UncontrolledTooltip target="claveLabel">
                <Translate contentKey="cfetimeApp.employee.help.clave" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cfetimeApp.employee.name')}
                id="employee-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <UncontrolledTooltip target="nameLabel">
                <Translate contentKey="cfetimeApp.employee.help.name" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cfetimeApp.employee.firstSurname')}
                id="employee-firstSurname"
                name="firstSurname"
                data-cy="firstSurname"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <UncontrolledTooltip target="firstSurnameLabel">
                <Translate contentKey="cfetimeApp.employee.help.firstSurname" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cfetimeApp.employee.secondSurname')}
                id="employee-secondSurname"
                name="secondSurname"
                data-cy="secondSurname"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <UncontrolledTooltip target="secondSurnameLabel">
                <Translate contentKey="cfetimeApp.employee.help.secondSurname" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cfetimeApp.employee.createdDate')}
                id="employee-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="createdDateLabel">
                <Translate contentKey="cfetimeApp.employee.help.createdDate" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cfetimeApp.employee.lastModifiedDate')}
                id="employee-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <UncontrolledTooltip target="lastModifiedDateLabel">
                <Translate contentKey="cfetimeApp.employee.help.lastModifiedDate" />
              </UncontrolledTooltip>
              <ValidatedField id="employee-user" name="user" data-cy="user" label={translate('cfetimeApp.employee.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="employee-status"
                name="status"
                data-cy="status"
                label={translate('cfetimeApp.employee.status')}
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
              <ValidatedField
                id="employee-created"
                name="created"
                data-cy="created"
                label={translate('cfetimeApp.employee.created')}
                type="select"
                required
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="employee-lastModified"
                name="lastModified"
                data-cy="lastModified"
                label={translate('cfetimeApp.employee.lastModified')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/employee" replace color="info">
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

export default EmployeeUpdate;
