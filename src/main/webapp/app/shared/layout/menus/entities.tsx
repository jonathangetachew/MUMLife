import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <MenuItem icon="asterisk" to="/entity/item">
      Item
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/item-type">
      Item Type
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/checkout-record">
      Checkout Record
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/reservation-record">
      Reservation Record
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/rating-record">
      Rating Record
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/event">
      Event
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/event-type">
      Event Type
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/rsvp-record">
      Rsvp Record
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
