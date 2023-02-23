package com.axepert.onetouch.listeners;

import com.axepert.onetouch.models.CartModel;

public interface CartListener {
    void onProductClicked(CartModel cartModel);

    void removeFromCart(CartModel cartModel, int position);

    void increaseQuantity(CartModel cartModel);

    void decreaseQuantity(CartModel cartModel);
}
