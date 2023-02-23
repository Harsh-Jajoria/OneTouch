package com.axepert.onetouch.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemProductsGridBinding;
import com.axepert.onetouch.listeners.ProductByCategoryListener;
import com.axepert.onetouch.responses.ProductByCatResponse;
import com.axepert.onetouch.responses.ProductListResponse;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterProductByCategory extends RecyclerView.Adapter<AdapterProductByCategory.ProductByCategoryViewHolder>{
    List<ProductByCatResponse.Datum> productList;
    ProductByCategoryListener productByCategoryListener;

    public AdapterProductByCategory(List<ProductByCatResponse.Datum> productList, ProductByCategoryListener productByCategoryListener) {
        this.productList = productList;
        this.productByCategoryListener = productByCategoryListener;
    }

    @NonNull
    @Override
    public ProductByCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductByCategoryViewHolder(
                ItemProductsGridBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ProductByCategoryViewHolder holder, int position) {
        holder.setData(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<ProductByCatResponse.Datum> products) {
        productList = products;
        notifyDataSetChanged();
    }

    class ProductByCategoryViewHolder extends RecyclerView.ViewHolder {
        ItemProductsGridBinding binding;

        ProductByCategoryViewHolder(ItemProductsGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(ProductByCatResponse.Datum product) {
            binding.tvCategory.setText(product.getCategory_name());
            binding.tvProductName.setText(product.getName());
            binding.tvOfferPrice.setText(amountFormat(product.getPrice()));
            Picasso.get().load(product.getProduct_image()).noFade().into(binding.imgProductImage);
            binding.getRoot().setOnClickListener(v -> productByCategoryListener.onProductClicked(product));
        }

        public String amountFormat(String amount) {
            Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
            return format.format(new BigDecimal(amount));
        }
    }
}
