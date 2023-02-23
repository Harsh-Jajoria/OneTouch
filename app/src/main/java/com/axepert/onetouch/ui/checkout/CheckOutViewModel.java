package com.axepert.onetouch.ui.checkout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.axepert.onetouch.database.CartDatabase;
import com.axepert.onetouch.models.CartModel;
import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.OrderCodRequest;
import com.axepert.onetouch.requests.OrderOnlineRequest;
import com.axepert.onetouch.responses.PlaceOrderResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutViewModel extends AndroidViewModel {

    private CartDatabase cartDatabase;
    private ApiService apiService;

    public CheckOutViewModel(@NonNull Application application) {
        super(application);
        cartDatabase = CartDatabase.getCartDatabase(application);
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public Flowable<List<CartModel>> loadCartList() {
        return cartDatabase.cartDao().getCart();
    }

    public Completable deleteAll() {
        return cartDatabase.cartDao().deleteAll();
    }

    public LiveData<PlaceOrderResponse> orderCod(OrderCodRequest codRequest) {
        MutableLiveData<PlaceOrderResponse> data = new MutableLiveData<>();
        apiService.orderCod(codRequest).enqueue(new Callback<PlaceOrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceOrderResponse> call, @NonNull Response<PlaceOrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaceOrderResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<PlaceOrderResponse> orderOnline(OrderOnlineRequest onlineRequest) {
        MutableLiveData<PlaceOrderResponse> data = new MutableLiveData<>();
        apiService.orderOnline(onlineRequest).enqueue(new Callback<PlaceOrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceOrderResponse> call, @NonNull Response<PlaceOrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaceOrderResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

}
