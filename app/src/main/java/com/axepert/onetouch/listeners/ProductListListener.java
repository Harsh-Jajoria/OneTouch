package com.axepert.onetouch.listeners;

import com.axepert.onetouch.responses.ProductListResponse;

public interface ProductListListener {
    void onProductClicked(ProductListResponse.Datum product);
}
