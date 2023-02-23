package com.axepert.onetouch.ui.ecommerce.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterAllProducts;
import com.axepert.onetouch.databinding.FragmentProductListBinding;
import com.axepert.onetouch.listeners.ProductListListener;
import com.axepert.onetouch.requests.ProductListRequest;
import com.axepert.onetouch.responses.ProductListResponse;
import com.axepert.onetouch.responses.SellerInformation;
import com.axepert.onetouch.ui.ecommerce.viewmodel.ProductListViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment implements ProductListListener {
    private FragmentProductListBinding binding;
    private ProductListViewModel viewModel;
    private List<ProductListResponse.Datum> productList;
    private AdapterAllProducts adapterAllProducts;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductListBinding.inflate(getLayoutInflater(), container, false);
        init();
        setListener();
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productList = new ArrayList<>();
        adapterAllProducts = new AdapterAllProducts(productList, this);
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);
        binding.recyclerProducts.setHasFixedSize(true);
        binding.recyclerProducts.setAdapter(adapterAllProducts);
        getAllProducts();
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
            productList.clear();
            getAllProducts();
        });
    }

    private void filterList(String str) {
        List<ProductListResponse.Datum> filterList = new ArrayList<>();
        for (ProductListResponse.Datum item : productList) {
            if (item.getName().toLowerCase().contains(str.toLowerCase())) {
                filterList.add(item);
            }
        }
        adapterAllProducts.filterList(filterList);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getAllProducts() {
        loading(true);
        ProductListRequest productListRequest = new ProductListRequest("onetouch.com", "", "", "");
        viewModel.allProducts(productListRequest).observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                if (response.code == 200) {
                    productList.addAll(response.data);
                    adapterAllProducts.notifyDataSetChanged();
                }
            }
            loading(false);
            binding.swipeRefresh.setRefreshing(false);
        });
    }

    @Override
    public void onProductClicked(ProductListResponse.Datum product) {
        Bundle bundle = new Bundle();
        bundle.putString("slug", product.getSlug());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.productDetailsFragment, bundle);
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            binding.swipeRefresh.setVisibility(View.GONE);
            binding.loading.setVisibility(View.VISIBLE);
        } else {
            binding.swipeRefresh.setVisibility(View.VISIBLE);
            binding.loading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        productList.clear();
    }
}