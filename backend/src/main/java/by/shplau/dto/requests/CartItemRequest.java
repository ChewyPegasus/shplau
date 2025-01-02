package by.shplau.dto.requests;

import lombok.Data;

@Data
public class CartItemRequest {

    private Long productId;
    private int quantity;
}
