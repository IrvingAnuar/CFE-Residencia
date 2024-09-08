import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './employee-attendance.reducer';

export const EmployeeAttendanceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const employeeAttendanceEntity = useAppSelector(state => state.employeeAttendance.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="employeeAttendanceDetailsHeading">
          <Translate contentKey="cfetimeApp.employeeAttendance.detail.title">EmployeeAttendance</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="cfetimeApp.employeeAttendance.id">Id</Translate>
            </span>
          </dt>
          <dd>{employeeAttendanceEntity.id}</dd>
          <dt>
            <span id="attendanceDate">
              <Translate contentKey="cfetimeApp.employeeAttendance.attendanceDate">Attendance Date</Translate>
            </span>
            <UncontrolledTooltip target="attendanceDate">
              <Translate contentKey="cfetimeApp.employeeAttendance.help.attendanceDate" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {employeeAttendanceEntity.attendanceDate ? (
              <TextFormat value={employeeAttendanceEntity.attendanceDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="checkInTime">
              <Translate contentKey="cfetimeApp.employeeAttendance.checkInTime">Check In Time</Translate>
            </span>
            <UncontrolledTooltip target="checkInTime">
              <Translate contentKey="cfetimeApp.employeeAttendance.help.checkInTime" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {employeeAttendanceEntity.checkInTime ? (
              <TextFormat value={employeeAttendanceEntity.checkInTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="checkOutTime">
              <Translate contentKey="cfetimeApp.employeeAttendance.checkOutTime">Check Out Time</Translate>
            </span>
            <UncontrolledTooltip target="checkOutTime">
              <Translate contentKey="cfetimeApp.employeeAttendance.help.checkOutTime" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {employeeAttendanceEntity.checkOutTime ? (
              <TextFormat value={employeeAttendanceEntity.checkOutTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="comments">
              <Translate contentKey="cfetimeApp.employeeAttendance.comments">Comments</Translate>
            </span>
            <UncontrolledTooltip target="comments">
              <Translate contentKey="cfetimeApp.employeeAttendance.help.comments" />
            </UncontrolledTooltip>
          </dt>
          <dd>{employeeAttendanceEntity.comments}</dd>
          <dt>
            <Translate contentKey="cfetimeApp.employeeAttendance.employee">Employee</Translate>
          </dt>
          <dd>{employeeAttendanceEntity.employee ? employeeAttendanceEntity.employee.name : ''}</dd>
          <dt>
            <Translate contentKey="cfetimeApp.employeeAttendance.status">Status</Translate>
          </dt>
          <dd>{employeeAttendanceEntity.status ? employeeAttendanceEntity.status.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/employee-attendance" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/employee-attendance/${employeeAttendanceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmployeeAttendanceDetail;
