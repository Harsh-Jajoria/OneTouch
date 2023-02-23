package com.axepert.onetouch.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.axepert.onetouch.models.CartModel;
import com.axepert.onetouch.responses.TrendingProduct;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import retrofit2.http.DELETE;

@Dao
public interface CartDao {

    @Query("SELECT * FROM cartProduct")
    Flowable<List<CartModel>> getCart();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addToCart(CartModel cartModel);

    @Delete
    Completable removeFromCart(CartModel cartModel);

    @Query("SELECT * FROM cartProduct WHERE id = :productId")
    Flowable<CartModel> getProductFromCart(String productId);

    @Update
    Completable updateProductQuantity(CartModel cartModel);

    @Query("DELETE FROM cartProduct")
    Completable deleteAll();
}
