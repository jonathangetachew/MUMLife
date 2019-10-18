import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './reservation-record.reducer';
import { IReservationRecord } from 'app/shared/model/reservation-record.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IReservationRecordProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class ReservationRecord extends React.Component<IReservationRecordProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { reservationRecordList, match } = this.props;
    return (
      <div>
        <h2 id="reservation-record-heading">
          Reservation Records
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Reservation Record
          </Link>
        </h2>
        <div className="table-responsive">
          {reservationRecordList && reservationRecordList.length > 0 ? (
            <Table responsive aria-describedby="reservation-record-heading">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Expiration Date</th>
                  <th>Created At</th>
                  <th>User</th>
                  <th>Item</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {reservationRecordList.map((reservationRecord, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${reservationRecord.id}`} color="link" size="sm">
                        {reservationRecord.id}
                      </Button>
                    </td>
                    <td>
                      <TextFormat type="date" value={reservationRecord.expirationDate} format={APP_DATE_FORMAT} />
                    </td>
                    <td>
                      <TextFormat type="date" value={reservationRecord.createdAt} format={APP_DATE_FORMAT} />
                    </td>
                    <td>{reservationRecord.user ? reservationRecord.user.username : ''}</td>
                    <td>
                      {reservationRecord.item ? <Link to={`item/${reservationRecord.item.id}`}>{reservationRecord.item.id}</Link> : ''}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${reservationRecord.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${reservationRecord.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${reservationRecord.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">No Reservation Records found</div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ reservationRecord }: IRootState) => ({
  reservationRecordList: reservationRecord.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ReservationRecord);
