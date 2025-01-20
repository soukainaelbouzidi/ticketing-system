import 'bootstrap/dist/css/bootstrap.min.css';
import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { loadStripe } from '@stripe/stripe-js';
import axios from 'axios';
import { FaUserCircle } from 'react-icons/fa';

const stripePromise = loadStripe('pk_test_51QVfdtKycUC9MuJltcoIIPpv4TnlMYLeXXPS66pJjjyKglqwuZu4hFRaLVQ2aYdjd5hzGw25l6nZJDkIrpDExjIM00wsVUbR42');

const ConfirmReservation = () => {
  const [user, setUser] = useState(null);
  const [dropdownVisible, setDropdownVisible] = useState(false);

  const location = useLocation();
  const navigate = useNavigate();
  const { reservationId, eventName, eventId, userId, username, seatsReserved, email } = location.state || {};
  const amount = 50;

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      const user = JSON.parse(userData);
      setUser(user);
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    setUser(null);
    navigate('/login');
  };

  const toggleDropdown = () => {
    setDropdownVisible(!dropdownVisible);
  };

  const handlePayment = async () => {
    try {
      const stripe = await stripePromise;

      const response = await axios.post(
        'http://localhost:8086/api/payments/create-checkout-session',
        null,
        { params: { reservationId, amount, email, eventId } }
      );

      const sessionUrl = response.data;
      window.location.href = sessionUrl;
    } catch (error) {
      console.error('Erreur lors de l\'initialisation du paiement :', error);
      alert('Erreur lors de l\'initialisation du paiement.');
    }
  };

  return (
    <div className="container mt-4">
      {/* Navbar */}
      <header className="d-flex justify-content-between align-items-center mb-4">
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <img
            src="https://i.vimeocdn.com/portrait/43505883_640x640"
            alt="Logo EventMaster"
            style={{ width: '30px', marginRight: '15px' }}
          />
          <h1
            style={{
              fontSize: '2rem',
              fontWeight: 'bold',
              background: 'linear-gradient(90deg, #ff00ff, #7e00ff)',
              WebkitBackgroundClip: 'text',
              WebkitTextFillColor: 'transparent',
              marginBottom: '0px',
            }}
          >
            EventMaster
          </h1>
        </div>
        {user && (
          <div className="position-relative">
            <FaUserCircle
              size={40}
              onClick={toggleDropdown}
              style={{ cursor: 'pointer', color: '#7e00ff' }}
            />
            {dropdownVisible && (
              <div
                className="dropdown-menu dropdown-menu-right show"
                style={{
                  position: 'absolute',
                  top: '50px',
                  right: '0',
                  display: 'block',
                  boxShadow: '0px 4px 6px rgba(0,0,0,0.1)',
                }}
              >
                <div className="px-3 py-2">
                  <p className="mb-1"><strong>{user.username}</strong></p>
                  <p className="mb-1">{user.email}</p>
                </div>
                <div className="dropdown-divider"></div>
                <button
                  className="dropdown-item text-danger"
                  onClick={handleLogout}
                >
                  Se déconnecter
                </button>
              </div>
            )}
          </div>
        )}
      </header>

      {/* Title */}
      <div
        style={{
          textAlign: 'center',
          fontSize: '2rem',
          fontWeight: 'bold',
          background: 'linear-gradient(90deg, #ff00ff, #7e00ff)',
          WebkitBackgroundClip: 'text',
          WebkitTextFillColor: 'transparent',
          marginBottom: '20px',
        }}
      >
        Confirmation de la Réservation
      </div>

      {/* Reservation Details */}
      <table className="table table-bordered table-striped table-hover" style={{ borderColor: '#7e00ff' }}>
        <thead className="thead-dark" style={{ backgroundColor: '#7e00ff', color: '#fff' }}>
          <tr>
            <th>Information</th>
            <th>Détails</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td><strong>Nom de l'événement</strong></td>
            <td>{eventName}</td>
          </tr>
          <tr>
            <td><strong>Places réservées</strong></td>
            <td>{seatsReserved}</td>
          </tr>
          <tr>
            <td><strong>Montant à payer</strong></td>
            <td>{amount} €</td>
          </tr>
        </tbody>
      </table>

      {/* Payment Button */}
      <div className="d-flex justify-content-center mt-4">
        <button
          onClick={handlePayment}
          className="btn btn-primary shadow px-sm-4"
          style={{
            background: '#7e00ff',
            color: '#fff',
            padding: '12px 24px',
            border: 'none',
            borderRadius: '8px',
            cursor: 'pointer',
            transition: 'background 0.3s ease'
          }}
          onMouseOver={(e) => (e.target.style.background = '#ff00ff')}
          onMouseOut={(e) => (e.target.style.background = '#7e00ff')}
        >
          Payer
        </button>
      </div>
    </div>
  );
};

export default ConfirmReservation;
