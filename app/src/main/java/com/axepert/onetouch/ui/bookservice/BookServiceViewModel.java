package com.axepert.onetouch.ui.bookservice;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.axepert.onetouch.requests.BookServiceRequest;
import com.axepert.onetouch.responses.BookServiceResponse;

public class BookServiceViewModel extends AndroidViewModel {

    private BookServiceRepository bookServiceRepository;

    public BookServiceViewModel(@NonNull Application application) {
        super(application);
         bookServiceRepository = new BookServiceRepository();
    }

    public MutableLiveData<BookServiceResponse> bookService(BookServiceRequest bookServiceRequest) {
        return bookServiceRepository.bookService(bookServiceRequest);
    }

}
