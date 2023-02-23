package com.axepert.onetouch.ui.ecommerce.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterProductByCategory;
import com.axepert.onetouch.databinding.FragmentProductByCategoryBinding;
import com.axepert.onetouch.listeners.ProductByCategoryListener;
import com.axepert.onetouch.responses.ProductByCatResponse;
import com.axepert.onetouch.ui.ecommerce.viewmodel.ProductByCategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductByCategoryFragment extends Fragment implements ProductByCategoryListener {
    private FragmentProductByCategoryBinding binding;
    private List<ProductByCatResponse.Datum> productList;
    private AdapterProductByCategory adapterProductByCategory;
    private ProductByCategoryViewModel viewModel;
    private String cat_name, cat_id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductByCategoryBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(this).get(ProductByCategoryViewModel.class);
        if (getArguments() != null) {
            cat_id = getArguments().getString("cat_id");
            cat_name = getArguments().getString("cat_name");
        }
        setListener();
        init();
        getProducts();
        return binding.getRoot();
    }

    private void init() {
        binding.recyclerViewCatViseProduct.setHasFixedSize(true);
        productList = new ArrayList<>();
        adapterProductByCategory = new AdapterProductByCategory(productList, this);
        binding.recyclerViewCatViseProduct.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewCatViseProduct.setAdapter(adapterProductByCategory);
    }

    private void setListener() {
        binding.toolbar.setTitle(cat_name);
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
            getProducts();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getProducts() {
        viewModel.productByCat(cat_id).observe(getViewLifecycleOwner(), productByCatResponse -> {
            if (productByCatResponse.code == 200) {
                if (productByCatResponse.data.isEmpty()) {
                    binding.swipeRefresh.setRefreshing(false);
                    binding.notFount.setVisibility(View.VISIBLE);
                } else {
                    productList.addAll(productByCatResponse.data);
                    adapterProductByCategory.notifyDataSetChanged();
                    binding.swipeRefresh.setRefreshing(false);
                }
            } else {
                binding.notFount.setVisibility(View.VISIBLE);
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void filterList(String str) {
        List<ProductByCatResponse.Datum> filterList = new ArrayList<>();
        for (ProductByCatResponse.Datum item : productList) {
            if (item.getName().toLowerCase().contains(str.toLowerCase())) {
                filterList.add(item);
            }
        }
        adapterProductByCategory.filterList(filterList);
    }

    @Override
    public void onProductClicked(ProductByCatResponse.Datum product) {
        Bundle bundle = new Bundle();
        bundle.putString("slug", product.getSlug());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_productByCategoryFragment_to_productDetailsFragment, bundle);
    }
}