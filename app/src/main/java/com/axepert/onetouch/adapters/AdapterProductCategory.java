package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemCategoryBinding;
import com.axepert.onetouch.listeners.ProductCategoryListener;
import com.axepert.onetouch.responses.ProductCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProductCategory extends RecyclerView.Adapter<AdapterProductCategory.ProductCategoryViewHolder>{
    List<ProductCategory> productCategoryList;
    ProductCategoryListener productCategoryListener;

    public AdapterProductCategory(List<ProductCategory> productCategoryList, ProductCategoryListener productCategoryListener) {
        this.productCategoryList = productCategoryList;
        this.productCategoryListener = productCategoryListener;
    }

    @NonNull
    @Override
    public ProductCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductCategoryViewHolder(
                ItemCategoryBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoryViewHolder holder, int position) {
        holder.setData(productCategoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return productCategoryList.size();
    }

    class ProductCategoryViewHolder extends RecyclerView.ViewHolder{
        ItemCategoryBinding binding;

        ProductCategoryViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(ProductCategory productCategory) {
            Picasso.get().load(productCategory.getCategory_icon()).into(binding.imgCategory);
            binding.tvCategory.setText(productCategory.getCategory_name());
            binding.getRoot().setOnClickListener(v -> productCategoryListener.onCategoryClicked(productCategory));
        }
    }
}
