import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { NavDropdown } from './menu-components';

export const ReservationsMenu = props => (
  <NavDropdown icon="trash" name="Reservations" id="reservation-menu">
    <MenuItem icon="asterisk" to="/entity/item">
      Reserve item
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/reservation-record">
      View reservervations
    </MenuItem>
  </NavDropdown>
);
