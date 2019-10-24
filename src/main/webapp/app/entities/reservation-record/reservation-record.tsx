import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './reservation-record.reducer';
import { hasAuthority } from 'app/shared/auth/private-route';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IReservationRecordProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> { }

export type IReservationRecordState = IPaginationBaseState;

export class ReservationRecord extends React.Component<IReservationRecordProps, IReservationRecordState> {
  state: IReservationRecordState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate() {
    if (this.props.updateSuccess) {
      this.reset();
    }
  }

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 1 }, () => {
      this.getEntities();
    });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => {
        this.reset();
      }
    );
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { reservationRecordList, match } = this.props;

    const isManager = hasAuthority([AUTHORITIES.ADMIN, AUTHORITIES.LENDER]);

    return (
      <div>
        <h2 id="reservation-record-heading">
          Reservation Records
          {isManager && <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Reservation Record
          </Link>}
        </h2>
        <div className="table-responsive">
          <InfiniteScroll
            pageStart={this.state.activePage}
            loadMore={this.handleLoadMore}
            hasMore={this.state.activePage - 1 < this.props.links.next}
            loader={<div className="loader">Loading ...</div>}
            threshold={0}
            initialLoad={false}
          >
            {reservationRecordList && reservationRecordList.length > 0 ? (
              <Table responsive aria-describedby="reservation-record-heading">
                <thead>
                  <tr>
                    <th className="hand" onClick={this.sort('id')}>
                      ID <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('expirationDate')}>
                      Expiration Date <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('createdAt')}>
                      Created At <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      User <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      Item <FontAwesomeIcon icon="sort" />
                    </th>
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
                          {isManager && <Button tag={Link} to={`${match.url}/${reservationRecord.id}/edit`} color="primary" size="sm">
                            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                          </Button>}
                          {isManager && <Button tag={Link} to={`${match.url}/${reservationRecord.id}/delete`} color="danger" size="sm">
                            <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                          </Button>}
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            ) : (
                <div className="alert alert-warning">No Reservation Records found</div>
              )}
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ reservationRecord }: IRootState) => ({
  reservationRecordList: reservationRecord.entities,
  totalItems: reservationRecord.totalItems,
  links: reservationRecord.links,
  entity: reservationRecord.entity,
  updateSuccess: reservationRecord.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ReservationRecord);
