import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './rsvp-record.reducer';
import { IRSVPRecord } from 'app/shared/model/rsvp-record.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRSVPRecordDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RSVPRecordDetail extends React.Component<IRSVPRecordDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { rSVPRecordEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            RSVPRecord [<b>{rSVPRecordEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="createdAt">Created At</span>
            </dt>
            <dd>
              <TextFormat value={rSVPRecordEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="status">Status</span>
            </dt>
            <dd>{rSVPRecordEntity.status}</dd>
            <dt>User</dt>
            <dd>{rSVPRecordEntity.user ? rSVPRecordEntity.user.username : ''}</dd>
            <dt>Event</dt>
            <dd>{rSVPRecordEntity.event ? rSVPRecordEntity.event.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/rsvp-record" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/rsvp-record/${rSVPRecordEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ rSVPRecord }: IRootState) => ({
  rSVPRecordEntity: rSVPRecord.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RSVPRecordDetail);
