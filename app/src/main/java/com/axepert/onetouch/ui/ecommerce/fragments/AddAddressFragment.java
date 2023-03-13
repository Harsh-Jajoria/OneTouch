package com.axepert.onetouch.ui.ecommerce.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.axepert.onetouch.databinding.FragmentAddAddressBinding;
import com.axepert.onetouch.requests.AddAddressRequest;
import com.axepert.onetouch.responses.AddressListResponse;
import com.axepert.onetouch.ui.ecommerce.viewmodel.AddAddressViewModel;
import com.axepert.onetouch.utilities.Constants;
import com.axepert.onetouch.utilities.PreferenceManager;

import java.util.Objects;

public class AddAddressFragment extends Fragment {
    private FragmentAddAddressBinding binding;
    private PreferenceManager preferenceManager;
    private AddAddressViewModel viewModel;
    private ProgressDialog progressDialog;
    private AddressListResponse.Datum address = new AddressListResponse.Datum();
    private Boolean isEdit = false;
    private String id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddAddressBinding.inflate(getLayoutInflater(), container, false);
        preferenceManager = new PreferenceManager(requireActivity());
        viewModel = new ViewModelProvider(this).get(AddAddressViewModel.class);

        if (getArguments() != null) {
            address = (AddressListResponse.Datum) getArguments().getSerializable("address");
            isEdit = getArguments().getBoolean("edit");
            id = address.getId();
            showSavedAddressFromBundle();
        }

        setListener();
        showProgress();
        return binding.getRoot();
    }

    private void showSavedAddressFromBundle() {
        if (!address.getName().isEmpty()) {
            binding.tvName.setText(address.getName());
        }
        if (!address.getMobile().isEmpty()) {
            binding.tvPhone.setText(address.getMobile());
        }
        if (!address.getEmail().isEmpty()) {
            binding.tvEmail.setText(address.getEmail());
        }
        if (!address.getAddress().isEmpty()) {
            binding.tvAddress.setText(address.getAddress());
        }
        if (!address.getLandmark().isEmpty()) {
            binding.tvLandmark.setText(address.getLandmark());
        }
        if (!address.getCity().isEmpty()) {
            binding.tvTownCity.setText(address.getCity());
        }
        if (!address.getState().isEmpty()) {
            binding.tvStateCountry.setText(address.getState());
        }
        if (!address.getPincode().isEmpty()) {
            binding.tvPostalCode.setText(address.getPincode());
        }
    }

    private void setListener() {
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());
        binding.btnSave.setOnClickListener(v -> {
            if (isValid()) {
                if (isEdit) {
                    editAddress();
                } else {
                    addAddress();
                }
            }
        });
    }

    private void addAddress() {
        progressDialog.show();
        AddAddressRequest addAddressRequest = new AddAddressRequest(
                preferenceManager.getString(Constants.KEY_USER_ID),
                "onetouch.com",
                Objects.requireNonNull(binding.tvName.getText()).toString(),
                Objects.requireNonNull(binding.tvEmail.getText()).toString(),
                Objects.requireNonNull(binding.tvPhone.getText()).toString(),
                Objects.requireNonNull(binding.tvAddress.getText()).toString(),
                Objects.requireNonNull(binding.tvLandmark.getText()).toString(),
                Objects.requireNonNull(binding.tvTownCity.getText()).toString(),
                Objects.requireNonNull(binding.tvPostalCode.getText()).toString(),
                Objects.requireNonNull(binding.tvStateCountry.getText()).toString(),
                null
        );

        viewModel.addAddress(addAddressRequest).observe(getViewLifecycleOwner(), addAddressResponse -> {
            if (addAddressResponse.code == 200) {
                progressDialog.dismiss();
                showToast("New address added successfully");
                Navigation.findNavController(binding.getRoot()).popBackStack();
            } else {
                progressDialog.dismiss();
                showToast(addAddressResponse.status);
            }
        });

    }

    private void editAddress() {
        progressDialog.show();
        AddAddressRequest addAddressRequest = new AddAddressRequest(
                preferenceManager.getString(Constants.KEY_USER_ID),
                "onetouch.com",
                Objects.requireNonNull(binding.tvName.getText()).toString(),
                Objects.requireNonNull(binding.tvEmail.getText()).toString(),
                Objects.requireNonNull(binding.tvPhone.getText()).toString(),
                Objects.requireNonNull(binding.tvAddress.getText()).toString(),
                Objects.requireNonNull(binding.tvLandmark.getText()).toString(),
                Objects.requireNonNull(binding.tvTownCity.getText()).toString(),
                Objects.requireNonNull(binding.tvPostalCode.getText()).toString(),
                Objects.requireNonNull(binding.tvStateCountry.getText()).toString(),
                id
        );

        viewModel.editAddress(addAddressRequest).observe(getViewLifecycleOwner(), addAddressResponse -> {
            if (addAddressResponse.code == 200) {
                progressDialog.dismiss();
                showToast("New address added successfully");
                Navigation.findNavController(binding.getRoot()).popBackStack();
            } else {
                progressDialog.dismiss();
                showToast(addAddressResponse.status);
            }
        });
    }

    private boolean isValid() {
        if (Objects.requireNonNull(binding.tvName.getText()).toString().isEmpty()) {
            showToast("Enter name");
            return false;
        } else if (Objects.requireNonNull(binding.tvPhone.getText()).toString().isEmpty()) {
            showToast("Enter phone number");
            return false;
        } else if (binding.tvPhone.getText().length() != 10) {
            showToast("Enter correct phone number");
            return false;
        } else  if (Objects.requireNonNull(binding.tvAddress.getText()).toString().isEmpty()) {
            showToast("Enter your address");
            return false;
        } else if (Objects.requireNonNull(binding.tvTownCity.getText()).toString().isEmpty()) {
            showToast("Enter your town/city");
            return false;
        } else if (Objects.requireNonNull(binding.tvStateCountry.getText()).toString().isEmpty()) {
            showToast("Enter your state");
            return false;
        } else if (Objects.requireNonNull(binding.tvPostalCode.getText()).toString().isEmpty()) {
            showToast("Enter your postal code");
            return false;
        } else {
            return true;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void showProgress() {
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}