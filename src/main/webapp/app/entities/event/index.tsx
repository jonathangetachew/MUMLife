import React from 'react';
import { Switch } from 'react-router-dom';

import { AUTHORITIES } from 'app/config/constants';

import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Event from './event';
import EventDetail from './event-detail';
import EventUpdate from './event-update';
import EventDeleteDialog from './event-delete-dialog';

const Routes = ({ match }) => (
 <>
   <Switch>
     <PrivateRoute exact path={`${match.url}/new`} component={EventUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.ORGANIZER]}/>
     <PrivateRoute exact path={`${match.url}/:id/edit`} component={EventUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.ORGANIZER]}/>
     <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EventDetail} />
     <ErrorBoundaryRoute path={match.url} component={Event} />
   </Switch>
   <PrivateRoute path={`${match.url}/:id/delete`} component={EventDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.ORGANIZER]} />
 </>
);

export default Routes;
