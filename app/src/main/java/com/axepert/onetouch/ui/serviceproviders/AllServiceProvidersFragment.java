package com.axepert.onetouch.ui.serviceproviders;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterAllServiceProviders;
import com.axepert.onetouch.databinding.FragmentAllServiceProvidersBinding;
import com.axepert.onetouch.responses.HomeScreenResponse;
import com.axepert.onetouch.responses.ServiceProviders;
import com.axepert.onetouch.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class AllServiceProvidersFragment extends Fragment {
    private FragmentAllServiceProvidersBinding binding;
    private HomeViewModel viewModel;
    private List<ServiceProviders> serviceProvidersList;
    private AdapterAllServiceProviders adapterAllServiceProviders;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllServiceProvidersBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        setListener();
        initServiceProviderRV();
        return binding.getRoot();
    }

    private void initServiceProviderRV() {
        binding.recyclerServiceProviders.setHasFixedSize(true);
        serviceProvidersList = new ArrayList<>();
        adapterAllServiceProviders = new AdapterAllServiceProviders(serviceProvidersList);
        binding.recyclerServiceProviders.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerServiceProviders.setAdapter(adapterAllServiceProviders);

        fetchServiceProviders();
    }

    private void filterList(String str) {
        List<ServiceProviders> filterList = new ArrayList<>();
        for (ServiceProviders item : serviceProvidersList) {
            if (item.getName().toLowerCase().contains(str.toLowerCase())) {
                filterList.add(item);
            }
        }
        adapterAllServiceProviders.filterList(filterList);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchServiceProviders() {
        viewModel.fetchHomeScreen().observe(requireActivity(), homeScreenResponse -> {
            if (homeScreenResponse != null) {
                if (homeScreenResponse.code == 200) {
                    serviceProvidersList.addAll(homeScreenResponse.data.getService_provider());
                    adapterAllServiceProviders.notifyDataSetChanged();
                    binding.swipeRefresh.setRefreshing(false);
                } else {

                }
            } else {

            }
        });
    }

    private void setListener() {
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        binding.swipeRefresh.setOnRefreshListener(() -> {
            serviceProvidersList.clear();
            fetchServiceProviders();
        });
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterList(s.toString());
            }
        });
    }
}