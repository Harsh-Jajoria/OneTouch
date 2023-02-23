package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemMostPopularServicesBinding;
import com.axepert.onetouch.listeners.ServicesListener;
import com.axepert.onetouch.responses.Services;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterMostPopularServices extends RecyclerView.Adapter<AdapterMostPopularServices.PopularServiceViewHolder> {
    private List<Services> servicesList;
    private ServicesListener servicesListener;

    public AdapterMostPopularServices(List<Services> servicesList, ServicesListener servicesListener) {
        this.servicesList = servicesList;
        this.servicesListener = servicesListener;
    }

    @NonNull
    @Override
    public PopularServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopularServiceViewHolder(
                ItemMostPopularServicesBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PopularServiceViewHolder holder, int position) {
        holder.setData(servicesList.get(position));
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    class PopularServiceViewHolder extends RecyclerView.ViewHolder {
        ItemMostPopularServicesBinding binding;

        PopularServiceViewHolder(ItemMostPopularServicesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Services services) {
            binding.tvServiceName.setText(String.format("%s %s", services.getService_type(), "Services"));
            binding.tvServiceCategory.setText(services.getDescription());
            binding.tvServiceProviderCount.setText(String.format("%s %s", services.getProvider_count(), "Providers"));
            Picasso.get().load(services.getServices_image()).into(binding.imgServices);
            binding.getRoot().setOnClickListener(v -> servicesListener.onClick(services));
        }

    }

}
