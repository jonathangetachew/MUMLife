import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './item-type.reducer';
import { IItemType } from 'app/shared/model/item-type.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IItemTypeProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class ItemType extends React.Component<IItemTypeProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { itemTypeList, match } = this.props;
    return (
      <div>
        <h2 id="item-type-heading">
          Item Types
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Item Type
          </Link>
        </h2>
        <div className="table-responsive">
          {itemTypeList && itemTypeList.length > 0 ? (
            <Table responsive aria-describedby="item-type-heading">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {itemTypeList.map((itemType, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${itemType.id}`} color="link" size="sm">
                        {itemType.id}
                      </Button>
                    </td>
                    <td>{itemType.name}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${itemType.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${itemType.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${itemType.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">No Item Types found</div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ itemType }: IRootState) => ({
  itemTypeList: itemType.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ItemType);
