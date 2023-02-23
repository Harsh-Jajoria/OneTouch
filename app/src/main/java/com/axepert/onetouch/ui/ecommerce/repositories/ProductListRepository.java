package com.axepert.onetouch.ui.ecommerce.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.ProductListRequest;
import com.axepert.onetouch.responses.ProductListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListRepository {

    private ApiService apiService;

    public ProductListRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public MutableLiveData<ProductListResponse> allProducts(ProductListRequest productListRequest) {
        MutableLiveData<ProductListResponse> data = new MutableLiveData<>();
        apiService.productList(productListRequest).enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductListResponse> call, @NonNull Response<ProductListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductListResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

}
