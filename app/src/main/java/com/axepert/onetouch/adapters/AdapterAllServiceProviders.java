package com.axepert.onetouch.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.ItemServiceProviderGridBinding;
import com.axepert.onetouch.responses.ServiceProviders;
import com.axepert.onetouch.responses.Services;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAllServiceProviders extends RecyclerView.Adapter<AdapterAllServiceProviders.AllServiceProvidersViewHolder>{
    private List<ServiceProviders> serviceProvidersList;

    public AdapterAllServiceProviders(List<ServiceProviders> serviceProvidersList) {
        this.serviceProvidersList = serviceProvidersList;
    }

    @NonNull
    @Override
    public AllServiceProvidersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllServiceProvidersViewHolder(
                ItemServiceProviderGridBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AllServiceProvidersViewHolder holder, int position) {
        holder.setData(serviceProvidersList.get(position));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<ServiceProviders> serviceProviders) {
        serviceProvidersList = serviceProviders;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return serviceProvidersList.size();
    }

    static class AllServiceProvidersViewHolder extends RecyclerView.ViewHolder {
        ItemServiceProviderGridBinding binding;

        AllServiceProvidersViewHolder(ItemServiceProviderGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(ServiceProviders providers) {
            binding.imgServices.setAlpha(0f);
            Picasso.get().load(providers.getMan_image()).noFade().into(binding.imgServices, new Callback() {
                @Override
                public void onSuccess() {
                    binding.imgServices.animate().alpha(1f).setDuration(300).start();
                }

                @Override
                public void onError(Exception e) {
                    binding.imgServices.setImageResource(R.drawable.ic_launcher_background);
                    binding.imgServices.animate().alpha(1f).setDuration(300).start();
                }
            });
            binding.tvProviderName.setText(providers.getName());
            binding.tvLocation.setText(providers.getAddress());
            binding.tvPhone.setText(replaceLastSix(providers.getMobile()));
            binding.tvServiceCategory.setText(providers.getService_name());
        }
    }

    public static String replaceLastSix(String s) {
        int length = s.length();
        if (length < 4)
            return "Error: The provided string is not greater than four characters long.";
        return s.substring(0, length - 6) + "XXXXXX";
    }

}

