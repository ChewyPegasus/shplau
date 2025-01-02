import api from './api';

const cartService = {
    getCart: async () => {
        try {
            const response = await api.get('/cart');
            return response.data;
        } catch (error) {
            if (error.response?.status === 401) {
                throw new Error('Please login to view cart');
            }
            throw error;
        }
    },

    addToCart: async (productId, quantity) => {
        try {
            const response = await api.post('/cart/add', { productId, quantity });
            return response.data;
        } catch (error) {
            if (error.response?.status === 401) {
                throw new Error('Please login to add to cart');
            }
            throw error;
        }
    },

    removeFromCart: async (productId) => {
        try {
            const response = await api.delete(`/cart/remove/${productId}`);
            return response.data;
        } catch (error) {
            if (error.response?.status === 401) {
                throw new Error('Please login to add to cart');
            }
            throw error;
        }
        
    },

    clearCart: async () => {
        try {
            await api.delete('/cart/clear');
        } catch (error) {
            if (error.response?.status === 401) {
                throw new Error('Please login to add to cart');
            }
            throw error;
        }
    }
};

export default cartService;
