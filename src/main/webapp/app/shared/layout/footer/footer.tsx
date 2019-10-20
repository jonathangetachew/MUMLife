import React from "react";
import { NavLink } from 'react-router-dom';
import Section from "./../section/section";
import "./footer.scss";

function Footer(props) {
  return (
    <Section color="light" size="normal">
      <div className="FooterComponent__container container">
        <div className="brand left">
          <NavLink to="/">
            <img src="https://uploads.divjoy.com/logo.svg" alt="Logo" />
          </NavLink>
        </div>
        <div className="links right">
          <NavLink to="/about">About</NavLink>
          <NavLink to="/faq">FAQ</NavLink>
          <NavLink to="/contact">Contact</NavLink>
          <a
            target="_blank"
            href="https://medium.com"
            rel="noopener noreferrer"
          >
            Blog
          </a>
        </div>
        <div className="social right">
          <a
            href="https://twitter.com"
            target="_blank"
            rel="noopener noreferrer"
          >
            <span className="icon">
              <i className="fab fa-twitter" />
            </span>
          </a>
          <a
            href="https://facebook.com"
            target="_blank"
            rel="noopener noreferrer"
          >
            <span className="icon">
              <i className="fab fa-facebook-f" />
            </span>
          </a>
          <a
            href="https://instagram.com"
            target="_blank"
            rel="noopener noreferrer"
          >
            <span className="icon">
              <i className="fab fa-instagram" />
            </span>
          </a>
        </div>
        <div className="copyright left">© 2019 MUMLife</div>
      </div>
    </Section>
  );
}

export default Footer;
