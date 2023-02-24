package com.axepert.onetouch.ui.myservices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.axepert.onetouch.adapters.AdapterMyServiceTabs;
import com.axepert.onetouch.databinding.FragmentMyServicesBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyServicesFragment extends Fragment {
    AdapterMyServiceTabs adapterMyServiceTabs;
    private FragmentMyServicesBinding binding;
    private String[] tabs = {"Categories", "Sub-Categories"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyServicesBinding.inflate(getLayoutInflater(), container, false);
        setupTabs();
        setListener();
        return binding.getRoot();
    }

    private void setListener() {
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());
    }

    private void setupTabs() {
        adapterMyServiceTabs = new AdapterMyServiceTabs(requireActivity());
        binding.viewpager2.setAdapter(adapterMyServiceTabs);
        new TabLayoutMediator(binding.tabLayout, binding.viewpager2, (tab, position) -> tab.setText(tabs[position])).attach();
    }

}