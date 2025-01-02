package by.shplau;

import by.shplau.dto.requests.OrderRequest;
import by.shplau.services.CartService;
import by.shplau.entities.Cart;
import by.shplau.entities.Order;
import by.shplau.entities.User;
import by.shplau.repositories.OrderRepository;
import by.shplau.repositories.UserRepository;
import by.shplau.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderService orderService;

    private User testUser;
    private Cart testCart;
    private OrderRequest testOrderRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .build();

        testCart = Cart.builder()
                .id(1L)
                .user(testUser)
                .build();

        testOrderRequest = new OrderRequest();
        testOrderRequest.setName("Test Name");
        testOrderRequest.setPhone("+1234567890");
        testOrderRequest.setComment("Test Comment");
        testOrderRequest.setRoute("Standard Delivery");
        testOrderRequest.setOrderDate(LocalDateTime.now());
        testOrderRequest.setReservationDate(LocalDateTime.now().plusDays(2));
    }

    @Test
    void createOrder_Success() {
        // Arrange
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(cartService.getCartForUser("testUser")).thenReturn(testCart);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Order result = orderService.createOrder("testUser", testOrderRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testOrderRequest.getComment(), result.getComment());
        assertEquals(testOrderRequest.getOrderDate(), result.getOrderDate());
        assertEquals(testOrderRequest.getReservationDate(), result.getReservationDate());
        assertEquals(testCart, result.getCart());

        verify(orderRepository).save(any(Order.class));
        verify(userRepository).findByUsername("testUser");
        verify(cartService).getCartForUser("testUser");
    }

    @Test
    void createOrder_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername("nonexistentUser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                orderService.createOrder("nonexistentUser", testOrderRequest)
        );

        verify(orderRepository, never()).save(any(Order.class));
    }
}
