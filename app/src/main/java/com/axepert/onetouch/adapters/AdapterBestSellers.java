package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.ItemServiceProvidersBinding;
import com.axepert.onetouch.listeners.BestSellerListener;
import com.axepert.onetouch.responses.SellerInformation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterBestSellers extends RecyclerView.Adapter<AdapterBestSellers.BestSellerViewHolder>{
    private List<SellerInformation> sellerInformationList;
    private BestSellerListener bestSellerListener;

    public AdapterBestSellers(List<SellerInformation> sellerInformationList, BestSellerListener bestSellerListener) {
        this.sellerInformationList = sellerInformationList;
        this.bestSellerListener = bestSellerListener;
    }

    @NonNull
    @Override
    public BestSellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BestSellerViewHolder(
                ItemServiceProvidersBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellerViewHolder holder, int position) {
        holder.setData(sellerInformationList.get(position));
    }

    @Override
    public int getItemCount() {
        return sellerInformationList.size();
    }

    class BestSellerViewHolder extends RecyclerView.ViewHolder {
        ItemServiceProvidersBinding binding;

        BestSellerViewHolder(ItemServiceProvidersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(SellerInformation sellerInformation) {
            binding.tvServiceCategory.setVisibility(View.GONE);
            binding.tvProviderName.setText(sellerInformation.getName());
            binding.tvPhone.setText(replaceLastSix(sellerInformation.getContact()));
            binding.tvLocation.setText(sellerInformation.getShop_address());
            binding.imgServices.setAlpha(0f);
            Picasso.get().load(sellerInformation.getImage()).noFade().into(binding.imgServices, new Callback() {
                @Override
                public void onSuccess() {
                    binding.imgServices.animate().alpha(1f).setDuration(300).start();
                }

                @Override
                public void onError(Exception e) {
                    binding.imgServices.animate().alpha(1f).setDuration(300).start();
                    binding.imgServices.setImageResource(R.drawable.ic_launcher_background);
                }
            });
            binding.getRoot().setOnClickListener(v -> bestSellerListener.onSellerClick(sellerInformation));
        }

        public String replaceLastSix(String s) {
            int length = s.length();
            if (length < 4) return "Error: The provided string is not greater than four characters long.";
            return s.substring(0, length - 6) + "XXXXXX";
        }

    }

}
