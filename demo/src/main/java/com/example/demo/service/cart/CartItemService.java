package com.example.demo.service.cart;

import com.example.demo.exeptions.ResourceNotFoundException;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repo.CartItemRepo;
import com.example.demo.repo.CartRepo;
import com.example.demo.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepo cartItemRepo;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepo cartRepo;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        try {
            // find the Cart to add item to
            Cart cart = cartService.getCart(cartId);

            // find the Product to add to CartItem
            Product product = productService.getProductById(productId);
            // find the item
            CartItem cartItem = getCartItem(cartId, productId);

            // if no item exists in the cart, add this to the cart
            if (cartItem.getId() == null) {
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(product.getPrice());
            }
            // if item already exists, only update the quantity
            else {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            }
            cartItem.setTotalPrice();
            cart.addItem(cartItem);
            cartItemRepo.save(cartItem);
            cartRepo.save(cart);
        } catch (Exception e) {
            String err = e.getMessage();
            System.out.println(err);
        }

    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        try {
            Cart cart = cartService.getCart(cartId);

            return cart.getCartItems().stream().filter(item ->
                            item.getProduct().getId().equals(productId))
                    .findFirst().orElse(new CartItem());

        } catch (Exception e) {
            String err = e.getMessage();
            System.out.println(err);
            return null;
        }


    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);
        cartRepo.save(cart);

    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });

        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepo.save(cart);
    }

}
