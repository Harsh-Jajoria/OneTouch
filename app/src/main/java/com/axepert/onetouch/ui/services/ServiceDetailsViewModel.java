package com.axepert.onetouch.ui.services;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.axepert.onetouch.responses.RelatedServiceProvidersResponse;

public class ServiceDetailsViewModel extends AndroidViewModel {

    private ServicesRepository servicesRepository;

    public ServiceDetailsViewModel(@NonNull Application application) {
        super(application);
        servicesRepository = new ServicesRepository();
    }

    public MutableLiveData<RelatedServiceProvidersResponse> getRelatedServiceProviders(String id) {
        return servicesRepository.relatedServiceProviders(id);
    }

}