package com.axepert.onetouch.ui.registration;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.RegisterRequest;
import com.axepert.onetouch.responses.RegistrationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationViewModel extends ViewModel {
    private final ApiService apiService;

    public RegistrationViewModel() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public MutableLiveData<RegistrationResponse> register(RegisterRequest registerRequest) {
        MutableLiveData<RegistrationResponse> data = new MutableLiveData<>();
        apiService.register(registerRequest).enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponse> call, @NonNull Response<RegistrationResponse> response) {
                data.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<RegistrationResponse> userRegistration(RegisterRequest registerRequest) {
        MutableLiveData<RegistrationResponse> data = new MutableLiveData<>();
        apiService.userRegister(registerRequest).enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponse> call, @NonNull Response<RegistrationResponse> response) {
                data.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

}
