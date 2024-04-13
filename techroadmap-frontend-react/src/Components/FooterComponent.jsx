import React from "react";

const footerStyle = {
    position: 'relative',
    bottom: 0,
    left: 0,
    width: '100%',
    backgroundColor: 'rgba(0, 0, 0, 0.05)',
    textAlign: 'center',
    padding: '1rem'
  };

function FooterComponent() {
    return (
        <footer style={footerStyle}>
            <div>
                © 2020 Copyright: 
                <a> Suresh - Roadmaps</a>
            </div>
        </footer>

    )
}

export default FooterComponent