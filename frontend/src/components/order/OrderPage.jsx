import React, { useState } from 'react';
import './OrderPage.css';

const OrderPage = ({ cart, onClose }) => {
    const [name, setName] = useState('');
    const [phone, setPhone] = useState('');
    const [comment, setComment] = useState('');
    const [route, setRoute] = useState('Standard Delivery');
    const [reservationDate, setReservationDate] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState(null);

    const handleOrderSubmit = async () => {
        try {
            setIsSubmitting(true);
            setError(null);

            const orderRequest = {
                name,
                phone,
                comment,
                route,
                orderDate: new Date(),
                reservationDate: new Date(reservationDate)
            };

            const response = await fetch('/orders', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: JSON.stringify(orderRequest)
            });

            if (response.status === 401) {
                throw new Error('Please log in to submit an order');
            }

            if (!response.ok) {
                throw new Error('Failed to submit order. Please try again.');
            }

            const data = await response.json();
            console.log('Order submitted successfully:', data);

            setName('');
            setPhone('');
            setComment('');
            setReservationDate('');
            
            onClose();
            alert('Order submitted successfully! Check your email for confirmation.');

        } catch (err) {
            console.error('Error submitting order:', err);
            setError(err.message);
        } finally {
            setIsSubmitting(false);
        }
    };

    // Вычисляем минимальную дату (сегодня)
    const today = new Date().toISOString().split('T')[0];

    const isFormValid = () => {
        return name.trim() && phone.trim() && reservationDate;
    };

    return (
        <div className="order-page">
            <button className="close-button" onClick={onClose}>×</button>
            <h2>Order Details</h2>
            <ul>
                {cart.items.map((item) => (
                    <li key={item.product.id}>
                        <div><strong>Name:</strong> {item.product.name}</div>
                        <div><strong>Description:</strong> {item.product.description}</div>
                        <div><strong>Price:</strong> ${item.product.price}</div>
                        <div><strong>Quantity:</strong> {item.quantity}</div>
                    </li>
                ))}
            </ul>
            <h3>Route</h3>
            <p>{route}</p>
            <h3>Fill Your Details</h3>
            <form onSubmit={(e) => e.preventDefault()}>
                <label>
                    Name:
                    <input 
                        type="text" 
                        value={name} 
                        onChange={(e) => setName(e.target.value)} 
                        required 
                        disabled={isSubmitting}
                    />
                </label>
                <label>
                    Phone:
                    <input 
                        type="tel" 
                        value={phone} 
                        onChange={(e) => setPhone(e.target.value)} 
                        required 
                        disabled={isSubmitting}
                    />
                </label>
                <label>
                    Delivery Date:
                    <input 
                        type="date"
                        value={reservationDate}
                        onChange={(e) => setReservationDate(e.target.value)}
                        min={today}
                        required
                        disabled={isSubmitting}
                    />
                </label>
                <label>
                    Comment:
                    <textarea 
                        value={comment} 
                        onChange={(e) => setComment(e.target.value)}
                        disabled={isSubmitting}
                    />
                </label>
                <button 
                    type="button" 
                    onClick={handleOrderSubmit}
                    disabled={isSubmitting || !isFormValid()}
                >
                    {isSubmitting ? 'Submitting...' : 'Submit Order'}
                </button>
            </form>
            <button onClick={onClose}>Back to Cart</button>
        </div>
    );
};

export default OrderPage;
