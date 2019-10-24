import { IEvent } from 'app/shared/model/event.model';

export interface IEventType {
  id?: number;
  name?: string;
  types?: IEvent;
}

export const defaultValue: Readonly<IEventType> = {};
