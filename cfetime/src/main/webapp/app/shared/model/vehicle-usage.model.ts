import dayjs from 'dayjs';
import { IVehicle } from 'app/shared/model/vehicle.model';
import { IEmployee } from 'app/shared/model/employee.model';
import { IStatus } from 'app/shared/model/status.model';

export interface IVehicleUsage {
  id?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs | null;
  comments?: string | null;
  vehicle?: IVehicle;
  employee?: IEmployee;
  status?: IStatus;
}

export const defaultValue: Readonly<IVehicleUsage> = {};
