import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRSVPRecord, defaultValue } from 'app/shared/model/rsvp-record.model';

export const ACTION_TYPES = {
  FETCH_RSVPRECORD_LIST: 'rSVPRecord/FETCH_RSVPRECORD_LIST',
  FETCH_RSVPRECORD: 'rSVPRecord/FETCH_RSVPRECORD',
  CREATE_RSVPRECORD: 'rSVPRecord/CREATE_RSVPRECORD',
  UPDATE_RSVPRECORD: 'rSVPRecord/UPDATE_RSVPRECORD',
  DELETE_RSVPRECORD: 'rSVPRecord/DELETE_RSVPRECORD',
  RESET: 'rSVPRecord/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRSVPRecord>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type RSVPRecordState = Readonly<typeof initialState>;

// Reducer

export default (state: RSVPRecordState = initialState, action): RSVPRecordState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RSVPRECORD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RSVPRECORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RSVPRECORD):
    case REQUEST(ACTION_TYPES.UPDATE_RSVPRECORD):
    case REQUEST(ACTION_TYPES.DELETE_RSVPRECORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_RSVPRECORD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RSVPRECORD):
    case FAILURE(ACTION_TYPES.CREATE_RSVPRECORD):
    case FAILURE(ACTION_TYPES.UPDATE_RSVPRECORD):
    case FAILURE(ACTION_TYPES.DELETE_RSVPRECORD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_RSVPRECORD_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_RSVPRECORD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RSVPRECORD):
    case SUCCESS(ACTION_TYPES.UPDATE_RSVPRECORD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RSVPRECORD):
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

const apiUrl = 'api/rsvp-records';

// Actions

export const getEntities: ICrudGetAllAction<IRSVPRecord> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_RSVPRECORD_LIST,
    payload: axios.get<IRSVPRecord>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IRSVPRecord> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RSVPRECORD,
    payload: axios.get<IRSVPRecord>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRSVPRecord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RSVPRECORD,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRSVPRecord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RSVPRECORD,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRSVPRecord> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RSVPRECORD,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
