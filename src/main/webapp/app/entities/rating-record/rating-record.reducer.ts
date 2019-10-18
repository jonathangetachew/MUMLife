import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRatingRecord, defaultValue } from 'app/shared/model/rating-record.model';

export const ACTION_TYPES = {
  FETCH_RATINGRECORD_LIST: 'ratingRecord/FETCH_RATINGRECORD_LIST',
  FETCH_RATINGRECORD: 'ratingRecord/FETCH_RATINGRECORD',
  CREATE_RATINGRECORD: 'ratingRecord/CREATE_RATINGRECORD',
  UPDATE_RATINGRECORD: 'ratingRecord/UPDATE_RATINGRECORD',
  DELETE_RATINGRECORD: 'ratingRecord/DELETE_RATINGRECORD',
  SET_BLOB: 'ratingRecord/SET_BLOB',
  RESET: 'ratingRecord/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRatingRecord>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RatingRecordState = Readonly<typeof initialState>;

// Reducer

export default (state: RatingRecordState = initialState, action): RatingRecordState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RATINGRECORD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RATINGRECORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RATINGRECORD):
    case REQUEST(ACTION_TYPES.UPDATE_RATINGRECORD):
    case REQUEST(ACTION_TYPES.DELETE_RATINGRECORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_RATINGRECORD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RATINGRECORD):
    case FAILURE(ACTION_TYPES.CREATE_RATINGRECORD):
    case FAILURE(ACTION_TYPES.UPDATE_RATINGRECORD):
    case FAILURE(ACTION_TYPES.DELETE_RATINGRECORD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_RATINGRECORD_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_RATINGRECORD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RATINGRECORD):
    case SUCCESS(ACTION_TYPES.UPDATE_RATINGRECORD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RATINGRECORD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/rating-records';

// Actions

export const getEntities: ICrudGetAllAction<IRatingRecord> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RATINGRECORD_LIST,
  payload: axios.get<IRatingRecord>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRatingRecord> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RATINGRECORD,
    payload: axios.get<IRatingRecord>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRatingRecord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RATINGRECORD,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRatingRecord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RATINGRECORD,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRatingRecord> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RATINGRECORD,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
