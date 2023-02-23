package com.axepert.onetouch.ui.sellerdetails;

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
import com.axepert.onetouch.adapters.AdapterAllSellers;
import com.axepert.onetouch.databinding.FragmentAllSellersBinding;
import com.axepert.onetouch.listeners.BestSellerListener;
import com.axepert.onetouch.responses.HomeScreenResponse;
import com.axepert.onetouch.responses.SellerInformation;
import com.axepert.onetouch.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class AllSellersFragment extends Fragment implements BestSellerListener {
    private FragmentAllSellersBinding binding;
    private HomeViewModel viewModel;
    private List<SellerInformation> sellerInformationList;
    private AdapterAllSellers adapterAllSellers;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllSellersBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        setListener();
        initSellerRV();
        return binding.getRoot();
    }

    private void setListener() {
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());
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
            sellerInformationList.clear();
            viewModel.refreshHomeScreen();
            binding.swipeRefresh.setRefreshing(false);
        });
    }

    private void initSellerRV() {
        sellerInformationList = new ArrayList<>();
        binding.recyclerSellers.setHasFixedSize(true);
        adapterAllSellers = new AdapterAllSellers(sellerInformationList, this);
        binding.recyclerSellers.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerSellers.setAdapter(adapterAllSellers);

        fetchAllSellers();
    }

    private void filterList(String str) {
        List<SellerInformation> filterList = new ArrayList<>();
        for (SellerInformation item : sellerInformationList) {
            if (item.getName().toLowerCase().contains(str.toLowerCase()) || item.getShop_address().toLowerCase().contains(str.toLowerCase())) {
                filterList.add(item);
            }
        }
        adapterAllSellers.filterList(filterList);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchAllSellers() {
        viewModel.fetchHomeScreen().observe(requireActivity(), homeScreenResponse -> {
            if (homeScreenResponse != null) {
                if (homeScreenResponse.code == 200) {
                    sellerInformationList.addAll(homeScreenResponse.data.getSeller_info());
                    adapterAllSellers.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onSellerClick(SellerInformation sellerInformation) {
        Bundle bundle = new Bundle();
        bundle.putString("id", sellerInformation.getId());
        bundle.putString("name", sellerInformation.getName());
        bundle.putString("email", sellerInformation.getEmail());
        bundle.putString("contact", sellerInformation.getContact());
        bundle.putString("image", sellerInformation.getImage());
        bundle.putString("shop_name", sellerInformation.getShop_name());
        bundle.putString("shop_add", sellerInformation.getShop_address());
        bundle.putString("desc", sellerInformation.getDescription());
        bundle.putString("seller_video", sellerInformation.getSeller_video());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_allSellersFragment_to_sellerDetailsFragment, bundle);
    }
}