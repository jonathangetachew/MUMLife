import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './reservation-record.reducer';
import { IReservationRecord } from 'app/shared/model/reservation-record.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IReservationRecordDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ReservationRecordDetail extends React.Component<IReservationRecordDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { reservationRecordEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            ReservationRecord [<b>{reservationRecordEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="expirationDate">Expiration Date</span>
            </dt>
            <dd>
              <TextFormat value={reservationRecordEntity.expirationDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="createdAt">Created At</span>
            </dt>
            <dd>
              <TextFormat value={reservationRecordEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>User</dt>
            <dd>{reservationRecordEntity.user ? reservationRecordEntity.user.username : ''}</dd>
            <dt>Item</dt>
            <dd>{reservationRecordEntity.item ? reservationRecordEntity.item.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/reservation-record" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/reservation-record/${reservationRecordEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ reservationRecord }: IRootState) => ({
  reservationRecordEntity: reservationRecord.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ReservationRecordDetail);
