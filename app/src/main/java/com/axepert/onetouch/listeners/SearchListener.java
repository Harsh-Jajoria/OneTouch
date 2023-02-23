package com.axepert.onetouch.listeners;

import com.axepert.onetouch.responses.SearchProductResponse;

public interface SearchListener {
    void onSearchItemClicked(SearchProductResponse.Datum searchItem);
}
