package com.axepert.onetouch.listeners;

import com.axepert.onetouch.responses.ProductByCatResponse;
import com.axepert.onetouch.responses.ProductCategory;

public interface ProductCategoryListener {
    void onCategoryClicked(ProductCategory productCategory);
}
