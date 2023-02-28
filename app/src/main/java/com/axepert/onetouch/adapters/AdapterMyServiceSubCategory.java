package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemMyServiceSubCateogoryBinding;
import com.axepert.onetouch.responses.MyServiceSubCategoryResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMyServiceSubCategory extends RecyclerView.Adapter<AdapterMyServiceSubCategory.MyServiceSubCategoryViewHolder>{
    List<MyServiceSubCategoryResponse.Datum> subCatList;

    public AdapterMyServiceSubCategory(List<MyServiceSubCategoryResponse.Datum> subCatList) {
        this.subCatList = subCatList;
    }

    @NonNull
    @Override
    public MyServiceSubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyServiceSubCategoryViewHolder(
                ItemMyServiceSubCateogoryBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MyServiceSubCategoryViewHolder holder, int position) {
        holder.setData(subCatList.get(position));
    }

    @Override
    public int getItemCount() {
        return subCatList.size();
    }

    static class MyServiceSubCategoryViewHolder extends RecyclerView.ViewHolder {
        ItemMyServiceSubCateogoryBinding binding;

        MyServiceSubCategoryViewHolder(ItemMyServiceSubCateogoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(MyServiceSubCategoryResponse.Datum subCat) {
            binding.tvSubCatName.setText(subCat.getName());
            Picasso.get().load(subCat.getImage()).into(binding.imgSubCategory);
            Picasso.get().load(subCat.getCat_data().getCat_image()).into(binding.imgCategoryIcon);
        }
    }

}
