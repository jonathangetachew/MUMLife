import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './rating-record.reducer';
import { IRatingRecord } from 'app/shared/model/rating-record.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRatingRecordDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RatingRecordDetail extends React.Component<IRatingRecordDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { ratingRecordEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            RatingRecord [<b>{ratingRecordEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="rateValue">Rate Value</span>
            </dt>
            <dd>{ratingRecordEntity.rateValue}</dd>
            <dt>
              <span id="comment">Comment</span>
            </dt>
            <dd>
              {ratingRecordEntity.comment ? (
                <div>
                  <a onClick={openFile(ratingRecordEntity.commentContentType, ratingRecordEntity.comment)}>Open&nbsp;</a>
                  <span>
                    {ratingRecordEntity.commentContentType}, {byteSize(ratingRecordEntity.comment)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>User</dt>
            <dd>{ratingRecordEntity.user ? ratingRecordEntity.user.username : ''}</dd>
            <dt>Item</dt>
            <dd>{ratingRecordEntity.item ? ratingRecordEntity.item.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/rating-record" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/rating-record/${ratingRecordEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ ratingRecord }: IRootState) => ({
  ratingRecordEntity: ratingRecord.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RatingRecordDetail);
