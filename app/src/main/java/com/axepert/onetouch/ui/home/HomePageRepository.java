package com.axepert.onetouch.ui.home;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.responses.HomeScreenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageRepository {
    private final ApiService apiService;

    public HomePageRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public MutableLiveData<HomeScreenResponse> getHomePageData(Context context) {
        MutableLiveData<HomeScreenResponse> data = new MutableLiveData<>();
        try {
            apiService.getHomeData().enqueue(new Callback<HomeScreenResponse>() {
                @Override
                public void onResponse(@NonNull Call<HomeScreenResponse> call, @NonNull Response<HomeScreenResponse> response) {
                    data.setValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<HomeScreenResponse> call, @NonNull Throwable t) {
                    data.setValue(null);
                    Toast.makeText(context, "Response Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            Toast.makeText(context, "Something went wrong try again after some time.", Toast.LENGTH_SHORT).show();
        }
        return data;
    }

}
