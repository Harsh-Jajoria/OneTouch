package com.axepert.onetouch.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.axepert.onetouch.responses.HomeScreenResponse;

public class HomeViewModel extends AndroidViewModel {

    private HomePageRepository homePageRepository;
    MutableLiveData<HomeScreenResponse> response;

    public HomeViewModel(Application application) {
        super(application);
        homePageRepository = new HomePageRepository();
        response = homePageRepository.getHomePageData(application);
    }

    public MutableLiveData<HomeScreenResponse> fetchHomeScreen() {
        return response;
    }

    public MutableLiveData<HomeScreenResponse> refreshHomeScreen() {
        response = homePageRepository.getHomePageData(getApplication());
        return response;
    }

}
