package com.axepert.onetouch.ui.ecommerce.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.AddressListRequest;
import com.axepert.onetouch.responses.AddressListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressListViewModel extends ViewModel {

    private ApiService apiService;
    private MutableLiveData<AddressListResponse> addressList = new MutableLiveData<>();

    public AddressListViewModel() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<AddressListResponse> addressList(String user_id) {
        AddressListRequest addressListRequest = new AddressListRequest(
                user_id,
                "onetouch.com");

        apiService.getAddressList(addressListRequest).enqueue(new Callback<AddressListResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddressListResponse> call, @NonNull Response<AddressListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    addressList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddressListResponse> call, @NonNull Throwable t) {
                addressList.postValue(null);
            }
        });
        return addressList;
    }

}
