import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EmployeeType from './employee-type';
import EmployeeTypeDetail from './employee-type-detail';
import EmployeeTypeUpdate from './employee-type-update';
import EmployeeTypeDeleteDialog from './employee-type-delete-dialog';

const EmployeeTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EmployeeType />} />
    <Route path="new" element={<EmployeeTypeUpdate />} />
    <Route path=":id">
      <Route index element={<EmployeeTypeDetail />} />
      <Route path="edit" element={<EmployeeTypeUpdate />} />
      <Route path="delete" element={<EmployeeTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EmployeeTypeRoutes;
