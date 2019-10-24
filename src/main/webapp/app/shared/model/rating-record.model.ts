import { IUser } from 'app/shared/model/user.model';
import { IItem } from 'app/shared/model/item.model';

export interface IRatingRecord {
  id?: number;
  rateValue?: number;
  commentContentType?: string;
  comment?: any;
  user?: IUser;
  item?: IItem;
}

export const defaultValue: Readonly<IRatingRecord> = {};
