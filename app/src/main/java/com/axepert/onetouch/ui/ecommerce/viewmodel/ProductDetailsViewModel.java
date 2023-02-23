package com.axepert.onetouch.ui.ecommerce.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.axepert.onetouch.database.CartDatabase;
import com.axepert.onetouch.models.CartModel;
import com.axepert.onetouch.requests.RelatedProductRequest;
import com.axepert.onetouch.responses.ProductDetailsResponse;
import com.axepert.onetouch.responses.RelatedProductResponse;
import com.axepert.onetouch.responses.TrendingProduct;
import com.axepert.onetouch.ui.ecommerce.repositories.ProductDetailsRepository;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class ProductDetailsViewModel extends AndroidViewModel {

    private final ProductDetailsRepository productDetailsRepository;
    private CartDatabase cartDatabase;

    public ProductDetailsViewModel(Application application) {
        super(application);
        productDetailsRepository = new ProductDetailsRepository(application.getApplicationContext());
        cartDatabase = CartDatabase.getCartDatabase(application);
    }

    public LiveData<ProductDetailsResponse> getProductDetails(String slug) {
        return productDetailsRepository.productDetails(slug);
    }

    public Completable addToCart(CartModel cartModel) {
        return cartDatabase.cartDao().addToCart(cartModel);
    }

    public Flowable<CartModel> getProductFromCart(String productId) {
        return cartDatabase.cartDao().getProductFromCart(productId);
    }

    public Completable removeFromCart(CartModel cartModel) {
        return cartDatabase.cartDao().removeFromCart(cartModel);
    }

    public LiveData<RelatedProductResponse> getRelatedProduct(RelatedProductRequest relatedProductRequest) {
        return productDetailsRepository.getRelatedProduct(relatedProductRequest);
    }

}
