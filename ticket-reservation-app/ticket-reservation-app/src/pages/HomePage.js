import React from "react";
import { useNavigate } from "react-router-dom";

const HomePage = () => {
  const navigate = useNavigate();

  const handleLoginRedirect = () => {
    navigate("/login");
  };

  return (
    <div
      style={{
        background: 'rgba(31, 31, 47, 0.1)',
        minHeight: "100vh",
        color: "#fff",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "flex-start",
        fontFamily: "Arial, sans-serif",
        textAlign: "center",
        padding: "20px",
      }}
    >
      {/* Logo et Nom de l'application */}
      <header style={{ width: "100%", padding: "20px" }}>
        <div style={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
          <img
            src="https://i.vimeocdn.com/portrait/43505883_640x640" // Remplacez par l'URL de votre logo
            alt="Logo EventMaster"
            style={{ width: "30px", marginRight: "10px" }}
          />
          <h1
            style={{
              fontSize: "2rem",
              fontWeight: "bold",
              background: "linear-gradient(90deg, #ff00ff, #7e00ff)",
              WebkitBackgroundClip: "text",
              WebkitTextFillColor: "transparent",
              marginBottom: "00px",
            }}
          >
            EventMaster
          </h1>
        </div>
      </header>

      {/* Image de présentation réduite */}
      <div style={{ width: "100%", padding: "30px 0", display: "flex", justifyContent: "center" }}>
        <img
          src="https://kineticlighting.com/wp-content/uploads/2020/04/Tent-Event-Lighting-LAPromise-1024x683-1.jpg" // Remplacez par une vraie URL d'image
          alt="Bannière de présentation"
          style={{
            width: "60%", // Réduction de la largeur
            maxHeight: "350px", // Hauteur réduite
            objectFit: "cover",
            borderRadius: "8px", // Arrondi des coins de l'image
          }}
        />
      </div>

      {/* Texte d'introduction */}
      <div style={{ maxWidth: "800px", margin: "0 auto", marginBottom: "40px" }}>
        <p
          style={{
            fontSize: "1.5rem",
            color: "#fff",
            marginBottom: "20px",
          }}
        >
          Bienvenue sur EventMaster, la plateforme ultime pour découvrir et réserver des billets pour les événements les plus excitants autour de vous.
        </p>
        <p
          style={{
            fontSize: "1.2rem",
            color: "#b0b3b8",
            marginBottom: "30px",
          }}
        >
          Que vous soyez passionné de musique, de théâtre, ou de conférences, nous avons une large gamme d'événements pour tous les goûts. Réservez vos billets en toute simplicité et vivez des expériences inoubliables.
        </p>
      </div>

      {/* Galerie d'événements */}
      <div
        style={{
          display: "flex",
          flexWrap: "wrap",
          justifyContent: "center",
          gap: "20px",
          marginBottom: "50px",
        }}
      >
        {[...Array(3)].map((_, index) => (
          <div
            key={index}
            style={{
              background: "#1e1e2f",
              borderRadius: "12px",
              overflow: "hidden",
              width: "300px",
              boxShadow: "0px 4px 10px rgba(0, 0, 0, 0.5)",
              transition: "transform 0.3s ease, box-shadow 0.3s ease",
              cursor: "pointer",
            }}
            onMouseOver={(e) => {
              e.currentTarget.style.transform = "scale(1.05)";
              e.currentTarget.style.boxShadow =
                "0px 6px 15px rgba(126, 0, 255, 0.6)";
            }}
            onMouseOut={(e) => {
              e.currentTarget.style.transform = "scale(1)";
              e.currentTarget.style.boxShadow =
                "0px 4px 10px rgba(0, 0, 0, 0.5)";
            }}
          >
            <img
              src="https://kineticlighting.com/wp-content/uploads/2020/04/Tent-Event-Lighting-LAPromise-1024x683-1.jpg" // Remplacez par l'image de votre événement
              alt="Événement"
              style={{ width: "100%", height: "180px", objectFit: "cover" }}
            />
            <div style={{ padding: "15px" }}>
              <h3 style={{ color: "#ff79c6", margin: "10px 0", fontSize: "1.3rem" }}>
                Nom de l'événement
              </h3>
              <p style={{ color: "#b0b3b8", fontSize: "1rem", marginBottom: "10px" }}>
                Description de l'événement.
              </p>
              <button
                onClick={handleLoginRedirect}
                style={{
                  background: "#7e00ff",
                  color: "#fff",
                  border: "none",
                  padding: "8px 16px",
                  borderRadius: "6px",
                  cursor: "pointer",
                  transition: "background 0.3s ease",
                }}
                onMouseOver={(e) => (e.target.style.background = "#ff00ff")}
                onMouseOut={(e) => (e.target.style.background = "#7e00ff")}
              >
                Réserver Maintenant
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* Bouton d'action principal */}
      <div style={{ marginTop: "40px" }}>
        <button
          onClick={handleLoginRedirect}
          style={{
            background: "linear-gradient(90deg, #7e00ff, #ff00ff)",
            color: "#fff",
            padding: "12px 24px",
            border: "none",
            fontSize: "1.2rem",
            borderRadius: "8px",
            cursor: "pointer",
            transition: "transform 0.3s ease",
          }}
          onMouseOver={(e) => (e.target.style.transform = "scale(1.1)")}
          onMouseOut={(e) => (e.target.style.transform = "scale(1)")}
        >
          Voir Tous Les Événements
        </button>
      </div>

      {/* Footer */}
      <footer
        style={{
          background: "#1e1e2f",
          color: "#b0b3b8",
          padding: "20px",
          width: "100%",
          textAlign: "center",
          marginTop: "auto",
        }}
      >
        <p>© 2024 EventMaster | Contactez-nous : support@eventmaster.com</p>
      </footer>
    </div>
  );
};

export default HomePage;
