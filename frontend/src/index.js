import React from 'react';
import './index.css';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import App from './App';
import reportWebVitals from './reportWebVitals';
import NewToast from './sharedComponents/NewToast';


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <div className="App">
    <BrowserRouter>
      <App />
      <NewToast />
    </BrowserRouter>
    </div>
  </React.StrictMode>
);

reportWebVitals();
