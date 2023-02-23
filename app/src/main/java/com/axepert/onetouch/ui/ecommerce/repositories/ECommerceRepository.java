package com.axepert.onetouch.ui.ecommerce.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.responses.ECommHomePageResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ECommerceRepository {
    private final ApiService apiService;

    public ECommerceRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public MutableLiveData<ECommHomePageResponse> ecommerceHome() {
        MutableLiveData<ECommHomePageResponse> data = new MutableLiveData<>();
        apiService.getEcommerceHomePage().enqueue(new Callback<ECommHomePageResponse>() {
            @Override
            public void onResponse(@NonNull Call<ECommHomePageResponse> call, @NonNull Response<ECommHomePageResponse> response) {
                data.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ECommHomePageResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

}
