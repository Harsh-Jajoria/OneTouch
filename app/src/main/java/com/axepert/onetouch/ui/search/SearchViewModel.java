package com.axepert.onetouch.ui.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.axepert.onetouch.responses.SearchProductResponse;

public class SearchViewModel extends AndroidViewModel {
    private SearchRepository searchRepository;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        searchRepository = new SearchRepository();
    }

    public MutableLiveData<SearchProductResponse> searchProduct(String item) {
        return searchRepository.searchProduct(item);
    }

}
