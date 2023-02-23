package com.axepert.onetouch.ui.ecommerce.repositories;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.ProductDetailsRequest;
import com.axepert.onetouch.requests.RelatedProductRequest;
import com.axepert.onetouch.responses.ProductDetailsResponse;
import com.axepert.onetouch.responses.RelatedProductResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsRepository {

    private ApiService apiService;
    private Context context;

    public ProductDetailsRepository(Context context) {
        this.context = context;
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<ProductDetailsResponse> productDetails(String slug) {
        MutableLiveData<ProductDetailsResponse> data = new MutableLiveData<>();
        ProductDetailsRequest productDetailsRequest = new ProductDetailsRequest(
                "onetouch.com",
                slug);
        try {
            apiService.getProductDetails(productDetailsRequest).enqueue(new Callback<ProductDetailsResponse>() {
                @Override
                public void onResponse(@NonNull Call<ProductDetailsResponse> call, @NonNull Response<ProductDetailsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        data.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ProductDetailsResponse> call, @NonNull Throwable t) {
                    data.postValue(null);
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "Failed due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return data;
    }

    public LiveData<RelatedProductResponse> getRelatedProduct(RelatedProductRequest relatedProductRequest) {
        MutableLiveData<RelatedProductResponse> data = new MutableLiveData<>();
        apiService.relatedProduct(relatedProductRequest).enqueue(new Callback<RelatedProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<RelatedProductResponse> call, @NonNull Response<RelatedProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RelatedProductResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }
}
