import { IVehicleType } from 'app/shared/model/vehicle-type.model';

export interface IVehicle {
  id?: number;
  descripcion?: string;
  model?: string;
  plates?: string;
  vehicleType?: IVehicleType;
}

export const defaultValue: Readonly<IVehicle> = {};
