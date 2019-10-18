import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './checkout-record.reducer';
import { ICheckoutRecord } from 'app/shared/model/checkout-record.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICheckoutRecordProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class CheckoutRecord extends React.Component<ICheckoutRecordProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { checkoutRecordList, match } = this.props;
    return (
      <div>
        <h2 id="checkout-record-heading">
          Checkout Records
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Checkout Record
          </Link>
        </h2>
        <div className="table-responsive">
          {checkoutRecordList && checkoutRecordList.length > 0 ? (
            <Table responsive aria-describedby="checkout-record-heading">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Due Date</th>
                  <th>Created At</th>
                  <th>User</th>
                  <th>Item</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {checkoutRecordList.map((checkoutRecord, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${checkoutRecord.id}`} color="link" size="sm">
                        {checkoutRecord.id}
                      </Button>
                    </td>
                    <td>
                      <TextFormat type="date" value={checkoutRecord.dueDate} format={APP_DATE_FORMAT} />
                    </td>
                    <td>
                      <TextFormat type="date" value={checkoutRecord.createdAt} format={APP_DATE_FORMAT} />
                    </td>
                    <td>{checkoutRecord.user ? checkoutRecord.user.username : ''}</td>
                    <td>{checkoutRecord.item ? <Link to={`item/${checkoutRecord.item.id}`}>{checkoutRecord.item.id}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${checkoutRecord.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${checkoutRecord.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${checkoutRecord.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">No Checkout Records found</div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ checkoutRecord }: IRootState) => ({
  checkoutRecordList: checkoutRecord.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CheckoutRecord);
