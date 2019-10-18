import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './rsvp-record.reducer';
import { IRSVPRecord } from 'app/shared/model/rsvp-record.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRSVPRecordProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class RSVPRecord extends React.Component<IRSVPRecordProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { rSVPRecordList, match } = this.props;
    return (
      <div>
        <h2 id="rsvp-record-heading">
          RSVP Records
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new RSVP Record
          </Link>
        </h2>
        <div className="table-responsive">
          {rSVPRecordList && rSVPRecordList.length > 0 ? (
            <Table responsive aria-describedby="rsvp-record-heading">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Created At</th>
                  <th>Status</th>
                  <th>User</th>
                  <th>Event</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {rSVPRecordList.map((rSVPRecord, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${rSVPRecord.id}`} color="link" size="sm">
                        {rSVPRecord.id}
                      </Button>
                    </td>
                    <td>
                      <TextFormat type="date" value={rSVPRecord.createdAt} format={APP_DATE_FORMAT} />
                    </td>
                    <td>{rSVPRecord.status}</td>
                    <td>{rSVPRecord.user ? rSVPRecord.user.username : ''}</td>
                    <td>{rSVPRecord.event ? <Link to={`event/${rSVPRecord.event.id}`}>{rSVPRecord.event.id}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${rSVPRecord.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${rSVPRecord.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${rSVPRecord.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">No RSVP Records found</div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ rSVPRecord }: IRootState) => ({
  rSVPRecordList: rSVPRecord.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RSVPRecord);
