import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';

import { IRootState } from './../../shared/reducers';
import Metrics from './../administration/metrics/metrics';
import Event from './../../entities/event';
import Item from './../../entities/item';

export type IHomeProp = StateProps;

export const Home = (props: IHomeProp) => {
  const { account, isAuthenticated } = props;

  return (
    <Row>
      <Col md="10" style={{ margin: '0 auto'}}>
        {account && account.username ? (
          <div>
            <div style={{ textAlign: 'right' }}>
              <Alert color="info">Welcom, {account.username}.</Alert>
            </div>
            { account.authorities.indexOf('ROLE_ADMIN') > -1 ? (
              <Metrics />
            ) :
              isAuthenticated && account.authorities.indexOf('ROLE_STUDENT') > -1 ? (
                <div>
                  <Event match={{url:""}} />
                </div>
              ) : account.authorities.indexOf('ROLE_ORGANIZER') > -1 ? (
                    <div>
                      <Event match={{url:""}} />
                    </div>
                  ) : account.authorities.indexOf('ROLE_LENDER') > -1 ? (
                        <div>
                          <Item match={{url:""}} />
                        </div>
                      ) : ''}
          </div>
        ) : (
          <div>
            <h2>Welcome to MUM Life!</h2>
            <p className="lead">A Platform to Discover Events and Rent Items in Maharishi University of Management.</p>
            <Alert color="info">
              If you want to
              <Link to="/login" className="alert-link">
                {' '}
                sign in
              </Link>
              , you can try the default accounts:
              <br />- Administrator (username=&quot;admin&quot; and password=&quot;admin&quot;)
              <br />- Student (username=&quot;student&quot; and password=&quot;user&quot;)
              <br />- Organizer (username=&quot;organizer&quot; and password=&quot;user&quot;)
              <br />- Lender (username=&quot;lender&quot; and password=&quot;user&quot;).
            </Alert>

            <Alert color="info">
              You do not have an account yet?&nbsp;
              <Link to="/account/register" className="alert-link">
                Register a new account
              </Link>
            </Alert>

          </div>
        )}
      </Col>
      {/* <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col> */}
    </Row>
  );
};

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated
});

type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(mapStateToProps)(Home);
