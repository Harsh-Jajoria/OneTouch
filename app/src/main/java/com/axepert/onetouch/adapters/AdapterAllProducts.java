package com.axepert.onetouch.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemProductsGridBinding;
import com.axepert.onetouch.listeners.ProductListListener;
import com.axepert.onetouch.responses.ProductListResponse;
import com.axepert.onetouch.responses.SellerInformation;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterAllProducts extends RecyclerView.Adapter<AdapterAllProducts.AllProductViewHolder> {
    List<ProductListResponse.Datum> productList;
    ProductListListener productListListener;

    public AdapterAllProducts(List<ProductListResponse.Datum> productList, ProductListListener productListListener) {
        this.productList = productList;
        this.productListListener = productListListener;
    }

    @NonNull
    @Override
    public AllProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllProductViewHolder(
                ItemProductsGridBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AllProductViewHolder holder, int position) {
        holder.setData(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<ProductListResponse.Datum> products) {
        productList = products;
        notifyDataSetChanged();
    }

    class AllProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductsGridBinding binding;

        AllProductViewHolder(ItemProductsGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(ProductListResponse.Datum product) {
            binding.tvCategory.setText(product.getCategory_name());
            binding.tvProductName.setText(product.getName());
            binding.tvOfferPrice.setText(amountFormat(product.getPrice()));
            Picasso.get().load(product.getProduct_image()).noFade().into(binding.imgProductImage);
            binding.getRoot().setOnClickListener(v -> productListListener.onProductClicked(product));
        }

        public String amountFormat(String amount) {
            Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
            return format.format(new BigDecimal(amount));
        }

    }

}
