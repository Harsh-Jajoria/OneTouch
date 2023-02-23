package com.axepert.onetouch.ui.ecommerce.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterAddress;
import com.axepert.onetouch.databinding.FragmentAddressesBinding;
import com.axepert.onetouch.listeners.AddressListener;
import com.axepert.onetouch.responses.AddressListResponse;
import com.axepert.onetouch.ui.ecommerce.viewmodel.AddressListViewModel;
import com.axepert.onetouch.utilities.Constants;
import com.axepert.onetouch.utilities.PreferenceManager;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddressesFragment extends Fragment implements AddressListener {
    private FragmentAddressesBinding binding;
    private AddressListViewModel viewModel;
    private PreferenceManager preferenceManager;
    private List<AddressListResponse.Datum> addressList;
    private AdapterAddress adapterAddress;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddressesBinding.inflate(getLayoutInflater(), container, false);
        preferenceManager = new PreferenceManager(requireContext());
        viewModel = new ViewModelProvider(this).get(AddressListViewModel.class);
        init();
        setListener();
        return binding.getRoot();
    }

    private void setListener() {
        binding.toolbar.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());
        binding.fabAddAddress.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addressesFragment_to_addAddressFragment));
    }

    private void init() {
        addressList = new ArrayList<>();
        adapterAddress = new AdapterAddress(addressList, this);
        binding.recyclerViewMyAddresses.setHasFixedSize(true);
        binding.recyclerViewMyAddresses.setAdapter(adapterAddress);
        getAddress();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getAddress() {
        viewModel.addressList(preferenceManager.getString(Constants.KEY_USER_ID)).observe(requireActivity(), addressListResponse -> {
            if (addressListResponse != null) {
                if (addressListResponse.code == 200) {
                    if (!addressList.isEmpty()) {
                        addressList.clear();
                    }
                    binding.loading.setVisibility(View.GONE);
                    binding.recyclerViewMyAddresses.setVisibility(View.VISIBLE);
                    addressList.addAll(addressListResponse.data);
                    adapterAddress.notifyDataSetChanged();
                } else {
                    binding.loading.setVisibility(View.GONE);
//                Toast.makeText(requireContext(), "No address found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        addressList.clear();
        binding = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addressList.clear();
        binding = null;
    }

    @Override
    public void onAddressClicked(AddressListResponse.Datum datum) {
        if (getArguments() != null) {
            if (getArguments().getInt("payment") == 1) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("address", datum);
                Navigation.findNavController(binding.getRoot()).navigate(R.id.checkOutFragment, bundle);
            }
        }
        /*Intent intent = requireActivity().getIntent();
                requireActivity().overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                requireActivity().finish();
                requireActivity().overridePendingTransition(0, 0);
                startActivity(intent);*/
    }

    @Override
    public void onEditAddressClicked(AddressListResponse.Datum datum) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("address", datum);
        bundle.putBoolean("edit", true);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addressesFragment_to_addAddressFragment, bundle);
    }
}