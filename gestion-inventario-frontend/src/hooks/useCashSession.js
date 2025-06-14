import { useState, useEffect, useCallback } from 'react';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';

export function useCashSession() {
    const { user } = useAuth();
    const [session, setSession] = useState(null);
    const [isModalOpen, setModalOpen] = useState(false);
    const [modalMode, setModalMode] = useState('open'); // 'open' or 'close'

    const isCashier = user?.role === 'CAJERO' || user?.role === 'MULTIFUNCION';

    const checkSession = useCallback(async () => {
        if (!isCashier) return;
        try {
            const response = await api.get('/api/cash-sessions/current');
            if (response.status === 200 && response.data) {
                setSession(response.data);
            } else {
                setSession(null);
                setModalMode('open');
                setModalOpen(true); // Forzar apertura de caja si no hay sesión
            }
        } catch (error) {
            console.error("Error checking cash session:", error);
        }
    }, [isCashier]);

    useEffect(() => {
        checkSession();
    }, [checkSession]);

    const handleOpenSession = async (initialAmount) => {
        try {
            const response = await api.post('/api/cash-sessions/open', { initialAmount });
            setSession(response.data);
            setModalOpen(false);
        } catch (error) {
            console.error("Error opening session:", error);
            alert("No se pudo abrir la caja.");
        }
    };

    const handleCloseSession = async (countedAmount) => {
        try {
            const response = await api.post('/api/cash-sessions/close', { countedAmount });
            const closedSession = response.data;
            alert(`Caja cerrada. Diferencia: $${closedSession.difference.toFixed(2)}`);
            setSession(null);
            setModalOpen(false);
        } catch (error) {
            console.error("Error closing session:", error);
            alert("No se pudo cerrar la caja.");
        }
    };

    const showCloseModal = () => {
        setModalMode('close');
        setModalOpen(true);
    };

    return {
        session,
        isModalOpen,
        modalMode,
        handleOpenSession,
        handleCloseSession,
        showCloseModal,
        setModalOpen,
    };
}