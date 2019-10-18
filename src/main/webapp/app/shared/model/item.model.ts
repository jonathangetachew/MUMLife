import { Moment } from 'moment';
import { IItemType } from 'app/shared/model/item-type.model';
import { IReservationRecord } from 'app/shared/model/reservation-record.model';
import { ICheckoutRecord } from 'app/shared/model/checkout-record.model';
import { IRatingRecord } from 'app/shared/model/rating-record.model';
import { ItemStatus } from 'app/shared/model/enumerations/item-status.model';

export interface IItem {
  id?: number;
  name?: string;
  imageUrl?: string;
  status?: ItemStatus;
  createdAt?: Moment;
  type?: IItemType;
  reservationRecords?: IReservationRecord[];
  checkoutRecords?: ICheckoutRecord[];
  ratingRecords?: IRatingRecord[];
}

export const defaultValue: Readonly<IItem> = {};
