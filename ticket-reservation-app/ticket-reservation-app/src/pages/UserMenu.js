// src/pages/UserMenu.js

import React, { useState } from 'react';
import { Dropdown, DropdownButton } from 'react-bootstrap';
import { FaUserCircle } from 'react-icons/fa';

const UserMenu = ({ user }) => {
  const [showMenu, setShowMenu] = useState(false);

  // Toggle the dropdown menu
  const handleToggle = () => setShowMenu(!showMenu);

  return (
    <div className="user-menu">
      {/* User icon in top-right corner */}
      <Dropdown show={showMenu} onToggle={handleToggle}>
        <Dropdown.Toggle variant="link" id="dropdown-user">
          <FaUserCircle size={30} />
        </Dropdown.Toggle>

        <Dropdown.Menu>
          <Dropdown.Item href="#/profile">Profil</Dropdown.Item>
          <Dropdown.Item href="#/settings">Paramètres</Dropdown.Item>
          <Dropdown.Item href="#/logout">Déconnexion</Dropdown.Item>
        </Dropdown.Menu>
      </Dropdown>

      {/* Optionally, display user info */}
      {user && (
        <div className="user-info">
          <p>Nom: {user.name}</p>
          <p>Email: {user.email}</p>
        </div>
      )}
    </div>
  );
};

export default UserMenu;
