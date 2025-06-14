import React, { useState } from 'react';
import './CashSessionModal.css';

function CashSessionModal({ mode, onOpen, onClose, onCancel, expectedAmount }) {
    const [amount, setAmount] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        if (mode === 'open') {
            onOpen(amount);
        } else {
            onClose(amount);
        }
        setAmount('');
    };

    const title = mode === 'open' ? 'Abrir Caja' : 'Cerrar Caja';
    const label = mode === 'open' ? 'Monto Inicial:' : 'Monto Final Contado:';
    const buttonText = mode === 'open' ? 'Confirmar Apertura' : 'Confirmar Cierre';

    return (
        <div className="modal-backdrop">
            <div className="modal-content">
                <h2>{title}</h2>
                <form onSubmit={handleSubmit}>
                    {mode === 'close' && (
                        <p className="expected-amount">Monto esperado según ventas: <strong>${expectedAmount?.toFixed(2) || '0.00'}</strong></p>
                    )}
                    <label htmlFor="amount">{label}</label>
                    <input
                        type="number"
                        id="amount"
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                        placeholder="0.00"
                        step="0.01"
                        required
                    />
                    <div className="modal-actions">
                        {/* El botón de cancelar solo se muestra si el modo no es 'open' (forzado) */}
                        {mode !== 'open' && <button type="button" className="btn-cancel" onClick={onCancel}>Cancelar</button>}
                        <button type="submit" className="btn-confirm">{buttonText}</button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default CashSessionModal;