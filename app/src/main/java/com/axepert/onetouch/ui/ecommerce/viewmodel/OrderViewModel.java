package com.axepert.onetouch.ui.ecommerce.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.AddressListRequest;
import com.axepert.onetouch.responses.OrdersResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewModel extends ViewModel {

    private ApiService apiService;

    public OrderViewModel() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<OrdersResponse> orders(String userId) {
        MutableLiveData<OrdersResponse> data = new MutableLiveData<>();
        AddressListRequest addressListRequest = new AddressListRequest(userId, "onetouch.com");
        apiService.fetchOrders(addressListRequest).enqueue(new Callback<OrdersResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrdersResponse> call, @NonNull Response<OrdersResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrdersResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

}
