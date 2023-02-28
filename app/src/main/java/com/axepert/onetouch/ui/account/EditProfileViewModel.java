package com.axepert.onetouch.ui.account;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.EditDealerProfileRequest;
import com.axepert.onetouch.requests.EditProfileRequest;
import com.axepert.onetouch.requests.MyServiceCategoryRequest;
import com.axepert.onetouch.requests.SubCategoryRequest;
import com.axepert.onetouch.responses.EditProfileResponse;
import com.axepert.onetouch.responses.MyServiceCategoryResponse;
import com.axepert.onetouch.responses.PlaceOrderResponse;

import org.apache.commons.lang3.mutable.Mutable;

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

    public LiveData<MyServiceCategoryResponse> getServiceCategory(String id) {
        MutableLiveData<MyServiceCategoryResponse> data = new MutableLiveData<>();

        MyServiceCategoryRequest myServiceCategoryRequest = new MyServiceCategoryRequest(id);
        apiService.myServiceCategory(myServiceCategoryRequest).enqueue(new Callback<MyServiceCategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyServiceCategoryResponse> call, @NonNull Response<MyServiceCategoryResponse> response) {
                if (response.isSuccessful() && null != response.body()) {
                    data.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyServiceCategoryResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<MyServiceCategoryResponse> getServiceSubCategory(String id) {
        MutableLiveData<MyServiceCategoryResponse> data = new MutableLiveData<>();

        SubCategoryRequest subCategoryRequest = new SubCategoryRequest("onetouch.com", id);
        apiService.subCategory(subCategoryRequest).enqueue(new Callback<MyServiceCategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyServiceCategoryResponse> call, @NonNull Response<MyServiceCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyServiceCategoryResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<PlaceOrderResponse> editDealerProfile(EditDealerProfileRequest editDealerProfileRequest) {
        MutableLiveData<PlaceOrderResponse> data = new MutableLiveData<>();
        apiService.editDealerProfile(editDealerProfileRequest).enqueue(new Callback<PlaceOrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceOrderResponse> call, @NonNull Response<PlaceOrderResponse> response) {
                if (response.isSuccessful() && null != response.body()) {
                    data.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaceOrderResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

}
