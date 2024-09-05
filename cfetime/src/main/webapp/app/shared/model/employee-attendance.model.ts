import dayjs from 'dayjs';
import { IEmployee } from 'app/shared/model/employee.model';
import { IStatus } from 'app/shared/model/status.model';

export interface IEmployeeAttendance {
  id?: number;
  attendanceDate?: dayjs.Dayjs;
  checkInTime?: dayjs.Dayjs;
  checkOutTime?: dayjs.Dayjs | null;
  comments?: string | null;
  employee?: IEmployee;
  status?: IStatus;
}

export const defaultValue: Readonly<IEmployeeAttendance> = {};
