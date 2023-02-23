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
import com.axepert.onetouch.databinding.FragmentEditProfileBinding;
import com.axepert.onetouch.requests.EditProfileRequest;
import com.axepert.onetouch.responses.EditProfileResponse;
import com.axepert.onetouch.utilities.Constants;
import com.axepert.onetouch.utilities.PreferenceManager;

import io.reactivex.Completable;

public class EditProfileFragment extends Fragment {
    private FragmentEditProfileBinding binding;
    private EditProfileViewModel viewModel;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
        preferenceManager = new PreferenceManager(requireActivity());
        binding.etName.setText(preferenceManager.getString(Constants.KEY_USERNAME));
        binding.etContact.setText(preferenceManager.getString(Constants.KEY_PHONE));
        if (preferenceManager.getString(Constants.KEY_ROLE).equals("user")) {
            binding.llDealer.setVisibility(View.GONE);
        } else {
            binding.llDealer.setVisibility(View.VISIBLE);
        }
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        binding.btnUpdate.setOnClickListener(v -> {
            if (preferenceManager.getString(Constants.KEY_ROLE).equals("user")) {
                editUserProfile();
            } else {
                Toast.makeText(requireActivity(), "Dealer profile edit in development", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editUserProfile() {
        EditProfileRequest editProfileRequest = new EditProfileRequest(
                "onetouch.com",
                binding.etName.getText().toString(),
                binding.etContact.getText().toString(),
                preferenceManager.getString(Constants.KEY_USER_ID));

        viewModel.editUserProfile(editProfileRequest).observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                if (response.code == 200) {
                    Toast.makeText(requireActivity(), response.status, Toast.LENGTH_SHORT).show();
                    preferenceManager.putString(Constants.KEY_USERNAME, binding.etName.getText().toString());
                    preferenceManager.putString(Constants.KEY_PHONE, binding.etContact.getText().toString());
                    Navigation.findNavController(binding.getRoot()).popBackStack();
                } else {
                    Toast.makeText(requireActivity(), response.status, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireActivity(), "Something went wrong! Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

}