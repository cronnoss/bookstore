package com.cronnos.bookstore.controllers;

import com.cronnos.bookstore.beans.Cart;
import com.cronnos.bookstore.dto.OrderDto;
import com.cronnos.bookstore.entities.Order;
import com.cronnos.bookstore.entities.User;
import com.cronnos.bookstore.exceptions.AccessDeniedException;
import com.cronnos.bookstore.exceptions.ResourceNotFoundException;
import com.cronnos.bookstore.services.OrderService;
import com.cronnos.bookstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final UserService userService;
    private final OrderService orderService;
    private final Cart cart;

    @GetMapping("/{id}")
    public OrderDto getOrder(@PathVariable Long id, Principal principal) {
        Order order = orderService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Unable to find order with id: " + id));
        if (!order.getUser().getUsername().equals(principal.getName())) {
            throw new AccessDeniedException(String.format("User %s do not have access to another user's order", principal.getName()), principal);
        }
        return new OrderDto(order);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createNewOrder(Principal principal, @RequestParam String address) {
        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new ResourceNotFoundException("Unable to create order for user: " + principal.getName() + ". User doesn't exist"));
        Order order = new Order(user, cart, address);
        order = orderService.saveOrder(order);
        cart.clear();
        return new OrderDto(order);
    }

    @GetMapping
    public List<OrderDto> getOrders(Principal principal) {
        return orderService.findAllByUsername(principal.getName()).stream().map(OrderDto::new).collect(Collectors.toList());
    }
}
