package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.ItemServiceProvidersBinding;
import com.axepert.onetouch.responses.RelatedServiceProvidersResponse;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterRelatedServiceProviders extends RecyclerView.Adapter<AdapterRelatedServiceProviders.RelatedServiceProvidersViewHolder>{
    List<RelatedServiceProvidersResponse.Datum> relatedServiceProvidersList;

    public AdapterRelatedServiceProviders(List<RelatedServiceProvidersResponse.Datum> relatedServiceProvidersList) {
        this.relatedServiceProvidersList = relatedServiceProvidersList;
    }

    @NonNull
    @Override
    public RelatedServiceProvidersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RelatedServiceProvidersViewHolder(
                ItemServiceProvidersBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedServiceProvidersViewHolder holder, int position) {
        holder.setData(relatedServiceProvidersList.get(position));
    }

    @Override
    public int getItemCount() {
        return relatedServiceProvidersList.size();
    }

    static class RelatedServiceProvidersViewHolder extends RecyclerView.ViewHolder {
        ItemServiceProvidersBinding binding;

        RelatedServiceProvidersViewHolder(ItemServiceProvidersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(RelatedServiceProvidersResponse.Datum response) {
            binding.tvProviderName.setText(response.getName());
            binding.tvLocation.setText(response.getAddress());
            binding.tvPhone.setText(response.getMobile());
            binding.tvServiceCategory.setVisibility(View.GONE);
            binding.imgServices.setAlpha(0f);
            Picasso.get().load(response.getMan_image()).noFade().into(binding.imgServices, new Callback() {
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
        }
    }
}
