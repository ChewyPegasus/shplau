package by.shplau.services;

import by.shplau.dto.requests.OrderRequest;
import by.shplau.entities.Cart;
import by.shplau.entities.Order;
import by.shplau.entities.User;
import by.shplau.repositories.OrderRepository;
import by.shplau.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    public Order createOrder(String username, OrderRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("User not found"));

        Cart cart = cartService.getCartForUser(username);

        Order order = Order.builder()
                .orderDate(request.getOrderDate())
                .reservationDate(request.getReservationDate())
                .comment(request.getComment())
                .cart(cart)
                .build();

        return orderRepository.save(order);
    }
}
