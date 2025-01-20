import 'bootstrap/dist/css/bootstrap.min.css';
import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle, faTrashAlt } from '@fortawesome/free-solid-svg-icons'; // Icône de confirmation et corbeille
import { FaUserCircle } from 'react-icons/fa';

const MyReservations = () => {
  
  const [user, setUser] = useState(null);
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const location = useLocation();
  const navigate = useNavigate();
  const [seatsReserved, setSeatsReserved] = useState(1);
  const [dropdownVisible, setDropdownVisible] = useState(false);


  const { eventName, eventId, userId, username, email } = location.state || {};

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      const user = JSON.parse(userData);
      setUser(user);
    }
    const fetchReservations = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get(
          `http://localhost:8089/reservation?userId=${userId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setReservations(response.data);
        setLoading(false);
      } catch (error) {
        console.error('Erreur lors de la récupération des réservations:', error);
        setError('Erreur lors de la récupération des réservations.');
        setLoading(false);
      }
    };

    if (userId) {
      fetchReservations();
    } else {
      setError("L'identifiant utilisateur est manquant.");
      setLoading(false);
    }
  }, [userId]);

  const handleCancelReservation = async (reservationId) => {
    try {
      const token = localStorage.getItem('token');
      await axios.delete(`http://localhost:8089/reservation/${reservationId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setReservations((prevReservations) =>
        prevReservations.filter((reservation) => reservation.id !== reservationId)
      );
      alert('Réservation annulée avec succès.');
    } catch (error) {
      console.error('Erreur lors de l\'annulation de la réservation:', error);
      alert('Échec de l\'annulation de la réservation.');
    }
  };
  const handleLogout = () => {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    setUser(null);
    navigate('/login');
  };

  const toggleDropdown = () => {
    setDropdownVisible(!dropdownVisible);
  };

  
  if (loading) {
    return <p>Chargement des réservations...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  return (
    <div className="container mt-4">
      {/* Navbar */}
      <header className="d-flex justify-content-between align-items-center mb-4">
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
          }}
        >
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

      {/* Titre "Mes Réservations" avec même style que la page Home */}
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
        Mes Réservations
      </div>

      {reservations.length === 0 ? (
        <p>Aucune réservation trouvée.</p>
      ) : (
        <div className="table-responsive">
          <table className="table table-striped table-hover table-bordered">
            <thead className="table-primary">
              <tr>
                <th>Nom de l'événement</th>
                <th>Date de réservation</th>
                <th>Places réservées</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {reservations.map((reservation) => (
                <tr key={reservation.id}>
                  <td>{eventName}</td>
                  <td>{new Date(reservation.reservationDate).toLocaleDateString()}</td>
                  <td>{reservation.seatsReserved}</td>
                  <td>
                    {/* Icône de confirmation avec style */}
                    <span
                      onClick={() =>
                        navigate('/ConfirmReservation', {
                          state: {
                            reservationId: reservation.id,
                            eventName,
                            eventId,
                            userId,
                            username,
                            seatsReserved: reservation.seatsReserved,
                            email,
                          },
                        })
                      }
                      style={{
                        cursor: 'pointer',
                        marginRight: '20px',
                        fontSize: '25px',
                        color: '#7e00ff', // Couleur de l'icône
                        padding: '10px',
                      }}
                    >
                      <FontAwesomeIcon icon={faCheckCircle} />
                    </span>

                    {/* Icône de corbeille avec style */}
                    <span
                      onClick={() => handleCancelReservation(reservation.id)}
                      style={{
                        cursor: 'pointer',
                        fontSize: '20px',
                        color: '#7e00ff', // Couleur de l'icône
                        padding: '10px',
                        marginLeft: '10px',
                      }}
                    >
                      <FontAwesomeIcon icon={faTrashAlt} />
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default MyReservations;
