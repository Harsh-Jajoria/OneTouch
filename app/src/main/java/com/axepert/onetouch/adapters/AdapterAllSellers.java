package com.axepert.onetouch.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.ItemServiceProviderGridBinding;
import com.axepert.onetouch.listeners.BestSellerListener;
import com.axepert.onetouch.responses.SellerInformation;
import com.axepert.onetouch.responses.Services;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAllSellers extends RecyclerView.Adapter<AdapterAllSellers.AllSellersViewHolder>{
    private List<SellerInformation> sellerInformationList;
    private BestSellerListener bestSellerListener;

    public AdapterAllSellers(List<SellerInformation> sellerInformationList, BestSellerListener bestSellerListener) {
        this.sellerInformationList = sellerInformationList;
        this.bestSellerListener = bestSellerListener;
    }

    @NonNull
    @Override
    public AllSellersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllSellersViewHolder(
                ItemServiceProviderGridBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AllSellersViewHolder holder, int position) {
        holder.setData(sellerInformationList.get(position));
    }

    @Override
    public int getItemCount() {
        return sellerInformationList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<SellerInformation> sellers) {
        sellerInformationList = sellers;
        notifyDataSetChanged();
    }

    class AllSellersViewHolder extends RecyclerView.ViewHolder {
        ItemServiceProviderGridBinding binding;

        AllSellersViewHolder(ItemServiceProviderGridBinding binding) {
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
