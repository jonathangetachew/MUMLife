import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { openFile, byteSize, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './rating-record.reducer';
import { IRatingRecord } from 'app/shared/model/rating-record.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRatingRecordProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class RatingRecord extends React.Component<IRatingRecordProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { ratingRecordList, match } = this.props;
    return (
      <div>
        <h2 id="rating-record-heading">
          Rating Records
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Rating Record
          </Link>
        </h2>
        <div className="table-responsive">
          {ratingRecordList && ratingRecordList.length > 0 ? (
            <Table responsive aria-describedby="rating-record-heading">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Rate Value</th>
                  <th>Comment</th>
                  <th>User</th>
                  <th>Item</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {ratingRecordList.map((ratingRecord, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${ratingRecord.id}`} color="link" size="sm">
                        {ratingRecord.id}
                      </Button>
                    </td>
                    <td>{ratingRecord.rateValue}</td>
                    <td>
                      {ratingRecord.comment ? (
                        <div>
                          <a onClick={openFile(ratingRecord.commentContentType, ratingRecord.comment)}>Open &nbsp;</a>
                          <span>
                            {ratingRecord.commentContentType}, {byteSize(ratingRecord.comment)}
                          </span>
                        </div>
                      ) : null}
                    </td>
                    <td>{ratingRecord.user ? ratingRecord.user.username : ''}</td>
                    <td>{ratingRecord.item ? <Link to={`item/${ratingRecord.item.id}`}>{ratingRecord.item.id}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${ratingRecord.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${ratingRecord.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${ratingRecord.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">No Rating Records found</div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ ratingRecord }: IRootState) => ({
  ratingRecordList: ratingRecord.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RatingRecord);
