// Asegúrate de incluir <script src="charts.js" defer></script> en tu HTML

document.addEventListener("DOMContentLoaded", () => {
    // ── Gráfico de ingresos por día (30 días) ──
    const ingresos30Dias = {
        labels: [
            "Día 1", "Día 2", "Día 3", "Día 4", "Día 5", "Día 6", "Día 7",
            "Día 8", "Día 9", "Día 10", "Día 11", "Día 12", "Día 13", "Día 14",
            "Día 15", "Día 16", "Día 17", "Día 18", "Día 19", "Día 20",
            "Día 21", "Día 22", "Día 23", "Día 24", "Día 25", "Día 26",
            "Día 27", "Día 28", "Día 29", "Día 30"
        ],
        datasets: [{
            label: "Ingresos ($)",
            data: [
                150, 200, 180, 220, 170, 210, 230, 190, 200, 210,
                195, 225, 240, 250, 260, 275, 280, 300, 310, 290,
                320, 330, 310, 300, 280, 270, 260, 250, 240, 230
            ],
            backgroundColor: "rgba(4, 128, 163, 0.7)",
            borderColor: "rgba(4, 128, 163, 1)",
            borderWidth: 1,
            fill: true,
            tension: 0.3 // suaviza la curva en line chart
        }]
    };

    const ctxRevenue = document.getElementById("revenueChart").getContext("2d");

    new Chart(ctxRevenue, {
        type: "line", // podés cambiar a 'bar' si preferís barras
        data: ingresos30Dias,
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        // Formateo opcional, para mostrar el signo $
                        callback: function(value) {
                            return "$" + value;
                        }
                    }
                }
            }
        }
    });
});

document.addEventListener("DOMContentLoaded", () => {
    // ── Gráfico de ingresos por tipo de cuenta ──
    const ctx = document.getElementById("adminChart").getContext("2d");

    new Chart(ctx, {
        type: "bar",
        data: {
            labels: ["FREE", "STANDARD", "PREMIUM"],
            datasets: [{
                label: "Ingresos por tipo de cuenta ($)",
                data: [5000, 12000, 25000],
                backgroundColor: ["#6c757d", "#17a2b8", "#ffc107"]
            }]
        },
        options: {
            responsive: true,
            scales: { y: { beginAtZero: true } }
        }
    });
});
