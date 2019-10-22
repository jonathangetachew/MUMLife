import { Moment } from 'moment';
import { IUser } from 'app/shared/model/user.model';
import { IEvent } from 'app/shared/model/event.model';
import { RSVPType } from 'app/shared/model/enumerations/rsvp-type.model';

export interface IRSVPRecord {
  id?: number;
  createdAt?: Moment;
  status?: RSVPType;
  user?: IUser;
  event?: IEvent;
}

export const defaultValue: Readonly<IRSVPRecord> = {};
