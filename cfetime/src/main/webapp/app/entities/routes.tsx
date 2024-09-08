import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Status from './status';
import StatusType from './status-type';
import VehicleType from './vehicle-type';
import Vehicle from './vehicle';
import EmployeeType from './employee-type';
import Employee from './employee';
import VehicleUsage from './vehicle-usage';
import EmployeeAttendance from './employee-attendance';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="status/*" element={<Status />} />
        <Route path="status-type/*" element={<StatusType />} />
        <Route path="vehicle-type/*" element={<VehicleType />} />
        <Route path="vehicle/*" element={<Vehicle />} />
        <Route path="employee-type/*" element={<EmployeeType />} />
        <Route path="employee/*" element={<Employee />} />
        <Route path="vehicle-usage/*" element={<VehicleUsage />} />
        <Route path="employee-attendance/*" element={<EmployeeAttendance />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
