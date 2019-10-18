import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IItemType, defaultValue } from 'app/shared/model/item-type.model';

export const ACTION_TYPES = {
  FETCH_ITEMTYPE_LIST: 'itemType/FETCH_ITEMTYPE_LIST',
  FETCH_ITEMTYPE: 'itemType/FETCH_ITEMTYPE',
  CREATE_ITEMTYPE: 'itemType/CREATE_ITEMTYPE',
  UPDATE_ITEMTYPE: 'itemType/UPDATE_ITEMTYPE',
  DELETE_ITEMTYPE: 'itemType/DELETE_ITEMTYPE',
  RESET: 'itemType/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IItemType>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ItemTypeState = Readonly<typeof initialState>;

// Reducer

export default (state: ItemTypeState = initialState, action): ItemTypeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ITEMTYPE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ITEMTYPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ITEMTYPE):
    case REQUEST(ACTION_TYPES.UPDATE_ITEMTYPE):
    case REQUEST(ACTION_TYPES.DELETE_ITEMTYPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ITEMTYPE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ITEMTYPE):
    case FAILURE(ACTION_TYPES.CREATE_ITEMTYPE):
    case FAILURE(ACTION_TYPES.UPDATE_ITEMTYPE):
    case FAILURE(ACTION_TYPES.DELETE_ITEMTYPE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ITEMTYPE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ITEMTYPE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ITEMTYPE):
    case SUCCESS(ACTION_TYPES.UPDATE_ITEMTYPE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ITEMTYPE):
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

const apiUrl = 'api/item-types';

// Actions

export const getEntities: ICrudGetAllAction<IItemType> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ITEMTYPE_LIST,
  payload: axios.get<IItemType>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IItemType> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ITEMTYPE,
    payload: axios.get<IItemType>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IItemType> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ITEMTYPE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IItemType> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ITEMTYPE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IItemType> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ITEMTYPE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
