package com.axepert.onetouch.ui.services;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterRelatedServiceProviders;
import com.axepert.onetouch.databinding.FragmentServiceDetailsBinding;
import com.axepert.onetouch.responses.RelatedServiceProvidersResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ServiceDetailsFragment extends Fragment {
    private ServiceDetailsViewModel mViewModel;
    private FragmentServiceDetailsBinding binding;
    private List<RelatedServiceProvidersResponse.Datum> relatedServiceProvidersList;
    private AdapterRelatedServiceProviders adapterRelatedServiceProviders;
    private String id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ServiceDetailsViewModel.class);
        binding = FragmentServiceDetailsBinding.inflate(getLayoutInflater(), container, false);
        serviceDetails();
        setListener();
        return binding.getRoot();
    }

    private void serviceDetails() {
        assert getArguments() != null;
        id = getArguments().getString("id");
        binding.tvServiceName.setText(String.format(
                "%s Services",
                getArguments().getString("category")
        ));
        Picasso.get().load(getArguments().getString("imageUrl")).noFade().into(binding.imgBanner);
        binding.tvDescription.setText(getArguments().getString("description"));
        binding.tvTitle.setText(String.format(
                "Related Service Providers (%s)",
                getArguments().getInt("count")
        ));
        initRV();
    }

    private void setListener() {
        binding.btnBookService.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("service_id", id);
            bundle.putString("service_name", getArguments().getString("category"));
            Navigation.findNavController(v).navigate(R.id.action_serviceDetailsFragment_to_bookServiceFragment2, bundle);
        });
        binding.imgBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
    }

    private void initRV() {
        binding.recyclerViewRelatedServiceProvider.setHasFixedSize(true);
        relatedServiceProvidersList = new ArrayList<>();
        adapterRelatedServiceProviders = new AdapterRelatedServiceProviders(relatedServiceProvidersList);
        binding.recyclerViewRelatedServiceProvider.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewRelatedServiceProvider.setAdapter(adapterRelatedServiceProviders);

        fetchRelatedProviders();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchRelatedProviders() {
        loading(true);
        mViewModel.getRelatedServiceProviders(id)
                .observe(requireActivity(), relatedServiceProvidersResponse -> {
                    if (relatedServiceProvidersResponse != null) {
                        if (relatedServiceProvidersResponse.code == 200) {
                            relatedServiceProvidersList.addAll(relatedServiceProvidersResponse.data);
                            adapterRelatedServiceProviders.notifyDataSetChanged();
                            loading(false);
                        } else {
                            loading(false);
                        }
                    } else {
                        loading(false);
                    }
                });
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            binding.scrollView.setVisibility(View.GONE);
            binding.loading.setVisibility(View.VISIBLE);
        } else {
            binding.scrollView.setVisibility(View.VISIBLE);
            binding.loading.setVisibility(View.GONE);
        }
    }

}