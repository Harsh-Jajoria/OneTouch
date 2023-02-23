package com.axepert.onetouch.listeners;

import com.axepert.onetouch.responses.ProductByCatResponse;

public interface ProductByCategoryListener {
    void onProductClicked(ProductByCatResponse.Datum product);
}
