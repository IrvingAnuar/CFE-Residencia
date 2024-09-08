import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Status from './status';
import StatusDetail from './status-detail';

const StatusRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Status />} />
    <Route path=":id">
      <Route index element={<StatusDetail />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StatusRoutes;
