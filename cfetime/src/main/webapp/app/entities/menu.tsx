import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/status">
        <Translate contentKey="global.menu.entities.status" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/status-type">
        <Translate contentKey="global.menu.entities.statusType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle-type">
        <Translate contentKey="global.menu.entities.vehicleType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle">
        <Translate contentKey="global.menu.entities.vehicle" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/employee-type">
        <Translate contentKey="global.menu.entities.employeeType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/employee">
        <Translate contentKey="global.menu.entities.employee" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle-usage">
        <Translate contentKey="global.menu.entities.vehicleUsage" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/employee-attendance">
        <Translate contentKey="global.menu.entities.employeeAttendance" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
