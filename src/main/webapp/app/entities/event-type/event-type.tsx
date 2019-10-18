import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './event-type.reducer';
import { IEventType } from 'app/shared/model/event-type.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEventTypeProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class EventType extends React.Component<IEventTypeProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { eventTypeList, match } = this.props;
    return (
      <div>
        <h2 id="event-type-heading">
          Event Types
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Event Type
          </Link>
        </h2>
        <div className="table-responsive">
          {eventTypeList && eventTypeList.length > 0 ? (
            <Table responsive aria-describedby="event-type-heading">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Types</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {eventTypeList.map((eventType, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${eventType.id}`} color="link" size="sm">
                        {eventType.id}
                      </Button>
                    </td>
                    <td>{eventType.name}</td>
                    <td>{eventType.types ? <Link to={`event/${eventType.types.id}`}>{eventType.types.id}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${eventType.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${eventType.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${eventType.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">No Event Types found</div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ eventType }: IRootState) => ({
  eventTypeList: eventType.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EventType);
