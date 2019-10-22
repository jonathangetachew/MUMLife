import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IReservationRecord, defaultValue } from 'app/shared/model/reservation-record.model';

export const ACTION_TYPES = {
  FETCH_RESERVATIONRECORD_LIST: 'reservationRecord/FETCH_RESERVATIONRECORD_LIST',
  FETCH_RESERVATIONRECORD: 'reservationRecord/FETCH_RESERVATIONRECORD',
  CREATE_RESERVATIONRECORD: 'reservationRecord/CREATE_RESERVATIONRECORD',
  UPDATE_RESERVATIONRECORD: 'reservationRecord/UPDATE_RESERVATIONRECORD',
  DELETE_RESERVATIONRECORD: 'reservationRecord/DELETE_RESERVATIONRECORD',
  RESET: 'reservationRecord/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IReservationRecord>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ReservationRecordState = Readonly<typeof initialState>;

// Reducer

export default (state: ReservationRecordState = initialState, action): ReservationRecordState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RESERVATIONRECORD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RESERVATIONRECORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RESERVATIONRECORD):
    case REQUEST(ACTION_TYPES.UPDATE_RESERVATIONRECORD):
    case REQUEST(ACTION_TYPES.DELETE_RESERVATIONRECORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_RESERVATIONRECORD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RESERVATIONRECORD):
    case FAILURE(ACTION_TYPES.CREATE_RESERVATIONRECORD):
    case FAILURE(ACTION_TYPES.UPDATE_RESERVATIONRECORD):
    case FAILURE(ACTION_TYPES.DELETE_RESERVATIONRECORD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_RESERVATIONRECORD_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_RESERVATIONRECORD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RESERVATIONRECORD):
    case SUCCESS(ACTION_TYPES.UPDATE_RESERVATIONRECORD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RESERVATIONRECORD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/reservation-records';

// Actions

export const getEntities: ICrudGetAllAction<IReservationRecord> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_RESERVATIONRECORD_LIST,
    payload: axios.get<IReservationRecord>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IReservationRecord> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RESERVATIONRECORD,
    payload: axios.get<IReservationRecord>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IReservationRecord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RESERVATIONRECORD,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IReservationRecord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RESERVATIONRECORD,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IReservationRecord> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RESERVATIONRECORD,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
