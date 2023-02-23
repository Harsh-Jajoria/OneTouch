package com.axepert.onetouch.ui.bookservice;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.BookServiceRequest;
import com.axepert.onetouch.responses.BookServiceResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookServiceRepository {

    private ApiService apiService;

    public BookServiceRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public MutableLiveData<BookServiceResponse> bookService(BookServiceRequest bookServiceRequest) {
        MutableLiveData<BookServiceResponse> data = new MutableLiveData<>();
        apiService.bookService(bookServiceRequest).enqueue(new Callback<BookServiceResponse>() {
            @Override
            public void onResponse(@NonNull Call<BookServiceResponse> call, @NonNull Response<BookServiceResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<BookServiceResponse> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

}
