package by.shplau;

import by.shplau.controllers.OrderController;
import by.shplau.dto.requests.OrderRequest;
import by.shplau.entities.Cart;
import by.shplau.entities.Order;
import by.shplau.entities.User;
import by.shplau.repositories.ProductRepository;
import by.shplau.repositories.RouteRepository;
import by.shplau.repositories.UserRepository;
import by.shplau.services.EmailService;
import by.shplau.services.OrderService;
import by.shplau.services.security.JwtService;
import by.shplau.services.security.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private RouteRepository routeRepository;

    @MockBean
    private UserRepository userRepository;

    private OrderRequest testOrderRequest;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrderRequest = new OrderRequest();
        testOrderRequest.setName("Test Name");
        testOrderRequest.setPhone("+1234567890");
        testOrderRequest.setComment("Test Comment");
        testOrderRequest.setRoute("Standard Delivery");
        testOrderRequest.setOrderDate(LocalDateTime.now());
        testOrderRequest.setReservationDate(LocalDateTime.now().plusDays(2));

        User testUser = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .build();

        Cart testCart = Cart.builder()
                .id(1L)
                .user(testUser)
                .build();

        testOrder = Order.builder()
                .id(1L)
                .orderDate(testOrderRequest.getOrderDate())
                .reservationDate(testOrderRequest.getReservationDate())
                .comment(testOrderRequest.getComment())
                .cart(testCart)
                .build();
    }

    @Test
    @WithMockUser(username = "testUser")
    void createOrder_Success() throws Exception {
        when(orderService.createOrder(eq("testUser"), any(OrderRequest.class)))
                .thenReturn(testOrder);
        doNothing().when(emailService).sendToCustomer(any(Order.class));
        doNothing().when(emailService).sendToSeller(any(Order.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.comment").value(testOrderRequest.getComment()));

        verify(orderService).createOrder(eq("testUser"), any(OrderRequest.class));
        verify(emailService).sendToCustomer(any(Order.class));
        verify(emailService).sendToSeller(any(Order.class));
    }

    @Test
    void createOrder_Unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrderRequest)))
                .andExpect(status().isUnauthorized());

        verify(orderService, never()).createOrder(any(), any());
        verify(emailService, never()).sendToCustomer(any());
        verify(emailService, never()).sendToSeller(any());
    }

    @Test
    @WithMockUser(username = "testUser")
    void createOrder_ServiceError() throws Exception {
        when(orderService.createOrder(any(), any()))
                .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrderRequest)))
                .andExpect(status().isInternalServerError());
    }
}