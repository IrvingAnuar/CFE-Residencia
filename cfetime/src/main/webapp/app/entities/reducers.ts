import status from 'app/entities/status/status.reducer';
import statusType from 'app/entities/status-type/status-type.reducer';
import vehicleType from 'app/entities/vehicle-type/vehicle-type.reducer';
import vehicle from 'app/entities/vehicle/vehicle.reducer';
import employeeType from 'app/entities/employee-type/employee-type.reducer';
import employee from 'app/entities/employee/employee.reducer';
import vehicleUsage from 'app/entities/vehicle-usage/vehicle-usage.reducer';
import employeeAttendance from 'app/entities/employee-attendance/employee-attendance.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  status,
  statusType,
  vehicleType,
  vehicle,
  employeeType,
  employee,
  vehicleUsage,
  employeeAttendance,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
