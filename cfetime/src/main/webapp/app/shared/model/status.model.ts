import { IStatusType } from 'app/shared/model/status-type.model';

export interface IStatus {
  id?: number;
  name?: string;
  statusType?: IStatusType;
}

export const defaultValue: Readonly<IStatus> = {};
