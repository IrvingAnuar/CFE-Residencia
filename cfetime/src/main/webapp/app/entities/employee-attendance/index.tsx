import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EmployeeAttendance from './employee-attendance';
import EmployeeAttendanceDetail from './employee-attendance-detail';
import EmployeeAttendanceUpdate from './employee-attendance-update';
import EmployeeAttendanceDeleteDialog from './employee-attendance-delete-dialog';

const EmployeeAttendanceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EmployeeAttendance />} />
    <Route path="new" element={<EmployeeAttendanceUpdate />} />
    <Route path=":id">
      <Route index element={<EmployeeAttendanceDetail />} />
      <Route path="edit" element={<EmployeeAttendanceUpdate />} />
      <Route path="delete" element={<EmployeeAttendanceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EmployeeAttendanceRoutes;
