package by.shplau.dto.requests;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderRequest {
    private String name;
    private String phone;
    private String comment;
    private String route;
    private LocalDateTime orderDate;
    private LocalDateTime reservationDate;
}
