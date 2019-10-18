import { Moment } from 'moment';
import { IUser } from 'app/shared/model/user.model';
import { IItem } from 'app/shared/model/item.model';

export interface ICheckoutRecord {
  id?: number;
  dueDate?: Moment;
  createdAt?: Moment;
  user?: IUser;
  item?: IItem;
}

export const defaultValue: Readonly<ICheckoutRecord> = {};
