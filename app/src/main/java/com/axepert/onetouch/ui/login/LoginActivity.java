package com.axepert.onetouch.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.axepert.onetouch.MainActivity;
import com.axepert.onetouch.databinding.ActivityLoginBinding;
import com.axepert.onetouch.responses.LoginResponse;
import com.axepert.onetouch.ui.registration.RegistrationActivity;
import com.axepert.onetouch.utilities.Constants;
import com.axepert.onetouch.utilities.PreferenceManager;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        preferenceManager = new PreferenceManager(this);
        setListener();
    }

    private void setListener() {
        binding.btnLogin.setOnClickListener(v -> {
            if (isValid()) {
                if (binding.tabLayout.getSelectedTabPosition() == 0) {
                    userLogin();
                } else if (binding.tabLayout.getSelectedTabPosition() == 1){
                    login();
                }
            }
        });
        binding.tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

    }

    private void login() {
        viewModel.login(
                binding.tvEmail.getText().toString().trim(),
                binding.tvPassword.getText().toString().trim()
        ).observe(this, loginResponse -> {
            if (loginResponse != null) {
                if (loginResponse.code == 200) {
                    preferenceManager.putString(Constants.KEY_USER_ID, loginResponse.data.getId());
                    preferenceManager.putString(Constants.KEY_USERNAME, loginResponse.data.getFirst_name());
                    preferenceManager.putString(Constants.KEY_EMAIL, loginResponse.data.getEmail());
                    preferenceManager.putString(Constants.KEY_IMAGE, loginResponse.data.getImage());
                    preferenceManager.putString(Constants.KEY_ROLE, "dealer");
                    preferenceManager.putBoolean(Constants.KEY_IS_LOGIN, true);
                    LoginActivity.super.onBackPressed();
                } else if (loginResponse.code == 401) {
                    new AlertDialog.Builder(this)
                            .setTitle("Authentication Failed")
                            .setMessage("Your account is not verified yet by the admin team.")
                            .setPositiveButton("Close", (dialog, which) -> dialog.dismiss()).show();
                } else {
                    showToast(loginResponse.status);
                }
            } else {
                showToast("Unable to login right now.");
            }
        });
    }

    private void userLogin() {
        viewModel.userLogin(
                binding.tvEmail.getText().toString().trim(),
                binding.tvPassword.getText().toString().trim()
        ).observe(this, loginResponse -> {
            if (loginResponse != null) {
                if (loginResponse.code == 200) {
                    preferenceManager.putString(Constants.KEY_USER_ID, loginResponse.data.getId());
                    preferenceManager.putString(Constants.KEY_USERNAME, loginResponse.data.getFirst_name());
                    preferenceManager.putString(Constants.KEY_EMAIL, loginResponse.data.getEmail());
                    preferenceManager.putString(Constants.KEY_IMAGE, loginResponse.data.getImage());
                    preferenceManager.putString(Constants.KEY_PHONE, loginResponse.data.getContact());
                    preferenceManager.putString(Constants.KEY_ROLE, "user");
                    preferenceManager.putBoolean(Constants.KEY_IS_LOGIN, true);
                    LoginActivity.super.onBackPressed();
                } else if (loginResponse.code == 401) {
                    new AlertDialog.Builder(this)
                            .setTitle("Authentication Failed")
                            .setMessage("Your account is not verified yet by the admin team.")
                            .setPositiveButton("Close", (dialog, which) -> dialog.dismiss()).show();
                } else {
                    showToast(loginResponse.status);
                }
            } else {
                showToast("Unable to login right now.");
            }
        });
    }

    private Boolean isValid() {
        if (binding.tvEmail.getText().toString().isEmpty()) {
            showToast("Enter your email address");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.tvEmail.getText().toString().trim()).matches()) {
            showToast("Enter valid email address");
            return false;
        } else if (binding.tvPassword.getText().toString().isEmpty()) {
            showToast("Enter password");
            return false;
        } else {
            return true;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}