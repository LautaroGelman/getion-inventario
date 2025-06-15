import React, { useState, useEffect } from 'react';
import { api } from '../services/api';
import './SaleFormPage.css';
import { useNavigate } from 'react-router-dom';

function SaleFormPage() {
    const [products, setProducts] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [cart, setCart] = useState([]); // <-- El estado para nuestro carrito
    const [customerName, setCustomerName] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await api.get('/products');
                setProducts(response.data);
            } catch (error) {
                console.error('Error fetching products:', error);
            }
        };
        fetchProducts();
    }, []);

    // Lógica para manejar el carrito
    const handleAddToCart = (product) => {
        setCart(prevCart => {
            const existingProduct = prevCart.find(item => item.id === product.id);
            if (existingProduct) {
                // Si el producto ya está en el carrito, incrementa la cantidad
                return prevCart.map(item =>
                    item.id === product.id ? { ...item, cantidad: item.cantidad + 1 } : item
                );
            } else {
                // Si es un producto nuevo, lo añade al carrito con cantidad 1
                return [...prevCart, { ...product, cantidad: 1 }];
            }
        });
    };

    const handleUpdateQuantity = (productId, newQuantity) => {
        const quantity = parseInt(newQuantity, 10);
        if (quantity <= 0) {
            // Si la cantidad es 0 o menos, elimina el producto del carrito
            setCart(prevCart => prevCart.filter(item => item.id !== productId));
        } else {
            setCart(prevCart =>
                prevCart.map(item =>
                    item.id === productId ? { ...item, cantidad: quantity } : item
                )
            );
        }
    };

    const calculateSubtotal = () => {
        return cart.reduce((total, item) => total + item.precio * item.cantidad, 0).toFixed(2);
    };

    // Lógica para enviar la venta
    const handleSubmit = async (event) => {
        event.preventDefault();
        if (cart.length === 0) {
            alert('El carrito está vacío.');
            return;
        }

        const saleData = {
            cliente: customerName || 'Consumidor Final',
            items: cart.map(item => ({
                productId: item.id,
                cantidad: item.cantidad
            }))
        };

        try {
            await api.post('/sales', saleData);
            alert('Venta registrada con éxito');
            navigate('/client-panel'); // Redirige después de la venta
        } catch (error) {
            console.error('Error creating sale:', error);
            alert('Error al registrar la venta.');
        }
    };

    const filteredProducts = products.filter(p =>
        p.nombre.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="sale-form-container">
            <h1>Punto de Venta</h1>
            <div className="pos-layout">
                {/* Columna de la izquierda: Lista de productos */}
                <div className="product-list-section">
                    <h2>Productos</h2>
                    <input
                        type="text"
                        placeholder="Buscar producto..."
                        className="search-input"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                    <div className="product-list">
                        {filteredProducts.map(product => (
                            <div key={product.id} className="product-item" onClick={() => handleAddToCart(product)}>
                                <span>{product.nombre}</span>
                                <span>${product.precio.toFixed(2)}</span>
                            </div>
                        ))}
                    </div>
                </div>

                {/* Columna de la derecha: Carrito de compras */}
                <div className="cart-section">
                    <h2>Carrito</h2>
                    <form onSubmit={handleSubmit}>
                        <div className="customer-input-container">
                            <label htmlFor="customerName">Nombre del Cliente (Opcional):</label>
                            <input
                                type="text"
                                id="customerName"
                                value={customerName}
                                onChange={(e) => setCustomerName(e.target.value)}
                                className="customer-input"
                            />
                        </div>

                        <div className="cart-items">
                            {cart.length === 0 ? (
                                <p>El carrito está vacío.</p>
                            ) : (
                                cart.map(item => (
                                    <div key={item.id} className="cart-item">
                                        <span className="item-name">{item.nombre}</span>
                                        <input
                                            type="number"
                                            min="0"
                                            value={item.cantidad}
                                            onChange={(e) => handleUpdateQuantity(item.id, e.target.value)}
                                            className="item-quantity"
                                        />
                                        <span className="item-price">${(item.precio * item.cantidad).toFixed(2)}</span>
                                    </div>
                                ))
                            )}
                        </div>
                        <div className="cart-summary">
                            <h3>Subtotal: ${calculateSubtotal()}</h3>
                            <button type="submit" className="submit-button">Registrar Venta</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default SaleFormPage;