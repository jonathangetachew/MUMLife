import React from 'react';
import { Switch } from 'react-router-dom';

import { AUTHORITIES } from 'app/config/constants';

import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ReservationRecord from './reservation-record';
import ReservationRecordDetail from './reservation-record-detail';
import ReservationRecordUpdate from './reservation-record-update';
import ReservationRecordDeleteDialog from './reservation-record-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={ReservationRecordUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.LENDER]} />
      <PrivateRoute exact path={`${match.url}/:id/edit`} component={ReservationRecordUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.LENDER]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ReservationRecordDetail} />
      <ErrorBoundaryRoute path={match.url} component={ReservationRecord} />
    </Switch>
    <PrivateRoute path={`${match.url}/:id/delete`} component={ReservationRecordDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.LENDER]}/>
  </>
);

export default Routes;
