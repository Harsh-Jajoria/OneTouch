package com.axepert.onetouch.ui.ecommerce.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.ProductByCarRequest;
import com.axepert.onetouch.responses.ProductByCatResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductByCategoryViewModel extends ViewModel {

    private ApiService apiService;

    public ProductByCategoryViewModel() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<ProductByCatResponse> productByCat(String catId) {
        MutableLiveData<ProductByCatResponse> data = new MutableLiveData<>();
        ProductByCarRequest productByCarRequest = new ProductByCarRequest(
                "onetouch.com",
                "",
                "",
                catId
        );

        apiService.productByCategory(productByCarRequest).enqueue(new Callback<ProductByCatResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductByCatResponse> call, @NonNull Response<ProductByCatResponse> response) {
                data.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ProductByCatResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

}
