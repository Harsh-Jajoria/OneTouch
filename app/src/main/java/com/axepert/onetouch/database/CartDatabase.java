package com.axepert.onetouch.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.axepert.onetouch.dao.CartDao;
import com.axepert.onetouch.models.CartModel;
import com.axepert.onetouch.responses.TrendingProduct;

@Database(entities = CartModel.class, version = 1, exportSchema = false)
public abstract class CartDatabase extends RoomDatabase {

    private static CartDatabase cartDatabase;

    public static synchronized CartDatabase getCartDatabase(Context context) {
        if (cartDatabase == null) {
            cartDatabase = Room.databaseBuilder(
                    context,
                    CartDatabase.class,
                    "cart_db"
            ).build();
        }
        return cartDatabase;
    }

    public abstract CartDao cartDao();

}
