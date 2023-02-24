package com.axepert.onetouch;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.axepert.onetouch.databinding.ActivityMainBinding;
import com.axepert.onetouch.utilities.Constants;
import com.axepert.onetouch.utilities.PreferenceManager;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(this);

        setupNav();
    }

    private void setupNav() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
        changeMenu();

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.productDetailsFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.addressesFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.blogInfoFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.sellerDetailsFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.addAddressFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.myOrdersFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.cartFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.changePasswordFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.checkOutFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.productListFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.productByCategoryFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.serviceDetailsFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.bookServiceFragment2) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.llProfileSettings) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.searchFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.invoiceFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.addBlogFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else if (navDestination.getId() == R.id.myServicesFragment) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else {
                binding.bottomNavigation.setVisibility(View.VISIBLE);
            }
        });
    }

    public void changeMenu() {
        if (preferenceManager.getString(Constants.KEY_ROLE).equals("dealer")) {
            binding.bottomNavigation.getMenu().removeItem(R.id.nav_professional);
        }
    }

}