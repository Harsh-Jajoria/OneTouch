package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemProductaBinding;
import com.axepert.onetouch.listeners.PopularProductListener;
import com.axepert.onetouch.responses.PopularProduct;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterPopularProducts extends RecyclerView.Adapter<AdapterPopularProducts.PopularProductViewHolder> {
    List<PopularProduct> popularProductList;
    PopularProductListener popularProductListener;

    public AdapterPopularProducts(List<PopularProduct> popularProductList, PopularProductListener popularProductListener) {
        this.popularProductList = popularProductList;
        this.popularProductListener = popularProductListener;
    }

    @NonNull
    @Override
    public PopularProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopularProductViewHolder(
                ItemProductaBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PopularProductViewHolder holder, int position) {
        holder.setData(popularProductList.get(position));
    }

    @Override
    public int getItemCount() {
        return popularProductList.size();
    }

    class PopularProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductaBinding binding;

        PopularProductViewHolder(ItemProductaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(PopularProduct popularProduct) {
            binding.tvCategory.setText(popularProduct.getCategory_name());
            binding.tvProductName.setText(popularProduct.getName());
            binding.tvOfferPrice.setText(amountFormat(popularProduct.getPrice()));
            Picasso.get().load(popularProduct.getProduct_image()).noFade().into(binding.imgProductImage);
            binding.getRoot().setOnClickListener(v -> popularProductListener.onPopularProductClicked(popularProduct));
        }

        public String amountFormat(String amount) {
            Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
            return format.format(new BigDecimal(amount));
        }

    }

}
