package com.axepert.onetouch.listeners;

import com.axepert.onetouch.responses.AddressListResponse;

public interface AddressListener {
    void onAddressClicked(AddressListResponse.Datum datum);

    void onEditAddressClicked(AddressListResponse.Datum datum);
}
