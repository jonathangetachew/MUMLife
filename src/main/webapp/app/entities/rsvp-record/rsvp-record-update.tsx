import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IEvent } from 'app/shared/model/event.model';
import { getEntities as getEvents } from 'app/entities/event/event.reducer';
import { getEntity, updateEntity, createEntity, reset } from './rsvp-record.reducer';
import { IRSVPRecord } from 'app/shared/model/rsvp-record.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRSVPRecordUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRSVPRecordUpdateState {
  isNew: boolean;
  userId: string;
  eventId: string;
}

export class RSVPRecordUpdate extends React.Component<IRSVPRecordUpdateProps, IRSVPRecordUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userId: '0',
      eventId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getUsers();
    this.props.getEvents();
  }

  saveEntity = (event, errors, values) => {
    values.createdAt = convertDateTimeToServer(values.createdAt);

    if (errors.length === 0) {
      const { rSVPRecordEntity } = this.props;
      const entity = {
        ...rSVPRecordEntity,
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
    this.props.history.push('/entity/rsvp-record');
  };

  render() {
    const { rSVPRecordEntity, users, events, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="mumLifeApp.rSVPRecord.home.createOrEditLabel">Create or edit a RSVPRecord</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : rSVPRecordEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="rsvp-record-id">ID</Label>
                    <AvInput id="rsvp-record-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="createdAtLabel" for="rsvp-record-createdAt">
                    Created At
                  </Label>
                  <AvInput
                    id="rsvp-record-createdAt"
                    type="datetime-local"
                    className="form-control"
                    name="createdAt"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.rSVPRecordEntity.createdAt)}
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel" for="rsvp-record-status">
                    Status
                  </Label>
                  <AvInput
                    id="rsvp-record-status"
                    type="select"
                    className="form-control"
                    name="status"
                    value={(!isNew && rSVPRecordEntity.status) || 'GOING'}
                  >
                    <option value="GOING">GOING</option>
                    <option value="INTERESTED">INTERESTED</option>
                    <option value="NONE">NONE</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="rsvp-record-user">User</Label>
                  <AvInput id="rsvp-record-user" type="select" className="form-control" name="user.id">
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.username}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="rsvp-record-event">Event</Label>
                  <AvInput id="rsvp-record-event" type="select" className="form-control" name="event.id">
                    <option value="" key="0" />
                    {events
                      ? events.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/rsvp-record" replace color="info">
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
  users: storeState.userManagement.users,
  events: storeState.event.entities,
  rSVPRecordEntity: storeState.rSVPRecord.entity,
  loading: storeState.rSVPRecord.loading,
  updating: storeState.rSVPRecord.updating,
  updateSuccess: storeState.rSVPRecord.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getEvents,
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
)(RSVPRecordUpdate);
