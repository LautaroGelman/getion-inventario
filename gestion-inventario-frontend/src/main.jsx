import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import App from './App.jsx';
import './index.css';

// ──────────────── DEBUG ENV ────────────────
// Solo muestra el valor cuando estás en modo dev
if (import.meta.env.DEV) {
    console.log(
        'VITE_API_BASE_URL (main.jsx):',
        import.meta.env.VITE_API_BASE_URL || '(vacío, usa proxy)'
    );
}

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <BrowserRouter>
            <App />
        </BrowserRouter>
    </React.StrictMode>
);
