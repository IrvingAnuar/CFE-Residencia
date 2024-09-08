import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import VehicleUsage from './vehicle-usage';
import VehicleUsageDetail from './vehicle-usage-detail';
import VehicleUsageUpdate from './vehicle-usage-update';
import VehicleUsageDeleteDialog from './vehicle-usage-delete-dialog';

const VehicleUsageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<VehicleUsage />} />
    <Route path="new" element={<VehicleUsageUpdate />} />
    <Route path=":id">
      <Route index element={<VehicleUsageDetail />} />
      <Route path="edit" element={<VehicleUsageUpdate />} />
      <Route path="delete" element={<VehicleUsageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VehicleUsageRoutes;
