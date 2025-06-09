import React from 'react';
import './AdminDashboard.css'; // Crearemos este archivo para los estilos

function AdminDashboard({ dashboardData }) {
    // Si no hay datos, no mostramos nada o un mensaje de carga.
    if (!dashboardData) {
        return <div>Cargando datos del dashboard...</div>;
    }

    return (
        <div className="admin-dashboard">
            <header>
                <h1>Bienvenido, Admin</h1>
            </header>
            <section className="cards">
                <div className="card">
                    <h3>Total Cuentas</h3>
                    <p>{dashboardData.total}</p>
                </div>
                <div className="card">
                    <h3>Cuentas BÁSICO</h3>
                    <p>{dashboardData.basico}</p>
                </div>
                <div className="card">
                    <h3>Cuentas INTERMEDIO</h3>
                    <p>{dashboardData.intermedio}</p>
                </div>
                <div className="card">
                    <h3>Cuentas PREMIUM</h3>
                    <p>{dashboardData.premium}</p>
                </div>
            </section>
        </div>
    );
}

export default AdminDashboard;