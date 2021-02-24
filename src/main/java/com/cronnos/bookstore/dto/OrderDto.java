package com.cronnos.bookstore.dto;

import com.cronnos.bookstore.entities.Order;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private BigDecimal price;

    public OrderDto(Order o) {
        this.id = o.getId();
        this.price = o.getPrice();
    }
}