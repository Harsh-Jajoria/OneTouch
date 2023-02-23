package com.axepert.onetouch.ui.ecommerce.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.axepert.onetouch.databinding.FragmentCategoriesBinding;

public class CategoriesFragment extends Fragment {
    private FragmentCategoriesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }



}