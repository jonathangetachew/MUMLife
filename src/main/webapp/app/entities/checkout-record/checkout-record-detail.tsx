import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './checkout-record.reducer';
import { ICheckoutRecord } from 'app/shared/model/checkout-record.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICheckoutRecordDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CheckoutRecordDetail extends React.Component<ICheckoutRecordDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { checkoutRecordEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            CheckoutRecord [<b>{checkoutRecordEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="active">Active</span>
            </dt>
            <dd>{checkoutRecordEntity.active ? 'true' : 'false'}</dd>
            <dt>
              <span id="dueDate">Due Date</span>
            </dt>
            <dd>
              <TextFormat value={checkoutRecordEntity.dueDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="createdAt">Created At</span>
            </dt>
            <dd>
              <TextFormat value={checkoutRecordEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>User</dt>
            <dd>{checkoutRecordEntity.user ? checkoutRecordEntity.user.username : ''}</dd>
            <dt>Item</dt>
            <dd>{checkoutRecordEntity.item ? checkoutRecordEntity.item.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/checkout-record" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/checkout-record/${checkoutRecordEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ checkoutRecord }: IRootState) => ({
  checkoutRecordEntity: checkoutRecord.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CheckoutRecordDetail);
