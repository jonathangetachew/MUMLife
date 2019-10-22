import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import item, {
  ItemState
} from 'app/entities/item/item.reducer';
// prettier-ignore
import itemType, {
  ItemTypeState
} from 'app/entities/item-type/item-type.reducer';
// prettier-ignore
import checkoutRecord, {
  CheckoutRecordState
} from 'app/entities/checkout-record/checkout-record.reducer';
// prettier-ignore
import reservationRecord, {
  ReservationRecordState
} from 'app/entities/reservation-record/reservation-record.reducer';
// prettier-ignore
import ratingRecord, {
  RatingRecordState
} from 'app/entities/rating-record/rating-record.reducer';
// prettier-ignore
import event, {
  EventState
} from 'app/entities/event/event.reducer';
// prettier-ignore
import eventType, {
  EventTypeState
} from 'app/entities/event-type/event-type.reducer';
// prettier-ignore
import rSVPRecord, {
  RSVPRecordState
} from 'app/entities/rsvp-record/rsvp-record.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly item: ItemState;
  readonly itemType: ItemTypeState;
  readonly checkoutRecord: CheckoutRecordState;
  readonly reservationRecord: ReservationRecordState;
  readonly ratingRecord: RatingRecordState;
  readonly event: EventState;
  readonly eventType: EventTypeState;
  readonly rSVPRecord: RSVPRecordState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  item,
  itemType,
  checkoutRecord,
  reservationRecord,
  ratingRecord,
  event,
  eventType,
  rSVPRecord,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
