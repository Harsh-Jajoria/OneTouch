package com.axepert.onetouch.ui.account;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.EditProfileRequest;
import com.axepert.onetouch.responses.EditProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileViewModel extends ViewModel {

    private ApiService apiService;

    public EditProfileViewModel() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<EditProfileResponse> editUserProfile(EditProfileRequest editProfileRequest) {
        MutableLiveData<EditProfileResponse> data = new MutableLiveData<>();
        apiService.editUserProfile(editProfileRequest).enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<EditProfileResponse> call, @NonNull Response<EditProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<EditProfileResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

}
