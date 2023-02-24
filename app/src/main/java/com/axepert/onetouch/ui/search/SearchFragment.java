package com.axepert.onetouch.ui.search;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterSearchResult;
import com.axepert.onetouch.databinding.FragmentSearchBinding;
import com.axepert.onetouch.listeners.SearchListener;
import com.axepert.onetouch.responses.SearchProductResponse;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchListener {
    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private List<SearchProductResponse.Datum> searchList;
    private AdapterSearchResult adapterSearchResult;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        setListener();
        init();
        return binding.getRoot();
    }

    private void init() {
        searchList = new ArrayList<>();
        adapterSearchResult = new AdapterSearchResult(searchList, this);
        binding.recyclerViewSearch.setHasFixedSize(true);
        binding.recyclerViewSearch.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        binding.recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewSearch.setAdapter(adapterSearchResult);
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
                searchList.clear();
                search(s.toString());
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void search(String item) {
        binding.progress.setVisibility(View.VISIBLE);
        viewModel.searchProduct(item).observe(this, searchProductResponse -> {
            if (searchProductResponse != null) {
                if (searchProductResponse.code == 200) {
                    searchList.addAll(searchProductResponse.data);
                    adapterSearchResult.notifyDataSetChanged();
                    binding.progress.setVisibility(View.GONE);
                } else {
                    binding.progress.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(requireActivity(), "Unable to get data.", Toast.LENGTH_LONG).show();
                binding.progress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSearchItemClicked(SearchProductResponse.Datum searchItem) {
        Bundle bundle = new Bundle();
        if (searchItem.getKey().equals("product")) {
            bundle.putString("slug", searchItem.getSlug());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.productDetailsFragment, bundle);
        } else {
            bundle.putString("cat_id", searchItem.getId());
            bundle.putString("cat_name", searchItem.getName());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_searchFragment_to_productByCategoryFragment, bundle);
        }

    }
}