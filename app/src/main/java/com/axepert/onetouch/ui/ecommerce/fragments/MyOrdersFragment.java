package com.axepert.onetouch.ui.ecommerce.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterOrders;
import com.axepert.onetouch.databinding.FragmentMyOrdersBinding;
import com.axepert.onetouch.listeners.OrdersListener;
import com.axepert.onetouch.responses.OrdersResponse;
import com.axepert.onetouch.ui.ecommerce.viewmodel.OrderViewModel;
import com.axepert.onetouch.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment implements OrdersListener {
    private FragmentMyOrdersBinding binding;
    private PreferenceManager preferenceManager;
    private OrderViewModel viewModel;
    private List<OrdersResponse.Datum> orderList;
    private AdapterOrders adapterOrders;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyOrdersBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        init();
        fetchOrder();
        setListener();
        return binding.getRoot();
    }

    private void setListener() {
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());
        binding.swipeRefresh.setOnRefreshListener(() -> {
            orderList.clear();
            fetchOrder();
        });
    }

    private void init() {
        binding.ordersRecyclerView.setHasFixedSize(true);
        orderList = new ArrayList<>();
        adapterOrders = new AdapterOrders(orderList, this);
        binding.ordersRecyclerView.setAdapter(adapterOrders);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchOrder() {
        loading(true);
        viewModel.orders("163").observe(getViewLifecycleOwner(), ordersResponse -> {
            if (ordersResponse != null) {
                if (ordersResponse.code == 200) {
                    orderList.addAll(ordersResponse.data);
                    adapterOrders.notifyDataSetChanged();
                    binding.loading.setVisibility(View.GONE);
                    binding.ordersRecyclerView.setVisibility(View.VISIBLE);
                    binding.swipeRefresh.setRefreshing(false);
                    loading(false);
                } else {
                    loading(false);
                }
            } else {
                loading(false);
            }
        });
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            binding.loading.setVisibility(View.VISIBLE);
            binding.swipeRefresh.setVisibility(View.GONE);
        } else {
            binding.loading.setVisibility(View.GONE);
            binding.swipeRefresh.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onOrderClicked(OrdersResponse.Datum order) {
        Bundle bundle = new Bundle();
        bundle.putString("order_id", order.getOrder_id());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_myOrdersFragment_to_invoiceFragment, bundle);
    }
}