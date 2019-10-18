import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RatingRecord from './rating-record';
import RatingRecordDetail from './rating-record-detail';
import RatingRecordUpdate from './rating-record-update';
import RatingRecordDeleteDialog from './rating-record-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RatingRecordUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RatingRecordUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RatingRecordDetail} />
      <ErrorBoundaryRoute path={match.url} component={RatingRecord} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RatingRecordDeleteDialog} />
  </>
);

export default Routes;
