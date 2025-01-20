import 'bootstrap/dist/css/bootstrap.min.css';
import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { jsPDF } from "jspdf";
import axios from 'axios';
import { FaUserCircle } from 'react-icons/fa';
import html2canvas from 'html2canvas'; // Import de html2canvas pour capturer la carte

const PaymentSuccess = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const sessionId = new URLSearchParams(location.search).get('session_id');
  const reservationId = new URLSearchParams(location.search).get('reservationId');
  const amount = new URLSearchParams(location.search).get('amount');
  const email = new URLSearchParams(location.search).get('email');
  const eventId = new URLSearchParams(location.search).get('eventId');

  const [eventDetails, setEventDetails] = useState(null);
  const [userDetails, setUserDetails] = useState(null);
  const [statusUpdated, setStatusUpdated] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const getEventDetails = async () => {
    try {
      const response = await axios.get(`http://localhost:8087/events/${eventId}`);
      setEventDetails(response.data);
    } catch (error) {
      setErrorMessage('Erreur lors de la récupération des détails de l\'événement.');
    }
  };

  const getUserDetailsByEmail = async () => {
    try {
      const response = await axios.get(`http://localhost:8095/auth/user/${email}`);
      setUserDetails(response.data);
    } catch (error) {
      setErrorMessage('Erreur lors de la récupération des informations de l\'utilisateur.');
    }
  };

  const updateReservationStatus = async () => {
    try {
      await axios.put(
        `http://localhost:8089/reservation/update-status/${reservationId}`,
        null,
        { params: { status: 'CONFIRMED' } }
      );
      setStatusUpdated(true);
      await sendEmailNotification(email, reservationId);
    } catch (error) {
      setErrorMessage('Une erreur est survenue lors de la mise à jour du statut de la réservation.');
    }
  };

  const sendEmailNotification = async (toEmail, reservationId) => {
    try {
      await axios.post('http://localhost:8097/send-email', null, {
        params: { toEmail, reservationId },
      });
    } catch (error) {
      console.error('Erreur lors de l\'envoi de l\'email :', error);
    }
  };

  const generateReceipt = () => {
    const doc = new jsPDF();
    doc.setFont("helvetica", "normal");
    doc.text("Reçu de paiement", 14, 10);
    doc.text(`Nom de l'utilisateur: ${userDetails ? userDetails.username : 'N/A'}`, 14, 20);
    doc.text(`Email: ${userDetails ? userDetails.email : 'N/A'}`, 14, 30);
    doc.text(`ID Réservation: ${reservationId}`, 14, 40);
    doc.text(`ID Événement: ${eventId}`, 14, 50);
    doc.text(`Nom de l'événement: ${eventDetails ? eventDetails.name : 'N/A'}`, 14, 60);
    doc.text(`Date de l'événement: ${eventDetails ? new Date(eventDetails.date).toLocaleDateString() : 'N/A'}`, 14, 70);
    doc.text(`Montant payé: ${amount} €`, 14, 80);
    doc.save("reçu-paiement.pdf");
  };

  const downloadCard = async () => {
    const cardElement = document.querySelector('.card'); // Sélectionnez la carte par sa classe
    if (cardElement) {
      try {
        const canvas = await html2canvas(cardElement);
        const image = canvas.toDataURL('image/png'); // Convertir en image PNG

        // Créer un lien pour télécharger l'image
        const link = document.createElement('a');
        link.href = image;
        link.download = 'ticket-evenement.png';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } catch (error) {
        console.error('Erreur lors de la capture de la carte :', error);
      }
    }
  };

  useEffect(() => {
    if (reservationId) {
      updateReservationStatus();
    }
    getEventDetails();
    getUserDetailsByEmail();
  }, [reservationId, eventId, email]);

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
      {/* Titre principal au-dessus de la carte */}
      <h3
        style={{
          color: '#ff00ff',
          textAlign: 'center',
          fontWeight: 'bold',
          marginBottom: '20px',
        }}
      >
        Votre ticket pour l'événement
      </h3>

      {/* Carte avec les informations */}
      <div
        className="card mx-auto"
        style={{
          width: '50%',
          padding: '20px',
          borderRadius: '15px',
          boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
          backgroundColor: '#7e00ff',
          color: '#fff',
        }}
      >
        <div className="mb-3">
          <strong
            style={{
              fontSize: '1.5rem',
              fontWeight: 'bold',
              textShadow: '1px 1px 4px rgba(0, 0, 0, 0.5)',
            }}
          >
            {eventDetails ? eventDetails.name : 'N/A'}
          </strong>
        </div>
        <div className="mb-3">
          <strong>Date de l'événement:</strong> {eventDetails ? new Date(eventDetails.date).toLocaleDateString() : 'N/A'}
        </div>
        <div className="mb-3">
          <strong>Lieu:</strong> {eventDetails ? eventDetails.location : 'N/A'}
        </div>
        <div className="mb-3">
          <strong>Description:</strong> {eventDetails ? eventDetails.description : 'N/A'}
        </div>
        <div className="mb-3">
          <strong>Montant payé:</strong> {amount} €
        </div>
      </div>

      {/* Boutons Télécharger en bas */}
      <div className="text-center mt-4">
       
        <button
          onClick={downloadCard}
          className="btn btn-secondary shadow ms-3 px-sm-4"
          style={{
            background: 'rgba(31, 31, 47, 0.4)',
            color: '#fff',
            padding: '12px 24px',
            border: 'none',
            borderRadius: '8px',
            cursor: 'pointer',
            transition: 'background 0.3s ease',
          }}
         
        >
          Télécharger le ticket
        </button>
      </div>
    </div>
  );
};

export default PaymentSuccess;
