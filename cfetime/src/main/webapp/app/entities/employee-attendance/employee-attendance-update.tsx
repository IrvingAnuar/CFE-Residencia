import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEmployee } from 'app/shared/model/employee.model';
import { getEntities as getEmployees } from 'app/entities/employee/employee.reducer';
import { IStatus } from 'app/shared/model/status.model';
import { getEntities as getStatuses } from 'app/entities/status/status.reducer';
import { IEmployeeAttendance } from 'app/shared/model/employee-attendance.model';
import { getEntity, updateEntity, createEntity, reset } from './employee-attendance.reducer';

export const EmployeeAttendanceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const employees = useAppSelector(state => state.employee.entities);
  const statuses = useAppSelector(state => state.status.entities);
  const employeeAttendanceEntity = useAppSelector(state => state.employeeAttendance.entity);
  const loading = useAppSelector(state => state.employeeAttendance.loading);
  const updating = useAppSelector(state => state.employeeAttendance.updating);
  const updateSuccess = useAppSelector(state => state.employeeAttendance.updateSuccess);

  const handleClose = () => {
    navigate('/employee-attendance');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    values.checkInTime = convertDateTimeToServer(values.checkInTime);
    values.checkOutTime = convertDateTimeToServer(values.checkOutTime);

    const entity = {
      ...employeeAttendanceEntity,
      ...values,
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
          attendanceDate: new Date().toISOString().split('T')[0], // Fecha de hoy
          checkInTime: displayDefaultDateTime(),
          checkOutTime: displayDefaultDateTime(),
        }
      : {
          ...employeeAttendanceEntity,
          checkInTime: convertDateTimeFromServer(employeeAttendanceEntity.checkInTime),
          checkOutTime: convertDateTimeFromServer(employeeAttendanceEntity.checkOutTime),
          employee: employeeAttendanceEntity?.employee?.id,
          status: employeeAttendanceEntity?.status?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cfetimeApp.employeeAttendance.home.createOrEditLabel" data-cy="EmployeeAttendanceCreateUpdateHeading">
            <Translate contentKey="cfetimeApp.employeeAttendance.home.createOrEditLabel">Create or edit a EmployeeAttendance</Translate>
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
                  id="employee-attendance-id"
                  label={translate('cfetimeApp.employeeAttendance.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cfetimeApp.employeeAttendance.attendanceDate')}
                id="employee-attendance-attendanceDate"
                name="attendanceDate"
                data-cy="attendanceDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="attendanceDateLabel">
                <Translate contentKey="cfetimeApp.employeeAttendance.help.attendanceDate" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cfetimeApp.employeeAttendance.checkInTime')}
                id="employee-attendance-checkInTime"
                name="checkInTime"
                data-cy="checkInTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="checkInTimeLabel">
                <Translate contentKey="cfetimeApp.employeeAttendance.help.checkInTime" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cfetimeApp.employeeAttendance.checkOutTime')}
                id="employee-attendance-checkOutTime"
                name="checkOutTime"
                data-cy="checkOutTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <UncontrolledTooltip target="checkOutTimeLabel">
                <Translate contentKey="cfetimeApp.employeeAttendance.help.checkOutTime" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cfetimeApp.employeeAttendance.comments')}
                id="employee-attendance-comments"
                name="comments"
                data-cy="comments"
                type="text"
              />
              <UncontrolledTooltip target="commentsLabel">
                <Translate contentKey="cfetimeApp.employeeAttendance.help.comments" />
              </UncontrolledTooltip>
              <ValidatedField
                id="employee-attendance-employee"
                name="employee"
                data-cy="employee"
                label={translate('cfetimeApp.employeeAttendance.employee')}
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
                id="employee-attendance-status"
                name="status"
                data-cy="status"
                label={translate('cfetimeApp.employeeAttendance.status')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/employee-attendance" replace color="info">
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

export default EmployeeAttendanceUpdate;
