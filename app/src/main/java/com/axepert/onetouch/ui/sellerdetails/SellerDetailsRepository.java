package com.axepert.onetouch.ui.sellerdetails;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.AddReviewRequest;
import com.axepert.onetouch.responses.AddReviewResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerDetailsRepository {

    private ApiService apiService;

    public SellerDetailsRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public MutableLiveData<AddReviewResponse> addReview(AddReviewRequest addReviewRequest) {
        MutableLiveData<AddReviewResponse> data = new MutableLiveData<>();
        apiService.addReview(addReviewRequest).enqueue(new Callback<AddReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddReviewResponse> call, @NonNull Response<AddReviewResponse> response) {
                data.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<AddReviewResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

}
