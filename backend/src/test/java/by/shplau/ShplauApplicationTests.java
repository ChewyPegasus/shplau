package by.shplau;

import by.shplau.entities.Cart;
import by.shplau.entities.Order;
import by.shplau.entities.User;
import by.shplau.entities.util.OrderStatus;
import by.shplau.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ShplauApplication.class)
class ShplauApplicationTests {

	@Test
	void contextLoads() {
	}

//	@Autowired
//	private MockMvc mvc;
//
//	@Test
//	public void getHello() throws Exception {
//		mvc.perform(MockMvcRequestBuilders.get("/")
//						.accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content().string(equalTo("Hello, World!")));
//
//	}

	@MockBean
	private JavaMailSender javaMailSender;

	@Autowired
	private EmailService emailService;

	@Test
	void testSendEmail() throws MessagingException {
		// Подготовка тестовых данных
		User user = User.builder()
				.email("test@example.com")
				.build();

		Cart cart = Cart.builder()
				.user(user)
				.build();

		Order order = Order.builder()
				.cart(cart)
				.orderDate(LocalDateTime.now())
				.reservationDate(LocalDateTime.now().plusDays(1))
				.status(OrderStatus.PENDING)
				.build();

		// Настройка мока
		MimeMessage mimeMessage = mock(MimeMessage.class);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		doNothing().when(javaMailSender).send(any(MimeMessage.class));

		// Проверка
		assertDoesNotThrow(() -> emailService.sendToCustomer(order));

		// Проверяем, что метод send был вызван
		verify(javaMailSender, times(1)).send(any(MimeMessage.class));
	}
}
