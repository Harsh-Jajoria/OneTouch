package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemBannersBinding;
import com.axepert.onetouch.responses.Banners;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterBanners extends RecyclerView.Adapter<AdapterBanners.BannersViewHolder>{
    private List<Banners> bannersList;

    public AdapterBanners(List<Banners> bannersList) {
        this.bannersList = bannersList;
    }

    @NonNull
    @Override
    public BannersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BannersViewHolder(
                ItemBannersBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BannersViewHolder holder, int position) {
        holder.setData(bannersList.get(position));
    }

    @Override
    public int getItemCount() {
        return bannersList.size();
    }

    static class BannersViewHolder extends RecyclerView.ViewHolder {
        ItemBannersBinding binding;

        BannersViewHolder(ItemBannersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Banners banners) {
            binding.tvTitle.setText(banners.getTitle());
            binding.tvSubTitle.setText(banners.getSubtitle());
            binding.imgBanners.setAlpha(0f);
            Picasso.get().load(banners.getImage()).noFade().into(binding.imgBanners, new Callback() {
                @Override
                public void onSuccess() {
                    binding.imgBanners.animate().alpha(1f).setDuration(300).start();
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }
}
