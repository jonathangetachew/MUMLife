import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers, getUser } from 'app/modules/administration/user-management/user-management.reducer';
import { IItem } from 'app/shared/model/item.model';
import { getEntities as getItems } from 'app/entities/item/item.reducer';
import { getEntity, updateEntity, createEntity, reset } from './reservation-record.reducer';
import { IReservationRecord } from 'app/shared/model/reservation-record.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import moment from 'moment';
import { hasAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

export interface IReservationRecordUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string, item: string }> { }

export interface IReservationRecordUpdateState {
  isNew: boolean;
  userId: string;
  itemId: string;
}

export class ReservationRecordUpdate extends React.Component<IReservationRecordUpdateProps, IReservationRecordUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userId: '0',
      itemId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id || 'item' in this.props.match.params
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getUsers();
    this.props.getItems();
  }

  saveEntity = (event, errors, values) => {
    values.expirationDate = convertDateTimeToServer(values.expirationDate);
    values.createdAt = convertDateTimeToServer(values.createdAt);

    if (errors.length === 0) {
      const { reservationRecordEntity } = this.props;
      const entity = {
        ...reservationRecordEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/reservation-record');
  };

  render() {
    const { reservationRecordEntity, users, account, items, loading, updating, match } = this.props;
    const { isNew } = this.state;
    const { item } = match && match.params ? match.params : undefined;
    const isManager = hasAuthority([AUTHORITIES.ADMIN, AUTHORITIES.LENDER]);

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="mumLifeApp.reservationRecord.home.createOrEditLabel">{isNew ? 'Create' : 'Edit'} Reservation</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
                <AvForm model={isNew ? {} : reservationRecordEntity} onSubmit={this.saveEntity}>
                  {!isNew ? (
                    <AvGroup>
                      <Label for="reservation-record-id">ID</Label>
                      <AvInput id="reservation-record-id" type="text" className="form-control" name="id" required readOnly />
                    </AvGroup>
                  ) : null}
                  <AvGroup>
                    <Label id="expirationDateLabel" for="reservation-record-expirationDate">
                      Expiration Date
                  </Label>
                    <AvInput
                      id="reservation-record-expirationDate"
                      type="datetime-local"
                      className="form-control"
                      name="expirationDate"
                      placeholder={'YYYY-MM-DD HH:mm'}
                      value={isNew ? null : convertDateTimeFromServer(this.props.reservationRecordEntity.expirationDate)}
                      validate={{
                        required: { value: true, errorMessage: 'This field is required.' }
                      }}
                    />
                  </AvGroup>
                  <AvGroup>
                    <AvInput id="reservation-record-createdAt" type="hidden" name="createdAt" value={isNew ? convertDateTimeFromServer(moment.now()) : convertDateTimeFromServer(this.props.reservationRecordEntity.createdAt)} />
                  </AvGroup>
                  <AvGroup>
                    {!item && <Label for="reservation-record-user">User</Label>}
                    {(!item || isManager )? <AvInput id="reservation-record-user" type="select" className="form-control" name="user.id">
                      <option value="" key="0" />
                      {users
                        ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.username}
                          </option>
                        ))
                        : null}
                    </AvInput> : <AvInput id="reservation-record-user" type="hidden" name="user.id" value={account.id} />}
                  </AvGroup>
                  <AvGroup>
                    {!item && <Label for="reservation-record-item">Item</Label>}
                    {!item ? <AvInput id="reservation-record-item" type="select" className="form-control" name="item.id">
                      <option value="" key="0" />
                      {items
                        ? items.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                        : null}
                    </AvInput> : <AvInput id="reservation-record-item" type="hidden" name="item.id" value={item} />}
                  </AvGroup>
                  <Button tag={Link} id="cancel-save" to="/entity/reservation-record" replace color="info">
                    <FontAwesomeIcon icon="arrow-left" />
                    &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                  </Button>
                  &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                    <FontAwesomeIcon icon="save" />
                    &nbsp; Save
                </Button>
                </AvForm>
              )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  account: storeState.authentication.account,
  users: storeState.userManagement.users,
  items: storeState.item.entities,
  reservationRecordEntity: storeState.reservationRecord.entity,
  loading: storeState.reservationRecord.loading,
  updating: storeState.reservationRecord.updating,
  updateSuccess: storeState.reservationRecord.updateSuccess
});

const mapDispatchToProps = {
  getUser,
  getUsers,
  getItems,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ReservationRecordUpdate);
