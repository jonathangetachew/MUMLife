import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CheckoutRecord from './checkout-record';
import CheckoutRecordDetail from './checkout-record-detail';
import CheckoutRecordUpdate from './checkout-record-update';
import CheckoutRecordDeleteDialog from './checkout-record-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CheckoutRecordUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CheckoutRecordUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CheckoutRecordDetail} />
      <ErrorBoundaryRoute path={match.url} component={CheckoutRecord} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CheckoutRecordDeleteDialog} />
  </>
);

export default Routes;
