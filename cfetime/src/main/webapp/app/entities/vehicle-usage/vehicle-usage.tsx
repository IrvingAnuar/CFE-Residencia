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

import { getEntities } from './vehicle-usage.reducer';

export const VehicleUsage = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const vehicleUsageList = useAppSelector(state => state.vehicleUsage.entities);
  const loading = useAppSelector(state => state.vehicleUsage.loading);

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
      <h2 id="vehicle-usage-heading" data-cy="VehicleUsageHeading">
        <Translate contentKey="cfetimeApp.vehicleUsage.home.title">Vehicle Usages</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="cfetimeApp.vehicleUsage.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/vehicle-usage/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="cfetimeApp.vehicleUsage.home.createLabel">Create new Vehicle Usage</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {vehicleUsageList && vehicleUsageList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="cfetimeApp.vehicleUsage.id">Id</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="cfetimeApp.vehicleUsage.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  <Translate contentKey="cfetimeApp.vehicleUsage.endDate">End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endDate')} />
                </th>
                <th className="hand" onClick={sort('comments')}>
                  <Translate contentKey="cfetimeApp.vehicleUsage.comments">Comments</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('comments')} />
                </th>
                <th>
                  <Translate contentKey="cfetimeApp.vehicleUsage.vehicle">Vehicle</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="cfetimeApp.vehicleUsage.employee">Employee</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="cfetimeApp.vehicleUsage.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {vehicleUsageList.map((vehicleUsage, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/vehicle-usage/${vehicleUsage.id}`} color="link" size="sm">
                      {vehicleUsage.id}
                    </Button>
                  </td>
                  <td>
                    {vehicleUsage.startDate ? <TextFormat type="date" value={vehicleUsage.startDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{vehicleUsage.endDate ? <TextFormat type="date" value={vehicleUsage.endDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{vehicleUsage.comments}</td>
                  <td>
                    {vehicleUsage.vehicle ? <Link to={`/vehicle/${vehicleUsage.vehicle.id}`}>{vehicleUsage.vehicle.plates}</Link> : ''}
                  </td>
                  <td>
                    {vehicleUsage.employee ? <Link to={`/employee/${vehicleUsage.employee.id}`}>{vehicleUsage.employee.name}</Link> : ''}
                  </td>
                  <td>{vehicleUsage.status ? <Link to={`/status/${vehicleUsage.status.id}`}>{vehicleUsage.status.name}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/vehicle-usage/${vehicleUsage.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/vehicle-usage/${vehicleUsage.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/vehicle-usage/${vehicleUsage.id}/delete`)}
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
              <Translate contentKey="cfetimeApp.vehicleUsage.home.notFound">No Vehicle Usages found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default VehicleUsage;
