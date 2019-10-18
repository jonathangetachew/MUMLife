import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ItemType from './item-type';
import ItemTypeDetail from './item-type-detail';
import ItemTypeUpdate from './item-type-update';
import ItemTypeDeleteDialog from './item-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ItemTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ItemTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ItemTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={ItemType} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ItemTypeDeleteDialog} />
  </>
);

export default Routes;
