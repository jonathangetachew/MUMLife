import React from 'react';
import { Switch } from 'react-router-dom';
import { connect } from 'react-redux';

import { AUTHORITIES } from 'app/config/constants';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Event from './event';
import EventDetail from './event-detail';
import EventUpdate from './event-update';
import EventDeleteDialog from './event-delete-dialog';

const Routes = ({ match, isEditable }) => (
 <>
   <Switch>
     { isEditable && <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EventUpdate} />}
     { isEditable && <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EventUpdate} />}
     <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EventDetail} />
     <ErrorBoundaryRoute path={match.url} component={Event} />
   </Switch>
   { isEditable && <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={EventDeleteDialog} />}
 </>
);

const mapStateToProps = ({authentication}) => ({
  account: authentication.account,
  isEditable: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.ORGANIZER]),
  isAuthenticated: authentication.isAuthenticated
});

export default connect(mapStateToProps)(Routes);
