import React from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'; // Remplacer Switch par Routes
import Connection from './pages/connection';
import Home from './pages/Home';
import SignUp from './pages/SignUp'; // Assurez-vous que ce composant existe
import MyReservations from './pages/MyReservations';
import ConfirmReservation from'./pages/ConfirmReservation';
import PaymentSuccess from './pages/PaymentSuccess'; 
import AdminDashboard from './pages/AdminDashboard'; // Composant pour l'interface admin

function App() {
  return (
    <div className="App">
      <Router>
        <Routes> {/* Utilisez Routes au lieu de Switch */}
      
          <Route path="/home" element={<Home />} /> {/* Page d'accueil */}
          <Route path="/login" element={<Connection />} /> {/* Page de connexion */}
          <Route path="/register" element={<SignUp />} /> {/* Page d'inscription */}
          <Route path="/my-reservations" element={<MyReservations />} />
          <Route path="/ConfirmReservation" element={<ConfirmReservation />} /> {/* Route pour le paiement */}
          <Route path="/success" element={<PaymentSuccess />} />
          <Route path="/admin" element={<AdminDashboard />} /> {/* Route pour l'admin */}
        </Routes>
      </Router>
    </div>
  );
}

export default App;
