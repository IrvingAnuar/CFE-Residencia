import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './employee-attendance.reducer';

export const EmployeeAttendance = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const employeeAttendanceList = useAppSelector(state => state.employeeAttendance.entities);
  const loading = useAppSelector(state => state.employeeAttendance.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="employee-attendance-heading" data-cy="EmployeeAttendanceHeading">
        <Translate contentKey="cfetimeApp.employeeAttendance.home.title">Employee Attendances</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="cfetimeApp.employeeAttendance.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/employee-attendance/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="cfetimeApp.employeeAttendance.home.createLabel">Create new Employee Attendance</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {employeeAttendanceList && employeeAttendanceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="cfetimeApp.employeeAttendance.id">Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('attendanceDate')}>
                  <Translate contentKey="cfetimeApp.employeeAttendance.attendanceDate">Attendance Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('attendanceDate')} />
                </th>
                <th className="hand" onClick={sort('checkInTime')}>
                  <Translate contentKey="cfetimeApp.employeeAttendance.checkInTime">Check In Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('checkInTime')} />
                </th>
                <th className="hand" onClick={sort('checkOutTime')}>
                  <Translate contentKey="cfetimeApp.employeeAttendance.checkOutTime">Check Out Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('checkOutTime')} />
                </th>
                <th className="hand" onClick={sort('comments')}>
                  <Translate contentKey="cfetimeApp.employeeAttendance.comments">Comments</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('comments')} />
                </th>
                <th>
                  <Translate contentKey="cfetimeApp.employeeAttendance.employee">Employee</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="cfetimeApp.employeeAttendance.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {employeeAttendanceList.map((employeeAttendance, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/employee-attendance/${employeeAttendance.id}`} color="link" size="sm">
                      {employeeAttendance.id}
                    </Button>
                  </td>
                  <td>
                    {employeeAttendance.attendanceDate ? (
                      <TextFormat type="date" value={employeeAttendance.attendanceDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {employeeAttendance.checkInTime ? (
                      <TextFormat type="date" value={employeeAttendance.checkInTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {employeeAttendance.checkOutTime ? (
                      <TextFormat type="date" value={employeeAttendance.checkOutTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{employeeAttendance.comments}</td>
                  <td>
                    {employeeAttendance.employee ? (
                      <Link to={`/employee/${employeeAttendance.employee.id}`}>{employeeAttendance.employee.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {employeeAttendance.status ? (
                      <Link to={`/status/${employeeAttendance.status.id}`}>{employeeAttendance.status.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/employee-attendance/${employeeAttendance.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/employee-attendance/${employeeAttendance.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/employee-attendance/${employeeAttendance.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="cfetimeApp.employeeAttendance.home.notFound">No Employee Attendances found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default EmployeeAttendance;
