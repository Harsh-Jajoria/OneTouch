package com.axepert.onetouch.ui.ecommerce.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.AddAddressRequest;
import com.axepert.onetouch.responses.AddAddressResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressViewModel extends ViewModel {

    private ApiService apiService;

    public AddAddressViewModel() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<AddAddressResponse> addAddress(AddAddressRequest addAddressRequest) {
        MutableLiveData<AddAddressResponse> data = new MutableLiveData<>();
        apiService.addAddress(addAddressRequest).enqueue(new Callback<AddAddressResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddAddressResponse> call, @NonNull Response<AddAddressResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddAddressResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<AddAddressResponse> editAddress(AddAddressRequest addAddressRequest) {
        MutableLiveData<AddAddressResponse> data = new MutableLiveData<>();
        apiService.editAddress(addAddressRequest).enqueue(new Callback<AddAddressResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddAddressResponse> call, @NonNull Response<AddAddressResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddAddressResponse> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

}
