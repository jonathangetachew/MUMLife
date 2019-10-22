import React from "react";
import { NavLink } from 'react-router-dom';

import "./footer.scss";

function Footer(props) {
  return (
    <div className="FooterComponent__container container">
      <div className="brand left">
        <NavLink to="/">
          {/* <img src="https://uploads.divjoy.com/logo.svg" alt="Logo" /> */}
          MUM Life
        </NavLink>
      </div>
      <div className="links right">
        <NavLink to="/about">About</NavLink>
        <NavLink to="/faq">FAQ</NavLink>
        <NavLink to="/contact">Contact</NavLink>
      </div>
      <div className="social right"></div>
      <div className="copyright left">© 2019 MUM Life</div>
    </div>
  );
}

export default Footer;
