import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './item.reducer';
import { IItem } from 'app/shared/model/item.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IItemProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Item extends React.Component<IItemProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { itemList, match } = this.props;
    return (
      <div>
        <h2 id="item-heading">
          Items
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Item
          </Link>
        </h2>
        <div className="table-responsive">
          {itemList && itemList.length > 0 ? (
            <Table responsive aria-describedby="item-heading">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Image Url</th>
                  <th>Status</th>
                  <th>Created At</th>
                  <th>Type</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {itemList.map((item, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${item.id}`} color="link" size="sm">
                        {item.id}
                      </Button>
                    </td>
                    <td>{item.name}</td>
                    <td>{item.imageUrl}</td>
                    <td>{item.status}</td>
                    <td>
                      <TextFormat type="date" value={item.createdAt} format={APP_DATE_FORMAT} />
                    </td>
                    <td>{item.type ? <Link to={`item-type/${item.type.id}`}>{item.type.id}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${item.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${item.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${item.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">No Items found</div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ item }: IRootState) => ({
  itemList: item.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Item);
