import React, { useState } from 'react'; 
import { login } from './API'; // Your login API call
import { Link, useNavigate } from 'react-router-dom'; // Navigation hook
import { jwtDecode } from 'jwt-decode'; // Corrected import for jwtDecode

const Connection = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await login({ username, password });

      if (response.data.token) {
        const { token } = response.data;
        const decodedUser = jwtDecode(token);
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(decodedUser));

        if (username === 'admin' && password === '123456') {
          
          navigate('/admin');
        } else {
         
          navigate('/home');
        }
      } else {
        setError('No token in response');
      }
    } catch (err) {
      setError('Incorrect username or password.');
      console.error('Login error:', err);
    }
  };

  return (
    <section
      style={{
        background: 'rgba(31, 31, 47, 0.1)', // Modified background to white
        minHeight: '100vh',
        color: '#000', // Changed text color to black for better contrast
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'flex-start',
        fontFamily: 'Arial, sans-serif',
        padding: '20px',
        marginTop: '0px',
        paddingTop: '100px',
      }}
    >
      <div
        className="auth-wrapper"
        style={{ maxWidth: '400px', margin: '0 auto' }}
      >
        <div
          className="auth-form"
          style={{
            backgroundColor: 'rgba(31, 31, 47, 0.2)', // Transparent gray background
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
            <h4
              style={{
                fontSize: '2rem',
                color: '#fff',
                marginBottom: '20px',
                background: 'linear-gradient(90deg, #ff00ff, #7e00ff)',
                WebkitBackgroundClip: 'text',
                WebkitTextFillColor: 'transparent',
              }}
            ></h4>
          </div>
          <form onSubmit={handleSubmit}>
            <div className="form-group mb-3">
              <input
                type="text"
                className="form-control"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                style={{
                  padding: '10px',
                  borderRadius: '6px',
                  border: 'none',
                  backgroundColor:  'rgba(31, 31, 47, 0.1)', // Dark gray background
                  color: '#fff', // White text color
                  fontSize: '1rem', // Increased font size for readability
                }}
              />
            </div>
            <div className="form-group mb-3">
              <input
                type="password"
                className="form-control"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                style={{
                  padding: '10px',
                  borderRadius: '6px',
                  border: 'none',
                  backgroundColor: 'rgba(31, 31, 47, 0.1)', // Dark gray background
                  color: '#fff', // White text color
                  fontSize: '1rem', // Increased font size for readability
                }}
              />
            </div>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <div className="d-flex mt-1 justify-content-between align-items-center">
              <div className="form-check">
                <input
                  className="form-check-input"
                  type="checkbox"
                  id="customCheckc1"
                />
                <label
                  className="form-check-label text-muted"
                  htmlFor="customCheckc1"
                >
                 Tu te souviens de moi ?
                </label>
              </div>
              <h6
                className="text-secondary mb-0"
                style={{ color: '#b0b3b8', cursor: 'pointer' }}
              >
               
              </h6>
            </div>
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
                Se Connecter 
              </button>
            </div>
          </form>
          <div className="d-flex justify-content-between align-items-end mt-4">
            <h6 className="f-w-500 mb-0" style={{ color: '#b0b3b8' }}>
            Vous n'avez pas de compte ?
            </h6>
            <Link
              to="/register"
              style={{
                color: '#7e00ff',
                textDecoration: 'none',
                fontWeight: 'bold',
              }}
            >
              S'inscrire
            </Link>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Connection;
