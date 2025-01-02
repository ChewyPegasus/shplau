import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Header from './components/layout/Header';
import ProductList from './components/products/ProductList';
import RouteList from './components/routes/RouteList';
import CartPage from './components/cart/CartPage';
import Login from './components/users/Login';
import Register from './components/users/Register';
import Profile from './components/users/Profile';
import PrivateRoute from './components/PrivateRoute';

function App() {
  return (
    <Router>
      <Header />
      <Routes>
        <Route path="/" element={<Navigate to="/products" />} />
        <Route path="/routes" element={<RouteList />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route
          path="/profile"
          element={
            <PrivateRoute>
              <Profile />
            </PrivateRoute>
          }
        />
        <Route
          path="/cart"
          element={
            <PrivateRoute>
              <CartPage />
            </PrivateRoute>
          }
        />
        <Route path="/products" element={<ProductList />} />
      </Routes>
    </Router>
  );
}

export default App;