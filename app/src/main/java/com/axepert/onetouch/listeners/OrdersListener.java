package com.axepert.onetouch.listeners;

import com.axepert.onetouch.responses.OrdersResponse;

public interface OrdersListener {
    void onOrderClicked(OrdersResponse.Datum order);
}
