import React, { useState, useEffect } from 'react';

import { connect } from 'react-redux';
import { AvForm, AvField } from 'availity-reactstrap-validation';
import { Row, Col, Alert, Button } from 'reactstrap';

import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { IRootState } from 'app/shared/reducers';
import { handleRegister, reset } from './register.reducer';

export type IRegisterProps = DispatchProps;

export const RegisterPage = (props: IRegisterProps) => {
  const [password, setPassword] = useState('');

  useEffect(() => () => props.reset(), []);

  const handleValidSubmit = (event, values) => {
    props.handleRegister(values.username, values.email, values.firstPassword);
    event.preventDefault();
  };

  const updatePassword = event => setPassword(event.target.value);

  return (
    <div style={{ margin: '2rem 0' }}>
      <Row className="justify-content-center">
        <Col md="8" style={{ margin: '1rem 0' }}>
          <h1 id="register-title">Registration</h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          <AvForm id="register-form" onValidSubmit={handleValidSubmit}>
            <AvField
              name="username"
              placeholder={'Username'}
              validate={{
                required: { value: true, errorMessage: 'Your username is required.' },
                pattern: { value: '^[_.@A-Za-z0-9-]*$', errorMessage: 'Your username can only contain letters and digits.' },
                minLength: { value: 1, errorMessage: 'Your username is required to be at least 1 character.' },
                maxLength: { value: 50, errorMessage: 'Your username cannot be longer than 50 characters.' }
              }}
            />
            <AvField
              name="email"
              placeholder={'Email'}
              type="email"
              validate={{
                required: { value: true, errorMessage: 'Email is required.' },
                minLength: { value: 5, errorMessage: 'Email is required to be at least 5 characters.' },
                maxLength: { value: 254, errorMessage: 'Email cannot be longer than 50 characters.' }
              }}
            />
            <AvField
              name="firstPassword"
              placeholder={'New Password'}
              type="password"
              onChange={updatePassword}
              validate={{
                required: { value: true, errorMessage: 'Password is required.' },
                minLength: { value: 4, errorMessage: 'Password is required to be at least 4 characters.' },
                maxLength: { value: 50, errorMessage: 'Password cannot be longer than 50 characters.' }
              }}
            />
            <PasswordStrengthBar password={password} />
            <AvField
              name="secondPassword"
              placeholder="Confirm Password"
              type="password"
              validate={{
                required: { value: true, errorMessage: 'Confirmation password is required.' },
                minLength: { value: 4, errorMessage: 'Confirmation password is required to be at least 4 characters.' },
                maxLength: { value: 50, errorMessage: 'Confirmation password cannot be longer than 50 characters.' },
                match: { value: 'firstPassword', errorMessage: 'The password and its confirmation do not match!' }
              }}
            />
            <Button id="register-submit" color="primary" type="submit">
              Register
            </Button>
          </AvForm>
        </Col>
      </Row>
    </div>
  );
};

const mapDispatchToProps = { handleRegister, reset };
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  null,
  mapDispatchToProps
)(RegisterPage);
