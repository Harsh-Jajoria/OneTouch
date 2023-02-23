package com.axepert.onetouch.ui.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.FragmentChangePasswordBinding;
import com.axepert.onetouch.requests.ChangePasswordRequest;
import com.axepert.onetouch.responses.ChangePasswordResponse;
import com.axepert.onetouch.utilities.Constants;
import com.axepert.onetouch.utilities.PreferenceManager;

import java.util.Objects;

public class ChangePasswordFragment extends Fragment {
    private FragmentChangePasswordBinding binding;
    private ChangePasswordViewModel changePasswordViewModel;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(getLayoutInflater(), container, false);
        changePasswordViewModel = new ViewModelProvider(this).get(ChangePasswordViewModel.class);
        preferenceManager = new PreferenceManager(requireActivity());
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        binding.btnChangePassword.setOnClickListener(v -> {
            if (isValid()) {
                changePassword();
            }
        });
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());
    }

    private void changePassword() {
        loading(true);
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(
                preferenceManager.getString(Constants.KEY_EMAIL),
                Objects.requireNonNull(binding.tvCurrentPassword.getText()).toString().trim(),
                Objects.requireNonNull(binding.tvNewPassword.getText()).toString().trim());

        changePasswordViewModel.changePassword(changePasswordRequest).observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                if (response.code == 200) {
                    loading(false);
                    showToast("Password updated successfully");
                    Navigation.findNavController(binding.getRoot()).popBackStack();
                } else if (response.code == 400) {
                    loading(false);
                    showToast(response.status);
                } else {
                    loading(false);
                    showToast(response.status);
                }
            } else {
                loading(false);
                showToast("Check your internet connection");
            }
        });
    }

    private Boolean isValid() {
        if (Objects.requireNonNull(binding.tvCurrentPassword.getText()).toString().isEmpty()) {
            showToast("Enter current password");
            return false;
        } else if (Objects.requireNonNull(binding.tvNewPassword.getText()).toString().isEmpty()) {
            showToast("Enter new password");
            return false;
        } else if (Objects.requireNonNull(binding.tvConfirmPassword.getText()).toString().isEmpty()) {
            showToast("Confirm your password");
            return false;
        } else if (!binding.tvNewPassword.getText().toString().matches(binding.tvConfirmPassword.getText().toString())) {
            showToast("New password and Confirm password must be same.");
            return false;
        } else {
            return true;
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.btnChangePassword.setVisibility(View.INVISIBLE);
            binding.progress.setVisibility(View.VISIBLE);
        } else {
            binding.progress.setVisibility(View.INVISIBLE);
            binding.btnChangePassword.setVisibility(View.VISIBLE);
        }
    }

}