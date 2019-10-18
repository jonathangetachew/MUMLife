import { Moment } from 'moment';
import { IUser } from 'app/shared/model/user.model';
import { IEvent } from 'app/shared/model/event.model';
import { AttandanceStatus } from 'app/shared/model/enumerations/attandance-status.model';

export interface IRSVPRecord {
  id?: number;
  createdAt?: Moment;
  status?: AttandanceStatus;
  user?: IUser;
  event?: IEvent;
}

export const defaultValue: Readonly<IRSVPRecord> = {};
