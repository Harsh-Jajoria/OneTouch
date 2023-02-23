package com.axepert.onetouch.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemServicesGridBinding;
import com.axepert.onetouch.listeners.ServicesListener;
import com.axepert.onetouch.responses.Services;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAllServices extends RecyclerView.Adapter<AdapterAllServices.AllServicesViewHolder>{
    private List<Services> servicesList;
    private ServicesListener servicesListener;

    public AdapterAllServices(List<Services> servicesList, ServicesListener servicesListener) {
        this.servicesList = servicesList;
        this.servicesListener = servicesListener;
    }

    @NonNull
    @Override
    public AllServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllServicesViewHolder(
                ItemServicesGridBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AllServicesViewHolder holder, int position) {
        holder.setData(servicesList.get(position));
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<Services> services) {
        servicesList = services;
        notifyDataSetChanged();
    }

    class AllServicesViewHolder extends RecyclerView.ViewHolder {
        ItemServicesGridBinding binding;

        AllServicesViewHolder(ItemServicesGridBinding binding) {
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
