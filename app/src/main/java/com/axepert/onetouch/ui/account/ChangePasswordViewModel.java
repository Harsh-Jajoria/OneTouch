package com.axepert.onetouch.ui.account;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.ChangePasswordRequest;
import com.axepert.onetouch.responses.ChangePasswordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordViewModel extends ViewModel {

    private final ApiService apiService;


    public ChangePasswordViewModel() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<ChangePasswordResponse> changePassword(ChangePasswordRequest changePasswordRequest) {
        MutableLiveData<ChangePasswordResponse> data = new MutableLiveData<>();
        apiService.changePassword(changePasswordRequest).enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChangePasswordResponse> call, @NonNull Response<ChangePasswordResponse> response) {
                if (response.body() != null) {
                    if (response.body().code == 200) {
                        data.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChangePasswordResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

}
