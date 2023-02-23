package com.axepert.onetouch.ui.cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.AndroidViewModel;

import com.axepert.onetouch.database.CartDatabase;
import com.axepert.onetouch.models.CartModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class CartViewModel extends AndroidViewModel {

    private CartDatabase cartDatabase;

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartDatabase = CartDatabase.getCartDatabase(application);
    }

    public Flowable<List<CartModel>> loadCartList() {
        return cartDatabase.cartDao().getCart();
    }

    public Completable removeFromCart(CartModel cartModel) {
        return cartDatabase.cartDao().removeFromCart(cartModel);
    }

    public Completable productQuantity(CartModel cartModel) {
        return cartDatabase.cartDao().updateProductQuantity(cartModel);
    }

}
