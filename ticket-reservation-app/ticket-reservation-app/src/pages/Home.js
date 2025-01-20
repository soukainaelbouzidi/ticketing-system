import 'bootstrap/dist/css/bootstrap.min.css';
import React, { useEffect, useState } from 'react';
import { getEvents } from './API';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { FaUserCircle } from 'react-icons/fa';

const Home = () => {
  const [user, setUser] = useState(null);
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [seatsReserved, setSeatsReserved] = useState(1);
  const [dropdownVisible, setDropdownVisible] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      const user = JSON.parse(userData);
      setUser(user);
    }

    const fetchEvents = async () => {
      try {
        const data = await getEvents();
        setEvents(data);
        setLoading(false);
      } catch (error) {
        setError('Erreur lors de la récupération des événements.');
        setLoading(false);
      }
    };

    fetchEvents();
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

  const handleReservation = async (eventId) => {
    if (!user) {
      alert('Veuillez vous connecter pour réserver un événement.');
      return;
    }

    const event = events.find((e) => e.id === eventId);

    if (seatsReserved > event.seatsAvailable) {
      alert(`Il n'y a pas suffisamment de places disponibles. Il n'en reste que ${event.seatsAvailable}.`);
      return;
    }

    const reservationData = {
      eventId,
      userId: user.id,
      seatsReserved,
      reservationDate: new Date().toISOString(),
      status: 'pending',
    };

    try {
      await axios.post('http://localhost:8089/reservation', reservationData, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });

      navigate('/my-reservations', {
        state: { eventName: event.name, eventId, userId: user.id, username: user.username, email: user.email },
      });
    } catch (error) {
      alert('Une erreur est survenue lors de la réservation.');
    }
  };

  if (loading) return <div className="text-center mt-5">Chargement des événements...</div>;
  if (error) return <div className="alert alert-danger">{error}</div>;

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
        Liste des Événements
      </div>

      {/* Event list */}
      {events.length === 0 ? (
        <p className="text-center text-muted">Aucun événement disponible pour le moment.</p>
      ) : (
        <table className="table table-striped table-hover table-bordered">
          <thead className="table-primary">
            <tr>
              <th>Nom</th>
              <th>Date</th>
              <th>Description</th>
              <th>Lieu</th>
              <th>Prix (€)</th>
              <th>Places disponibles</th>
              <th>Réserver</th>
            </tr>
          </thead>
          <tbody>
            {events.map((event) => (
              <tr key={event.id}>
                <td>{event.name}</td>
                <td>{new Date(event.date).toLocaleDateString()}</td>
                <td>{event.description}</td>
                <td>{event.location}</td>
                <td>{event.price}</td>
                <td>{event.seatsAvailable}</td>
                <td>
                  <div className="d-flex align-items-center">
                    <input
                      type="number"
                      className="form-control me-2"
                      value={seatsReserved}
                      onChange={(e) => setSeatsReserved(Math.max(1, e.target.value))}
                      min="1"
                      max={event.seatsAvailable}
                      style={{ width: '80px' }}
                    />
                    <button
                      onClick={() => handleReservation(event.id)}
                      className="btn btn-primary shadow px-sm-4"
                      style={{
                        background: '#7e00ff',
                        color: '#fff',
                        padding: '12px 24px',
                        border: 'none',
                        borderRadius: '8px',
                        cursor: 'pointer',
                        transition: 'background 0.3s ease',
                      }}
                      onMouseOver={(e) => (e.target.style.background = '#ff00ff')}
                      onMouseOut={(e) => (e.target.style.background = '#7e00ff')}
                    >
                      Réserver
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default Home;
