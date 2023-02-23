package com.axepert.onetouch.ui.ecommerce.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.axepert.onetouch.requests.ProductListRequest;
import com.axepert.onetouch.responses.ProductListResponse;
import com.axepert.onetouch.ui.ecommerce.repositories.ProductListRepository;

public class ProductListViewModel extends AndroidViewModel {
    private MutableLiveData<ProductListResponse> productListResponseMutableLiveData;
    private ProductListRepository productListRepository;


    public ProductListViewModel(Application application) {
        super(application);
        productListRepository = new ProductListRepository();
    }

    public MutableLiveData<ProductListResponse> allProducts(ProductListRequest productListRequest) {
        return productListResponseMutableLiveData = productListRepository.allProducts(productListRequest);
    }

}
