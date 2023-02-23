package com.axepert.onetouch.ui.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.ActivityRegistrationBinding;
import com.axepert.onetouch.requests.RegisterRequest;
import com.axepert.onetouch.responses.RegistrationResponse;
import com.axepert.onetouch.utilities.Constants;
import com.axepert.onetouch.utilities.PreferenceManager;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {
    private ActivityRegistrationBinding binding;
    private RegistrationViewModel registrationViewModel;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(this);
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        setListener();
    }

    private void setListener() {
        binding.tvLogin.setOnClickListener(v -> onBackPressed());
        binding.btnLogin.setOnClickListener(v -> {
            if (isValid()) {
                register();
            }
        });
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    binding.tvTitle.setText(getString(R.string.welcome_to_onetouch));
                    binding.tilPhone.setVisibility(View.GONE);
                    binding.tvPhone.setText(null);
                } else if (tab.getPosition() == 1) {
                    binding.tvTitle.setText(getString(R.string.join_as_a_provider));
                    binding.tilPhone.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.btnLogin.setVisibility(View.GONE);
            binding.progress.setVisibility(View.VISIBLE);
        } else {
            binding.btnLogin.setVisibility(View.VISIBLE);
            binding.progress.setVisibility(View.GONE);
        }
    }

    private void register() {
        loading(true);
        RegisterRequest registerRequest = new RegisterRequest(
                binding.tvName.getText().toString().trim(),
                binding.tvEmail.getText().toString().trim(),
                binding.tvPhone.getText().toString().trim(),
                binding.tvPassword.getText().toString().trim(),
                binding.tvConfirmPassword.getText().toString().trim());

        registrationViewModel.register(registerRequest).observe(this, registrationResponse -> {
            if (registrationResponse != null) {
                if (registrationResponse.code == 200) {
                    loading(false);
                    new AlertDialog.Builder(this)
                            .setTitle("Successfully Registered")
                            .setMessage("You account is successfully created and currently is under verification.")
                            .setPositiveButton("Okay", (dialog, which) -> {
                                RegistrationActivity.super.onBackPressed();
                                dialog.dismiss();
                            }).show();
                } else {
                    loading(false);
                    Toast.makeText(this, registrationResponse.status, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void userRegister() {
        loading(true);
        RegisterRequest registerRequest = new RegisterRequest(
                binding.tvName.getText().toString().trim(),
                binding.tvEmail.getText().toString().trim(), null,
                binding.tvPassword.getText().toString().trim(),
                binding.tvConfirmPassword.getText().toString().trim());

        registrationViewModel.userRegistration(registerRequest).observe(this, response -> {
            if (response != null) {
                if (response.code == 200) {
                    preferenceManager.putString(Constants.KEY_ROLE, "user");
                    preferenceManager.putBoolean(Constants.KEY_IS_LOGIN, true);
                } else {
                    loading(false);
                }
            } else {
                loading(false);
            }
        });

    }

    private boolean isValid() {
        if (binding.tvName.getText().toString().isEmpty()) {
            showToast("Enter your name");
            return false;
        } else if (binding.tvPhone.getText().toString().isEmpty()) {
            showToast("Enter your mobile number");
            return false;
        } else if (binding.tvPhone.getText().toString().length() != 10) {
            showToast("Mobile number should be 10 digits");
            return false;
        } else if (binding.tvEmail.getText().toString().isEmpty()) {
            showToast("Enter your email address");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.tvEmail.getText().toString()).matches()) {
            showToast("Email address is not valid");
            return false;
        } else if (binding.tvPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter your password");
            return false;
        } else if (binding.tvConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Confirm your password");
            return false;
        } else if (!binding.tvConfirmPassword.getText().toString().equals(binding.tvPassword.getText().toString().trim())) {
            showToast("Password and Confirm password not same");
            return false;
        } else {
            return true;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}