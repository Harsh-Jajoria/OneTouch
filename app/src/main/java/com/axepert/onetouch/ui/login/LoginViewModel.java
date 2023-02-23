package com.axepert.onetouch.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.LoginRequest;
import com.axepert.onetouch.responses.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private final ApiService apiService;

    public LoginViewModel() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public MutableLiveData<LoginResponse> login(String email, String password) {
        MutableLiveData<LoginResponse> data = new MutableLiveData<>();
        LoginRequest loginRequest = new LoginRequest(email, password);
        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                data.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<LoginResponse> userLogin(String email, String password) {
        MutableLiveData<LoginResponse> data = new MutableLiveData<>();
        LoginRequest loginRequest = new LoginRequest(email, password);
        apiService.userLogin(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                data.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

}
