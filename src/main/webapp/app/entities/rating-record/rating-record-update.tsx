import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IItem } from 'app/shared/model/item.model';
import { getEntities as getItems } from 'app/entities/item/item.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './rating-record.reducer';
import { IRatingRecord } from 'app/shared/model/rating-record.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRatingRecordUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRatingRecordUpdateState {
  isNew: boolean;
  userId: string;
  itemId: string;
}

export class RatingRecordUpdate extends React.Component<IRatingRecordUpdateProps, IRatingRecordUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userId: '0',
      itemId: '0',
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
    this.props.getItems();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { ratingRecordEntity } = this.props;
      const entity = {
        ...ratingRecordEntity,
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
    this.props.history.push('/entity/rating-record');
  };

  render() {
    const { ratingRecordEntity, users, items, loading, updating } = this.props;
    const { isNew } = this.state;

    const { comment, commentContentType } = ratingRecordEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="mumLifeApp.ratingRecord.home.createOrEditLabel">Create or edit a RatingRecord</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : ratingRecordEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="rating-record-id">ID</Label>
                    <AvInput id="rating-record-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="rateValueLabel" for="rating-record-rateValue">
                    Rate Value
                  </Label>
                  <AvField id="rating-record-rateValue" type="string" className="form-control" name="rateValue" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="commentLabel" for="comment">
                      Comment
                    </Label>
                    <br />
                    {comment ? (
                      <div>
                        <a onClick={openFile(commentContentType, comment)}>Open</a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {commentContentType}, {byteSize(comment)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('comment')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_comment" type="file" onChange={this.onBlobChange(false, 'comment')} />
                    <AvInput type="hidden" name="comment" value={comment} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label for="rating-record-user">User</Label>
                  <AvInput id="rating-record-user" type="select" className="form-control" name="user.id">
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
                  <Label for="rating-record-item">Item</Label>
                  <AvInput id="rating-record-item" type="select" className="form-control" name="item.id">
                    <option value="" key="0" />
                    {items
                      ? items.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/rating-record" replace color="info">
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
  items: storeState.item.entities,
  ratingRecordEntity: storeState.ratingRecord.entity,
  loading: storeState.ratingRecord.loading,
  updating: storeState.ratingRecord.updating,
  updateSuccess: storeState.ratingRecord.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getItems,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RatingRecordUpdate);
