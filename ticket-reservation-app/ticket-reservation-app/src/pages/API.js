import axios from 'axios';
import { jwtDecode } from 'jwt-decode'; // Utiliser l'importation correcte
import { loadStripe } from '@stripe/stripe-js';



// Fonction pour confirmer la réservation
export const confirmReservation = async (reservationId) => {
  const token = localStorage.getItem('token');
  await axios.post(`http://localhost:8089/reservations/${reservationId}/confirm`, {}, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};
 
// Définir l'URL de base de vos APIs
const API_BASE_URL = 'http://localhost:8890';  // API des événements
const API_BASE_URL1 = 'http://localhost:8095'; // API d'authentification
const API_BASE_URL3 = 'http://localhost:8089'; // API des réservations

// Fonction pour récupérer l'ID de l'utilisateur depuis le token JWT
export const getUserIdFromToken = () => {
  const token = localStorage.getItem('token');
  if (token) {
    const decodedToken = jwtDecode(token); // Décoder le token avec jwtDecode
    return decodedToken.id; // Le `sub` est l'ID de l'utilisateur
  }
  return null;
};

// Fonction pour récupérer la liste des événements
export const getEvents = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/events`);
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des événements', error);
    throw error;
  }
};

// Fonction pour enregistrer un nouvel utilisateur (inscription)
export const register = async (userData) => {
  try {
    const response = await axios.post(`${API_BASE_URL1}/auth/register`, userData);
    return response.data;
  } catch (error) {
    console.error('Erreur lors de l\'inscription', error);
    throw error;
  }
};

// Fonction de connexion
export const login = async (credentials) => {
  try {
    const response = await axios.post(`${API_BASE_URL1}/auth/login`, credentials);
    const token = response.data;

    if (token) {
      localStorage.setItem('token', token);
      console.log("Token reçu:", token);
    } else {
      console.error("Token non trouvé dans la réponse.");
    }

    return response;
  } catch (error) {
    console.error('Erreur lors de la connexion', error);
    throw error;
  }
};

export const reserveEvent = async (eventId) => {
  const token = localStorage.getItem('token');

  if (!token) {
    throw new Error('Utilisateur non connecté');
  }

  const userId = getUserIdFromToken(); // Récupérer l'ID de l'utilisateur depuis le token

  if (!userId) {
    throw new Error('ID de l\'utilisateur non trouvé');
  }

  const reservationData = {
    event_id: eventId, // Si l'API attend event_id plutôt que eventId
    user_id: userId,   // Si l'API attend user_id plutôt que userId
  };

  console.log("Données envoyées à l'API:", reservationData); // Log pour voir les données envoyées

  try {
    const response = await axios.post(
      `${API_BASE_URL3}/reservation`, // L'URL de l'API pour la réservation
      reservationData,
      {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json', // Indiquer que nous envoyons des données JSON
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la réservation', error.response ? error.response.data : error);
    throw error;
  }
};

// Fonction pour obtenir les réservations de l'utilisateur
export const getUserReservations = async () => {
  const token = localStorage.getItem('token');
  const userId = getUserIdFromToken();  // Récupérer l'ID de l'utilisateur depuis le token

  if (!userId) {
    throw new Error('ID de l\'utilisateur non trouvé');
  }

  try {
    const response = await axios.get(`http://localhost:8089/reservation/user/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des réservations de l\'utilisateur', error.response ? error.response.data : error);
    throw error;
  }
};



// Clé publique Stripe (depuis votre dashboard Stripe)
const stripePromise = loadStripe('pk_test_51QVfdtKycUC9MuJltcoIIPpv4TnlMYLeXXPS66pJjjyKglqwuZu4hFRaLVQ2aYdjd5hzGw25l6nZJDkIrpDExjIM00wsVUbR42');

export const initiatePayment = async (reservationId, amount) => {
  try {
    const response = await axios.post('http://localhost:8095/api/payments/create-checkout-session', {
      reservationId,
      amount,
    });

    const stripe = await stripePromise;
    if (response.data) {
      await stripe.redirectToCheckout({ sessionId: response.data });
    }
  } catch (error) {
    console.error('Erreur lors de l\'initialisation du paiement', error);
    throw error;
  }
};
