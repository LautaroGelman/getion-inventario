const express = require("express");
const mysql = require("mysql2");
const cors = require("cors");

const app = express();
app.use(cors());
app.use(express.json());

// Configurar conexión con tu base de datos
const db = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "root", // cambiá por la contraseña real si es distinta
  database: "gestionbasedatos"
});

// Probar conexión
app.get("/", (req, res) => {
  res.send("Conexión exitosa a la base de datos gestionbasedatos.");
});

// Ruta para obtener todos los productos
app.get("/productos", (req, res) => {
  const sql = "SELECT * FROM product";
  db.query(sql, (err, result) => {
    if (err) return res.status(500).json({ error: err });
    res.json(result);
  });
});

// Ruta para insertar un producto
app.post("/productos", (req, res) => {
  const { name, description, price, stock_quantity, client_id } = req.body;
  const sql = "INSERT INTO product (name, description, price, stock_quantity, client_id) VALUES (?, ?, ?, ?, ?)";
  db.query(sql, [name, description, price, stock_quantity, client_id], (err, result) => {
    if (err) return res.status(500).json({ error: err });
    res.status(201).json({ message: "Producto creado con éxito", id: result.insertId });
  });
});

// Iniciar servidor
app.listen(3000, () => {
  console.log("Servidor corriendo en http://localhost:3000");
});
