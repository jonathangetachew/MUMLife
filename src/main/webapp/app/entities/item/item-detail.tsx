import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './item.reducer';
import { APP_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { hasAuthority } from 'app/shared/auth/private-route';

export interface IItemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> { }

export class ItemDetail extends React.Component<IItemDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { itemEntity } = this.props;
    const isManager = hasAuthority([AUTHORITIES.ADMIN, AUTHORITIES.LENDER]);
    const isStudent = hasAuthority([AUTHORITIES.STUDENT]);
    return (
      <Row>
        <Col md="8">
          <h2>
            Item [<b>{itemEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{itemEntity.name}</dd>
            <dt>
              <span id="image">Image</span>
            </dt>
            <dd>
              {itemEntity.image ? (
                <div>
                  <a onClick={openFile(itemEntity.imageContentType, itemEntity.image)}>
                    <img src={`data:${itemEntity.imageContentType};base64,${itemEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {itemEntity.imageContentType}, {byteSize(itemEntity.image)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="status">Status</span>
            </dt>
            <dd>{itemEntity.status}</dd>
            <dt>
              <span id="createdAt">Created At</span>
            </dt>
            <dd>
              <TextFormat value={itemEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>Type</dt>
            <dd>{itemEntity.type ? itemEntity.type.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/item" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          {isManager && <Button tag={Link} to={`/entity/item/${itemEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>}

          {(isStudent || isManager) && <Button tag={Link} to={`/entity/reservation-record/new/${itemEntity.id}/`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Reserve</span>
          </Button>}
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ item }: IRootState) => ({
  itemEntity: item.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ItemDetail);
