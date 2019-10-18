import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RSVPRecord from './rsvp-record';
import RSVPRecordDetail from './rsvp-record-detail';
import RSVPRecordUpdate from './rsvp-record-update';
import RSVPRecordDeleteDialog from './rsvp-record-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RSVPRecordUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RSVPRecordUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RSVPRecordDetail} />
      <ErrorBoundaryRoute path={match.url} component={RSVPRecord} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RSVPRecordDeleteDialog} />
  </>
);

export default Routes;
