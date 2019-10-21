import { Moment } from 'moment';
import { IRSVPRecord } from 'app/shared/model/rsvp-record.model';
import { IEventType } from 'app/shared/model/event-type.model';

export interface IEvent {
  id?: number;
  name?: string;
  description?: string;
  posterUrlImage?: string;
  createdAt?: Moment;
  start?: Moment;
  end?: Moment;
  rSVPRecords?: IRSVPRecord[];
  eventTypes?: IEventType[];
}

export const defaultValue: Readonly<IEvent> = {};
