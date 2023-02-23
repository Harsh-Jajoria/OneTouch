package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.ItemBannersBinding;
import com.axepert.onetouch.responses.ProductImage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProductImages extends RecyclerView.Adapter<AdapterProductImages.ProductImageViewHolder>{
    List<ProductImage> productImageList;

    public AdapterProductImages(List<ProductImage> productImageList) {
        this.productImageList = productImageList;
    }

    @NonNull
    @Override
    public ProductImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductImageViewHolder(
                ItemBannersBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ProductImageViewHolder holder, int position) {
        holder.setData(productImageList.get(position));
    }

    @Override
    public int getItemCount() {
        return productImageList.size();
    }


    static class ProductImageViewHolder extends RecyclerView.ViewHolder {
        ItemBannersBinding binding;

        ProductImageViewHolder(ItemBannersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(ProductImage productImage) {
            binding.imgBanners.setAlpha(0f);
            Picasso.get().load(productImage.getProduct_image()).noFade().into(binding.imgBanners, new Callback() {
                @Override
                public void onSuccess() {
                    binding.imgBanners.animate().alpha(1f).setDuration(300).start();
                }

                @Override
                public void onError(Exception e) {
                    binding.imgBanners.setImageResource(R.drawable.ic_launcher_background);
                    binding.imgBanners.animate().alpha(1f).setDuration(300).start();
                }
            });
        }

    }

}
