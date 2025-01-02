package by.shplau.services;

import by.shplau.entities.Cart;
import by.shplau.entities.Product;
import by.shplau.entities.User;
import by.shplau.entities.util.CartItem;
import by.shplau.repositories.CartRepository;
import by.shplau.repositories.ProductRepository;
import by.shplau.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Cart getCartForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user)
                .map(cart -> {
                    // Инициализируем коллекции
                    cart.getItems().size();
                    cart.getItems().forEach(item -> item.getProduct().getName());
                    return cart;
                })
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public Cart addItemToCart(String username, Long productId, int quantity) {
        Cart cart = getCartForUser(username);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .cart(cart)
                    .build();
            cart.getItems().add(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        // Инициализируем все связанные сущности перед возвратом
        savedCart.getItems().forEach(item -> item.getProduct().getName());
        return savedCart;
    }

    @Transactional
    public Cart removeItemFromCart(String username, Long productId) {
        Cart cart = getCartForUser(username);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));

        Cart savedCart = cartRepository.save(cart);
        // Инициализируем все связанные сущности перед возвратом
        savedCart.getItems().forEach(item -> item.getProduct().getName());
        return savedCart;
    }

    @Transactional
    public void clearCart(String username) {
        Cart cart = getCartForUser(username);
        cart.getItems().clear();
        Cart savedCart = cartRepository.save(cart);
        // Инициализируем все связанные сущности (если требуется) перед завершением метода
        savedCart.getItems().forEach(item -> item.getProduct().getName());
    }
}
