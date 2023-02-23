package com.axepert.onetouch.ui.search;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.SearchProductRequest;
import com.axepert.onetouch.responses.SearchProductResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {

    private ApiService apiService;

    public SearchRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public MutableLiveData<SearchProductResponse> searchProduct(String item) {
        MutableLiveData<SearchProductResponse> data = new MutableLiveData<>();
        SearchProductRequest searchProductRequest = new SearchProductRequest("onetouch.com", item);
        apiService.getProductBySearch(searchProductRequest).enqueue(new Callback<SearchProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchProductResponse> call, @NonNull Response<SearchProductResponse> response) {
                data.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<SearchProductResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

}
