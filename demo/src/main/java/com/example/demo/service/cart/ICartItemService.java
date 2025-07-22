package com.example.demo.service.cart;

import com.example.demo.model.CartItem;

public interface ICartItemService {
    void addItemToCart(Long cardId, Long productId, int quantity);

    void removeItemFromCart(Long cardId, Long productId);

    void updateItemQuantity(Long cardId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);
}
