import React, { useState, useEffect } from 'react';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import { FaEdit, FaTrash, FaPlus, FaUserCircle } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';

const AdminDashboard = () => {
  const [events, setEvents] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [selectedEventId, setSelectedEventId] = useState(null);
  const [alertMessage, setAlertMessage] = useState('');
  const [showAlertModal, setShowAlertModal] = useState(false);
  const [formData, setFormData] = useState({
    id: null,
    name: '',
    date: '',
    location: '',
    seatsAvailable: 0,
    description: '',
  });
  const [user, setUser] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      const user = JSON.parse(userData);
      setUser(user);
    }

    fetchEvents();
  }, []);

  const fetchEvents = async () => {
    try {
      const response = await axios.get('http://localhost:8087/events');
      setEvents(response.data);
    } catch (error) {
      console.error('Erreur lors du chargement des événements:', error);
    }
  };

  const handleDelete = async () => {
    try {
      await axios.delete(`http://localhost:8087/events/${selectedEventId}`);
      setAlertMessage('Événement supprimé avec succès.');
      setShowAlertModal(true);
      fetchEvents();
    } catch (error) {
      console.error('Erreur lors de la suppression:', error);
    } finally {
      setShowDeleteModal(false);
    }
  };

  const handleFormOpen = (event = null) => {
    if (event) {
      setFormData({
        id: event.id,
        name: event.name,
        date: event.date,
        location: event.location,
        seatsAvailable: event.seatsAvailable,
        description: event.description,
      });
    } else {
      setFormData({
        id: null,
        name: '',
        date: '',
        location: '',
        seatsAvailable: 0,
        description: '',
      });
    }
    setShowForm(true);
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    try {
      if (formData.id) {
        await axios.put(`http://localhost:8087/events/${formData.id}`, formData);
        setAlertMessage('Événement modifié avec succès.');
      } else {
        await axios.post('http://localhost:8087/events', formData);
        setAlertMessage('Événement ajouté avec succès.');
      }
      fetchEvents();
      setShowForm(false);
      setShowAlertModal(true);
    } catch (error) {
      console.error('Erreur lors de l\'enregistrement:', error);
    }
  };

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
        <div className="position-relative">
          <FaUserCircle size={40} style={{ cursor: 'pointer', color: '#7e00ff' }} />
        </div>
      </header>
      {/* Titre */}
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
        Gestion des Événements
        <span
          className="text-primary"
          style={{
            cursor: 'pointer',
            fontSize: '1.8rem',
            display: 'flex',
            alignItems: 'center',
            gap: '0.5rem',
          }}
          onClick={() => handleFormOpen()}
          title="Ajouter un Nouvel Événement"
        >
          <FaPlus />
        </span>
      </div>

      {/* Tableau des événements */}
      <table className="table table-striped table-hover table-bordered">
        <thead className="table-primary">
          <tr>
            <th>Nom</th>
            <th>Date</th>
            <th>Lieu</th>
            <th>Places Disponibles</th>
            <th>Description</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {events.map((event) => (
            <tr key={event.id}>
              <td>{event.name}</td>
              <td>{new Date(event.date).toLocaleDateString()}</td>
              <td>{event.location}</td>
              <td>{event.seatsAvailable}</td>
              <td>{event.description}</td>
           <td>
  <FaEdit
    className="me-3"
    style={{ cursor: 'pointer', fontSize: '1.2rem', color: '#7e00ff' }}
    onClick={() => handleFormOpen(event)}
    title="Modifier"
  />
  <FaTrash
    style={{ cursor: 'pointer', fontSize: '1.2rem', color: '#7e00ff' }}
    onClick={() => {
      setSelectedEventId(event.id);
      setShowDeleteModal(true);
    }}
    title="Supprimer"
  />
</td>

            </tr>
          ))}
        </tbody>
      </table>


      {/* Formulaire d'ajout/modification */}
      {showForm && (
        <div className="modal d-block" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
          <div className="modal-dialog modal-dialog-centered" style={{ maxWidth: '500px' }}>
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">
                  {formData.id ? "Modifier l'Événement" : 'Ajouter un Nouvel Événement'}
                </h5>
              </div>
              <div className="modal-body">
                <form onSubmit={handleFormSubmit}>
                  <div className="mb-3">
                    <label className="form-label">Nom :</label>
                    <input
                      type="text"
                      className="form-control"
                      value={formData.name}
                      onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Date :</label>
                    <input
                      type="datetime-local"
                      className="form-control"
                      value={formData.date}
                      onChange={(e) => setFormData({ ...formData, date: e.target.value })}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Lieu :</label>
                    <input
                      type="text"
                      className="form-control"
                      value={formData.location}
                      onChange={(e) => setFormData({ ...formData, location: e.target.value })}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Places Disponibles :</label>
                    <input
                      type="number"
                      className="form-control"
                      value={formData.seatsAvailable}
                      onChange={(e) => setFormData({ ...formData, seatsAvailable: parseInt(e.target.value) })}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Description :</label>
                    <textarea
                      className="form-control"
                      value={formData.description}
                      onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                      required
                    />
                  </div>
                  <div className="d-flex justify-content-end">
                    <button type="submit" className="btn btn-primary me-2">Enregistrer</button>
                    <button
                      type="button"
                      className="btn btn-secondary"
                      onClick={() => setShowForm(false)}
                    >
                      Annuler
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      )}
     {/* Modal de confirmation de suppression */}
      {showDeleteModal && (
        <div className="modal d-block" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
          <div className="modal-dialog modal-dialog-centered">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Confirmer la suppression</h5>
              </div>
              <div className="modal-body">
                <p>Voulez-vous vraiment supprimer cet événement ?</p>
              </div>
              <div className="modal-footer">
                <button className="btn btn-danger" onClick={handleDelete}>Oui</button>
                <button className="btn btn-secondary" onClick={() => setShowDeleteModal(false)}>Annuler</button>
              </div>
            </div>
          </div>
        </div>
      )}
      {/* Modal d'alerte */}
      {showAlertModal && (
        <div className="modal d-block" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
          <div className="modal-dialog modal-dialog-centered" style={{ maxWidth: '400px' }}>
            <div className="modal-content">
              <div className="modal-body">
                <p>{alertMessage}</p>
              </div>
              <div className="modal-footer">
                <button className="btn btn-primary" onClick={() => setShowAlertModal(false)}>OK</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
