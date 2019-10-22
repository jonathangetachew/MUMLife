import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICheckoutRecord, defaultValue } from 'app/shared/model/checkout-record.model';

export const ACTION_TYPES = {
  FETCH_CHECKOUTRECORD_LIST: 'checkoutRecord/FETCH_CHECKOUTRECORD_LIST',
  FETCH_CHECKOUTRECORD: 'checkoutRecord/FETCH_CHECKOUTRECORD',
  CREATE_CHECKOUTRECORD: 'checkoutRecord/CREATE_CHECKOUTRECORD',
  UPDATE_CHECKOUTRECORD: 'checkoutRecord/UPDATE_CHECKOUTRECORD',
  DELETE_CHECKOUTRECORD: 'checkoutRecord/DELETE_CHECKOUTRECORD',
  RESET: 'checkoutRecord/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICheckoutRecord>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CheckoutRecordState = Readonly<typeof initialState>;

// Reducer

export default (state: CheckoutRecordState = initialState, action): CheckoutRecordState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CHECKOUTRECORD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CHECKOUTRECORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CHECKOUTRECORD):
    case REQUEST(ACTION_TYPES.UPDATE_CHECKOUTRECORD):
    case REQUEST(ACTION_TYPES.DELETE_CHECKOUTRECORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CHECKOUTRECORD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CHECKOUTRECORD):
    case FAILURE(ACTION_TYPES.CREATE_CHECKOUTRECORD):
    case FAILURE(ACTION_TYPES.UPDATE_CHECKOUTRECORD):
    case FAILURE(ACTION_TYPES.DELETE_CHECKOUTRECORD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CHECKOUTRECORD_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_CHECKOUTRECORD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CHECKOUTRECORD):
    case SUCCESS(ACTION_TYPES.UPDATE_CHECKOUTRECORD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CHECKOUTRECORD):
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

const apiUrl = 'api/checkout-records';

// Actions

export const getEntities: ICrudGetAllAction<ICheckoutRecord> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CHECKOUTRECORD_LIST,
    payload: axios.get<ICheckoutRecord>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICheckoutRecord> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CHECKOUTRECORD,
    payload: axios.get<ICheckoutRecord>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICheckoutRecord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CHECKOUTRECORD,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICheckoutRecord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CHECKOUTRECORD,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICheckoutRecord> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CHECKOUTRECORD,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
