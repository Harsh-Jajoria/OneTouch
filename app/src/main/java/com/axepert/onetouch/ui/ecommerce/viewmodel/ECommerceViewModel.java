package com.axepert.onetouch.ui.ecommerce.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.axepert.onetouch.database.CartDatabase;
import com.axepert.onetouch.models.CartModel;
import com.axepert.onetouch.responses.ECommHomePageResponse;
import com.axepert.onetouch.responses.TrendingProduct;
import com.axepert.onetouch.ui.ecommerce.repositories.ECommerceRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class ECommerceViewModel extends AndroidViewModel {

    private ECommerceRepository eCommerceRepository;
    private MutableLiveData<ECommHomePageResponse> eCommHomePageResponseMutableLiveData;
    private CartDatabase cartDatabase;

    public ECommerceViewModel(@NonNull Application application) {
        super(application);
        eCommerceRepository = new ECommerceRepository();
        eCommHomePageResponseMutableLiveData = eCommerceRepository.ecommerceHome();
        cartDatabase = CartDatabase.getCartDatabase(application);
    }

    public MutableLiveData<ECommHomePageResponse> ecommerceHomePage() {
        return eCommHomePageResponseMutableLiveData;
    }

    public MutableLiveData<ECommHomePageResponse> refreshEComm() {
        eCommHomePageResponseMutableLiveData = eCommerceRepository.ecommerceHome();
        return eCommHomePageResponseMutableLiveData;
    }

    public Flowable<List<CartModel>> loadCartList() {
        return cartDatabase.cartDao().getCart();
    }

}
