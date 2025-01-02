package by.shplau.services;

import by.shplau.entities.Order;
import by.shplau.entities.util.CartItem;
import by.shplau.exceptions.EmailSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EmailService {

    private final JavaMailSender mailSender;
    private final String sellerMail;

    @Autowired
    public EmailService(JavaMailSender mailSender,
                        @Value("${mail.seller_mail}") String sellerMail) {
        this.mailSender = mailSender;
        this.sellerMail = sellerMail;
    }

    public void sendToCustomer(Order order) {
        String to = order.getCart().getUser().getEmail();
        String subject = "Уведомление о заказе №" + order.getId();
        String body = getOrderDetails(order);

        sendEmail(to, subject, body);
    }

    public void sendToSeller(Order order) {
        String subject = "Новый заказ №" + order.getId();
        String body = getOrderDetails(order);

        sendEmail(sellerMail, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true означает, что это HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendException("Ошибка при отправке email на адрес: " + to, e);
        }
    }

    public String getOrderDetails(Order order) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html>")
                .append("<body style='font-family: Arial, sans-serif; line-height: 1.6;'>")
                .append("<h2 style='color: #4CAF50;'>Order Details</h2>")
                .append("<p><strong>Order ID:</strong> ").append(order.getId()).append("</p>")
                .append("<p><strong>Order Date:</strong> ").append(order.getOrderDate()).append("</p>")
                .append("<p><strong>Delivery Date:</strong> ").append(order.getReservationDate()).append("</p>")
                .append("<p><strong>Status:</strong> ").append(order.getStatus()).append("</p>")
                .append("<h3 style='color: #2196F3;'>Items:</h3>")
                .append("<table style='border-collapse: collapse; width: 100%;'>")
                .append("<thead>")
                .append("<tr style='background-color: #f2f2f2;'>")
                .append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Product Name</th>")
                .append("<th style='border: 1px solid #ddd; padding: 8px; text-align: center;'>Quantity</th>")
                .append("<th style='border: 1px solid #ddd; padding: 8px; text-align: right;'>Price</th>")
                .append("</tr>")
                .append("</thead>")
                .append("<tbody>");

        for (CartItem item : order.getCart().getItems()) {
            htmlBuilder.append("<tr>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(item.getProduct().getName()).append("</td>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px; text-align: center;'>").append(item.getQuantity()).append("</td>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px; text-align: right;'>")
                    .append(String.format("$%.2f", item.getProduct().getPrice())).append("</td>")
                    .append("</tr>");
        }

        htmlBuilder.append("</tbody>")
                .append("</table>")
                .append("<p><strong>Total Price:</strong> ").append(String.format("$%.2f", order.getCart().getTotalPrice())).append("</p>")
                .append("</body>")
                .append("</html>");

        return htmlBuilder.toString();
    }
}
