package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemProductaBinding;
import com.axepert.onetouch.listeners.TrendingProductListener;
import com.axepert.onetouch.responses.TrendingProduct;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterTrendingProducts extends RecyclerView.Adapter<AdapterTrendingProducts.TrendingProductViewHolder>{
    List<TrendingProduct> trendingProductList;
    TrendingProductListener trendingProductListener;

    public AdapterTrendingProducts(List<TrendingProduct> trendingProductList, TrendingProductListener trendingProductListener) {
        this.trendingProductList = trendingProductList;
        this.trendingProductListener = trendingProductListener;
    }

    @NonNull
    @Override
    public TrendingProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrendingProductViewHolder(
                ItemProductaBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingProductViewHolder holder, int position) {
        holder.setData(trendingProductList.get(position));
    }

    @Override
    public int getItemCount() {
        return trendingProductList.size();
    }

    class TrendingProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductaBinding binding;

        TrendingProductViewHolder(ItemProductaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(TrendingProduct trendingProduct) {
            binding.tvCategory.setText(trendingProduct.getCategory_name());
            binding.tvProductName.setText(trendingProduct.getName());
            binding.tvOfferPrice.setText(amountFormat(trendingProduct.getPrice()));
            binding.tvMRP.setText(amountFormat(trendingProduct.getMrp()));
            Picasso.get().load(trendingProduct.getProduct_image()).noFade().into(binding.imgProductImage);
            binding.getRoot().setOnClickListener(v -> trendingProductListener.onTrendingProductClicked(trendingProduct));
        }

        public String amountFormat(String amount) {
            Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
            return format.format(new BigDecimal(amount));
        }

    }

}
