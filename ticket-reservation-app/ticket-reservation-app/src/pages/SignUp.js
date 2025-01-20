import React, { useState } from 'react';
import { register } from './API'; // Importer la fonction d'inscription de l'API
import { Link, useNavigate } from 'react-router-dom'; // Navigation hook

const SignUp = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await register({ username, password, email });
  
      navigate('/login');
    } catch (err) {
      setError('Une erreur est survenue lors de l\'inscription.');
    }
  };

  return (
    <section
      style={{
        background: 'rgba(31, 31, 47, 0.1)',
        minHeight: '100vh',
        color: '#fff',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'flex-start',
        fontFamily: 'Arial, sans-serif',
        padding: '20px',
        paddingTop: '100px',
      }}
    >
      <div className="auth-wrapper" style={{ maxWidth: '400px', margin: '0 auto' }}>
        <div
          className="auth-form"
          style={{
            backgroundColor: 'rgba(31, 31, 47, 0.2)',
            borderRadius: '8px',
            padding: '20px',
            boxShadow: '0 4px 10px rgba(0, 0, 0, 0.5)',
          }}
        >
          <div className="text-center">
            <div
              style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                marginBottom: '20px',
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
                }}
              >
                EventMaster
              </h1>
            </div>
          </div>
          <h4
            style={{
              fontSize: '2rem',
              color: '#fff',
              marginBottom: '20px',
              background: 'linear-gradient(90deg, #ff00ff, #7e00ff)',
              WebkitBackgroundClip: 'text',
              WebkitTextFillColor: 'transparent',
            }}
          >
           
          </h4>
          <form onSubmit={handleSubmit}>
            <div className="form-group mb-3">
              <input
                type="text"
                className="form-control"
                placeholder="Nom d'utilisateur"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                style={{
                  padding: '10px',
                  borderRadius: '6px',
                  border: 'none',
                  backgroundColor: 'rgba(31, 31, 47, 0.1)',
                  color: '#fff',
                }}
              />
            </div>
            <div className="form-group mb-3">
              <input
                type="email"
                className="form-control"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                style={{
                  padding: '10px',
                  borderRadius: '6px',
                  border: 'none',
                  backgroundColor: 'rgba(31, 31, 47, 0.1)',
                  color: '#fff',
                }}
              />
            </div>
            <div className="form-group mb-3">
              <input
                type="password"
                className="form-control"
                placeholder="Mot de passe"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                style={{
                  padding: '10px',
                  borderRadius: '6px',
                  border: 'none',
                  backgroundColor:'rgba(31, 31, 47, 0.1)',
                  color: '#fff',
                }}
              />
            </div>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <div className="text-center mt-4">
              <button
                type="submit"
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
                onMouseOver={(e) =>
                  (e.target.style.background = '#ff00ff')
                }
                onMouseOut={(e) =>
                  (e.target.style.background = '#7e00ff')
                }
              >
               S'inscrire 
              </button>
            </div>
          </form>
          <div className="d-flex justify-content-between align-items-end mt-4">
            <h6 className="f-w-500 mb-0" style={{ color: '#b0b3b8' }}>
            Vous avez déjà un compte ?






            </h6>
            <Link
              to="/login"
              style={{
                color: '#7e00ff',
                textDecoration: 'none',
                fontWeight: 'bold',
              }}
            >
              Se connecter
            </Link>
          </div>
        </div>
      </div>
    </section>
  );
};

export default SignUp;
