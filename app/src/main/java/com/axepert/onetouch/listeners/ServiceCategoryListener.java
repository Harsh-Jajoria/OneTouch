package com.axepert.onetouch.listeners;

import com.axepert.onetouch.responses.MyServiceCategoryResponse;

public interface ServiceCategoryListener {
    void onClick(MyServiceCategoryResponse.Datum data);
}
