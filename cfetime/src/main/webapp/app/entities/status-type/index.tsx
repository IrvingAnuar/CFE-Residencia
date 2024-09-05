import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StatusType from './status-type';
import StatusTypeDetail from './status-type-detail';

const StatusTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StatusType />} />
    <Route path=":id">
      <Route index element={<StatusTypeDetail />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StatusTypeRoutes;
