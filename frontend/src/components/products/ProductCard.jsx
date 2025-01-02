import React, { useState } from 'react';
import { Card, CardContent, CardMedia, Typography, Button, Box } from '@mui/material';
import cartService from '../../services/cartService';
import { API_BASE_URL } from '../../services/api';

const ProductCard = ({ product, onCartUpdate }) => {
  const [imageError, setImageError] = useState(false);
  const placeholderImage = `${API_BASE_URL}/img/default/product.jpg`;

  const handleImageError = () => {
    setImageError(true);
  };

  const handleAddToCart = async () => {
    try {
      const updatedCart = await cartService.addToCart(product.id, 1);
      onCartUpdate?.(updatedCart); // оповещаем родителя об обновлении корзины
      // Добавим уведомление об успехе
      alert('Product added to cart successfully');
    } catch (error) {
      console.error('Error adding to cart:', error);
      // Добавим уведомление об ошибке
      alert(error.response?.data?.message || 'Error adding to cart');
    }
  };

  return (
    <Card>
      <CardMedia
        component="img"
        height="200"
        image={imageError ? placeholderImage : `${API_BASE_URL}${product.imageURL}`}
        onError={handleImageError}
        alt={product.name}
      />
      <CardContent>
        <Typography gutterBottom variant="h5" component="div">
          {product.name}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          {product.description}
        </Typography>
        <Box sx={{ mt: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="h6">
            ${product.price}
          </Typography>
          <Button variant="contained" color="primary" onClick={handleAddToCart}>
            Add to Cart
          </Button>
        </Box>
      </CardContent>
    </Card>
  );
};

export default ProductCard;