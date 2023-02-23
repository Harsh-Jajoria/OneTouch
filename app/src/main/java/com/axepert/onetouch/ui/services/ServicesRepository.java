package com.axepert.onetouch.ui.services;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.RelatedServiceProvidersRequest;
import com.axepert.onetouch.responses.RelatedServiceProvidersResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesRepository {

    private ApiService apiService;

    public ServicesRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public MutableLiveData<RelatedServiceProvidersResponse> relatedServiceProviders(String id) {
        MutableLiveData<RelatedServiceProvidersResponse> data = new MutableLiveData<>();
        RelatedServiceProvidersRequest relatedServiceProvidersRequest = new RelatedServiceProvidersRequest(id);
        apiService.getServiceProviders(relatedServiceProvidersRequest)
                .enqueue(new Callback<RelatedServiceProvidersResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RelatedServiceProvidersResponse> call, @NonNull Response<RelatedServiceProvidersResponse> response) {
                        data.postValue(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<RelatedServiceProvidersResponse> call, @NonNull Throwable t) {
                        data.postValue(null);
                    }
                });
        return data;
    }

}
