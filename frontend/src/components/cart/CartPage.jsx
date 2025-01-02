import React, { useEffect, useState } from 'react';
import cartService from '../../services/cartService';
import './CartPage.css';
import OrderPage from '../order/OrderPage';

const CartPage = () => {
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showOrderPage, setShowOrderPage] = useState(false);

    useEffect(() => {
        loadCart();
    }, []);

    const loadCart = async () => {
        try {
            setLoading(true);
            const cartData = await cartService.getCart();
            setCart(cartData);
            setError(null);
        } catch (err) {
            setError('Failed to load cart');
            console.error('Error loading cart:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleRemove = async (productId) => {
        try {
            const updatedCart = await cartService.removeFromCart(productId);
            setCart(updatedCart);
        } catch (err) {
            alert('Failed to remove item from cart');
            console.error('Error removing item:', err);
        }
    };

    const handleClearCart = async () => {
        try {
            await cartService.clearCart();
            setCart({ items: [] });
        } catch (err) {
            alert('Failed to clear cart');
            console.error('Error clearing cart:', err);
        }
    };

    const toggleOrderPage = () => {
        setShowOrderPage(!showOrderPage);
    };

    if (loading) return <div>Loading cart...</div>;
    if (error) return <div>Error: {error}</div>;
    if (!cart) return <div>No cart found</div>;

    return (
        <div className="cart-container">
            <h1>Your Cart</h1>
            {cart.items.length === 0 ? (
                <p>Your cart is empty.</p>
            ) : (
                <>
                    <ul>
                        {cart.items.map((item) => (
                            <li key={item.product.id}>
                                {item.product.name} - ${item.product.price} x {item.quantity}
                                <button onClick={() => handleRemove(item.product.id)}>Remove</button>
                            </li>
                        ))}
                    </ul>
                    <h2>Total: ${cart.items.reduce((sum, item) => sum + item.product.price * item.quantity, 0).toFixed(2)}</h2>
                    <button onClick={handleClearCart}>Clear Cart</button>
                    <button onClick={toggleOrderPage} className="order-button">Proceed to Order</button>
                </>
            )}
            {showOrderPage && (
                <div className="order-modal" onClick={toggleOrderPage}>
                    <div className="order-modal-content" onClick={(e) => e.stopPropagation()}>
                        <OrderPage cart={cart} onClose={toggleOrderPage} />
                    </div>
                </div>
            )}
        </div>
    );
};

export default CartPage;
