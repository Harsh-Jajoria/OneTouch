package com.axepert.onetouch.ui.sellerdetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.axepert.onetouch.requests.AddReviewRequest;
import com.axepert.onetouch.responses.AddReviewResponse;

public class SellerDetailsViewModel extends AndroidViewModel {

    private SellerDetailsRepository sellerDetailsRepository;

    public SellerDetailsViewModel(@NonNull Application application) {
        super(application);
        sellerDetailsRepository = new SellerDetailsRepository();
    }

    public MutableLiveData<AddReviewResponse> addReview(AddReviewRequest addReviewRequest) {
        return sellerDetailsRepository.addReview(addReviewRequest);
    }

}
