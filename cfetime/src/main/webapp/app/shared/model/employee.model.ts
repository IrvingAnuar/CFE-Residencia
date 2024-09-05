import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IStatus } from 'app/shared/model/status.model';

export interface IEmployee {
  id?: number;
  clave?: number | null;
  name?: string | null;
  firstSurname?: string | null;
  secondSurname?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  user?: IUser | null;
  status?: IStatus;
  created?: IUser;
  lastModified?: IUser | null;
}

export const defaultValue: Readonly<IEmployee> = {};
