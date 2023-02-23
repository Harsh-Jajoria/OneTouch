package com.axepert.onetouch.ui.services;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.axepert.onetouch.adapters.AdapterAllServices;
import com.axepert.onetouch.adapters.AdapterBlogs;
import com.axepert.onetouch.databinding.FragmentAllServicesBinding;
import com.axepert.onetouch.listeners.ServicesListener;
import com.axepert.onetouch.responses.Services;
import com.axepert.onetouch.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class AllServicesFragment extends Fragment implements ServicesListener {
    private FragmentAllServicesBinding binding;
    private AdapterAllServices adapterAllServices;
    private List<Services> servicesList;
    private HomeViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllServicesBinding.inflate(getLayoutInflater(), container,false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        initBlogRV();
        setListener();
        return binding.getRoot();
    }

    private void setListener() {
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());
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
        binding.swipeRefresh.setOnRefreshListener(() -> {
            servicesList.clear();
            viewModel.refreshHomeScreen();
            binding.swipeRefresh.setRefreshing(false);
        });
    }

    private void filterList(String str) {
        List<Services> filterList = new ArrayList<>();
        for (Services item : servicesList) {
            if (item.getService_type().toLowerCase().contains(str.toLowerCase())) {
                filterList.add(item);
            }
        }
        adapterAllServices.filterList(filterList);
    }

    private void initBlogRV() {
        servicesList = new ArrayList<>();
        binding.recyclerServices.setHasFixedSize(true);
        adapterAllServices = new AdapterAllServices(servicesList, this);
        binding.recyclerServices.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerServices.setAdapter(adapterAllServices);

        fetchServices();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchServices() {
        viewModel.fetchHomeScreen().observe(requireActivity(), homeScreenResponse -> {
            if (homeScreenResponse != null) {
                if (homeScreenResponse.code == 200) {
                    // Blogs
                    servicesList.addAll(homeScreenResponse.data.getServices());
                    adapterAllServices.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(Services services) {
        Bundle bundle = new Bundle();
        bundle.putString("category", services.getService_type());
        bundle.putString("description", services.getDescription());
        bundle.putString("imageUrl", services.getServices_image());
        bundle.putString("id", services.getId());
        bundle.putInt("count", services.getProvider_count());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_allServicesFragment_to_serviceDetailsFragment, bundle);
    }
}