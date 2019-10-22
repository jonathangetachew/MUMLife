import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Item from './item';
import ItemType from './item-type';
import CheckoutRecord from './checkout-record';
import ReservationRecord from './reservation-record';
import RatingRecord from './rating-record';
import Event from './event';
import EventType from './event-type';
import RSVPRecord from './rsvp-record';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/item`} component={Item} />
      <ErrorBoundaryRoute path={`${match.url}/item-type`} component={ItemType} />
      <ErrorBoundaryRoute path={`${match.url}/checkout-record`} component={CheckoutRecord} />
      <ErrorBoundaryRoute path={`${match.url}/reservation-record`} component={ReservationRecord} />
      <ErrorBoundaryRoute path={`${match.url}/rating-record`} component={RatingRecord} />
      <ErrorBoundaryRoute path={`${match.url}/event`} component={Event} />
      <ErrorBoundaryRoute path={`${match.url}/event-type`} component={EventType} />
      <ErrorBoundaryRoute path={`${match.url}/rsvp-record`} component={RSVPRecord} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
