import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import {
  openFile,
  byteSize,
  ICrudGetAllAction,
  TextFormat,
  getSortState,
  IPaginationBaseState,
  JhiPagination,
  JhiItemCount
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './item.reducer';
import { APP_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { hasAuthority } from 'app/shared/auth/private-route';
import { ItemStatus } from 'app/shared/model/enumerations/item-status.model';

export interface IItemProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> { }

export type IItemState = IPaginationBaseState & { type: number };

export class Item extends React.Component<IItemProps, IItemState> {
  state = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE),
    type: 0
  };

  componentDidMount() {
    this.getEntities();
  }

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  handleChange = (event) => {
    this.setState({ type: event.target.value || 0 });
  };

  render() {
    const { itemList, match, totalItems, itemTypes } = this.props;
    const list = itemList.filter(item => (this.state.type.toString() === "0" || (item.type && this.state.type.toString() === item.type.id.toString())));
    const isManager = hasAuthority([AUTHORITIES.ADMIN, AUTHORITIES.LENDER]);
    return (
      <div>
        <h2 id="item-heading">
          Items
          {isManager && <Link to={`/entity/item/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Item
          </Link>}
        </h2>
        {(itemTypes && itemTypes.length > 0) && <div>
          <span>Type</span>
          <select id="item-type" className="form-control" onChange={this.handleChange} value={this.state.type}>
            <option value={0} key={0}>All</option>
            {itemTypes
              ? itemTypes.filter(type => type !== null).map(type => (
                <option value={type.id} key={type.id}>
                  {type.name}
                </option>
              ))
              : null}
          </select>
        </div>
        }
        <div className="table-responsive">
          {list && list.length > 0 ? (
            <Table responsive aria-describedby="item-heading">
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('id')}>
                    ID <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('name')}>
                    Name <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('image')}>
                    Image <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('status')}>
                    Status <FontAwesomeIcon icon="sort" />
                  </th>
                  {isManager && <th className="hand" onClick={this.sort('createdAt')}>
                    Created At <FontAwesomeIcon icon="sort" />
                  </th>}
                  <th>
                    Type <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {list.map((item, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`/entity/item/${item.id}`} color="link" size="sm">
                        {item.id}
                      </Button>
                    </td>
                    <td>{item.name}</td>
                    <td>
                      {item.image ? (
                        <div>
                          <a onClick={openFile(item.imageContentType, item.image)}>
                            <img src={`data:${item.imageContentType};base64,${item.image}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                          <span>
                            {item.imageContentType}, {byteSize(item.image)}
                          </span>
                        </div>
                      ) : null}
                    </td>
                    <td>{item.status}</td>
                    {isManager && <td><TextFormat type="date" value={item.createdAt} format={APP_DATE_FORMAT} /></td>}
                    <td>{item.type ? <Link to={`item-type/${item.type.id}`}>{item.type.name}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/entity/item/${item.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        {isManager && <Button tag={Link} to={`/entity/item/${item.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>}
                        {isManager && <Button tag={Link} to={`/entity/item/${item.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>}
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
        <div className={itemList && itemList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiItemCount page={this.state.activePage} total={totalItems} itemsPerPage={this.state.itemsPerPage} />
          </Row>
          <Row className="justify-content-center">
            <JhiPagination
              activePage={this.state.activePage}
              onSelect={this.handlePagination}
              maxButtons={5}
              itemsPerPage={this.state.itemsPerPage}
              totalItems={totalItems}
            />
          </Row>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ item }: IRootState) => {
  const itemList = hasAuthority([AUTHORITIES.ADMIN, AUTHORITIES.LENDER]) ? item.entities : item.entities.filter(i => i.status === ItemStatus.AVAILABLE);
  return {
    itemList,
    itemTypes: itemList.map(i => i.type).filter(i => i !== null),
    totalItems: hasAuthority([AUTHORITIES.ADMIN, AUTHORITIES.LENDER]) ? item.totalItems : itemList.length
  };
};

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Item);
