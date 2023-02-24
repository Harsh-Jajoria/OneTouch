package com.axepert.onetouch.ui.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.axepert.onetouch.MainActivity;
import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterDashboard;
import com.axepert.onetouch.databinding.FragmentAccountBinding;
import com.axepert.onetouch.models.DashboardData;
import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.DashboradRequest;
import com.axepert.onetouch.responses.DashboardResponse;
import com.axepert.onetouch.ui.login.LoginActivity;
import com.axepert.onetouch.utilities.Constants;
import com.axepert.onetouch.utilities.PreferenceManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    private FragmentAccountBinding binding;
    private List<DashboardData> dashboardList;
    private AdapterDashboard adapterDashboard;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(getLayoutInflater(), container, false);
        preferenceManager = new PreferenceManager(requireActivity());
        setListener();
        if (!preferenceManager.getBoolean(Constants.KEY_IS_LOGIN)) {
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            Navigation.findNavController(container).popBackStack();
        } else {
            profileData();
            dashboardContent();
        }
        showingDataAccordingUserType();
        return binding.getRoot();
    }

    private void setListener() {
        binding.llLogout.setOnClickListener(v -> {
            preferenceManager.clear();
            Navigation.findNavController(v).popBackStack();
        });
        binding.llAddress.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_account_to_addressesFragment);
        });
        binding.llMyServices.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_account_to_myServicesFragment);
        });
        binding.llOrders.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_account_to_myOrdersFragment);
        });
        binding.llChangePassword.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_account_to_changePasswordFragment);
        });
        binding.llProfileSettings.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_account_to_editProfileFragment);
        });
    }

    private void showingDataAccordingUserType() {
        if (preferenceManager.getString(Constants.KEY_ROLE).equals("user")) {
            binding.recyclerViewDashboard.setVisibility(View.GONE);
            binding.llMyServices.setVisibility(View.GONE);
            binding.llReviews.setVisibility(View.GONE);
        } else {
            binding.recyclerViewDashboard.setVisibility(View.VISIBLE);
            binding.llMyServices.setVisibility(View.VISIBLE);
            binding.llReviews.setVisibility(View.VISIBLE);
            binding.llOrders.setVisibility(View.GONE);
            binding.llAddress.setVisibility(View.GONE);
        }
    }

    public void profileData() {
        binding.tvUsername.setText(preferenceManager.getString(Constants.KEY_USERNAME));
        binding.tvEmail.setText(preferenceManager.getString(Constants.KEY_EMAIL));
        if (!preferenceManager.getString(Constants.KEY_IMAGE).isEmpty()) {
            binding.profileImage.setAlpha(0f);
            Picasso.get().load(preferenceManager.getString(Constants.KEY_IMAGE)).into(binding.profileImage, new Callback() {
                @Override
                public void onSuccess() {
                    binding.profileImage.animate().alpha(1f).setDuration(300).start();
                }

                @Override
                public void onError(Exception e) {
                    binding.profileImage.animate().alpha(1f).setDuration(300).start();
                    binding.profileImage.setImageResource(R.drawable.ic_launcher_background);
                }
            });
        }
    }

    private void dashboardContent() {
        binding.recyclerViewDashboard.setHasFixedSize(true);
        dashboardList = new ArrayList<>();
        adapterDashboard = new AdapterDashboard(dashboardList, requireActivity());
        binding.recyclerViewDashboard.setAdapter(adapterDashboard);

        DashboradRequest dashboradRequest = new DashboradRequest(preferenceManager.getString(Constants.KEY_USER_ID));
        ApiClient.getRetrofit().create(ApiService.class).dashboard(dashboradRequest).enqueue(new retrofit2.Callback<DashboardResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<DashboardResponse> call, @NonNull Response<DashboardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().code == 200) {
                        dashboardList.add(new DashboardData(R.color.yellow, "Category", response.body().data.getCategory_count()));
                        dashboardList.add(new DashboardData(R.color.primary_text, "Sub Category", response.body().data.getSubcategory_count()));
                        dashboardList.add(new DashboardData(R.color.blue, "Reviews", response.body().data.getReview_count()));
                        adapterDashboard.notifyDataSetChanged();
                    } else {
                        Toast.makeText(requireActivity(), response.body().status, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireActivity(), "Response failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DashboardResponse> call, @NonNull Throwable t) {
                Toast.makeText(requireActivity(), "Failed dut to : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}