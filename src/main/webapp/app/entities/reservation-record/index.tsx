import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ReservationRecord from './reservation-record';
import ReservationRecordDetail from './reservation-record-detail';
import ReservationRecordUpdate from './reservation-record-update';
import ReservationRecordDeleteDialog from './reservation-record-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ReservationRecordUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ReservationRecordUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ReservationRecordDetail} />
      <ErrorBoundaryRoute path={match.url} component={ReservationRecord} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ReservationRecordDeleteDialog} />
  </>
);

export default Routes;
