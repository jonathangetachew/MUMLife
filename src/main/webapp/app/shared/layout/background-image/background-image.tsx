import React from "react";
import "./background-image.scss";

function BackgroundImage(props) {
  return (
    <div
      className="BackgroundImage"
      style={{
        "--image": `url(${props.image})`,
        "--opacity": props.opacity
      }}
    />
  );
}

export default BackgroundImage;
