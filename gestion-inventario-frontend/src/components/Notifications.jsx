import React, { useState, useEffect } from 'react';
import { api } from '../../services/api';
import './Notifications.css';

const BellIcon = () => (
    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path>
        <path d="M13.73 21a2 2 0 0 1-3.46 0"></path>
    </svg>
);


function Notifications() {
    const [alerts, setAlerts] = useState([]);
    const [isOpen, setIsOpen] = useState(false);

    useEffect(() => {
        const fetchAlerts = async () => {
            try {
                const response = await api.get('/api/alerts/unread');
                setAlerts(response.data);
            } catch (error) {
                console.error("Error fetching alerts:", error);
            }
        };
        fetchAlerts();
    }, []);

    const handleMarkAsRead = async (alertId) => {
        try {
            await api.post(`/api/alerts/${alertId}/read`);
            setAlerts(prevAlerts => prevAlerts.filter(alert => alert.id !== alertId));
        } catch (error) {
            console.error("Error marking alert as read:", error);
        }
    };

    return (
        <div className="notifications-container">
            <button className="notifications-bell" onClick={() => setIsOpen(!isOpen)}>
                <BellIcon />
                {alerts.length > 0 && <span className="notifications-badge">{alerts.length}</span>}
            </button>

            {isOpen && (
                <div className="notifications-dropdown">
                    {alerts.length === 0 ? (
                        <div className="no-alerts">No hay notificaciones nuevas.</div>
                    ) : (
                        <ul>
                            {alerts.map(alert => (
                                <li key={alert.id}>
                                    <p>{alert.message}</p>
                                    <small>{new Date(alert.createdAt).toLocaleString()}</small>
                                    <button onClick={() => handleMarkAsRead(alert.id)}>Marcar como leído</button>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            )}
        </div>
    );
}

export default Notifications;