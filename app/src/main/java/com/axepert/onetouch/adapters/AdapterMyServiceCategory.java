package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemMyServiceCateogoryBinding;
import com.axepert.onetouch.responses.MyServiceCategoryResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMyServiceCategory extends RecyclerView.Adapter<AdapterMyServiceCategory.MyServiceCategoryViewHolder>{
    List<MyServiceCategoryResponse.Datum> myServiceData;

    public AdapterMyServiceCategory(List<MyServiceCategoryResponse.Datum> myServiceData) {
        this.myServiceData = myServiceData;
    }

    @NonNull
    @Override
    public MyServiceCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyServiceCategoryViewHolder(
                ItemMyServiceCateogoryBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MyServiceCategoryViewHolder holder, int position) {
        holder.setData(myServiceData.get(position));
    }

    @Override
    public int getItemCount() {
        return myServiceData.size();
    }

    static class MyServiceCategoryViewHolder extends RecyclerView.ViewHolder {
        ItemMyServiceCateogoryBinding binding;

        MyServiceCategoryViewHolder(ItemMyServiceCateogoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(MyServiceCategoryResponse.Datum myServiceCat) {
            binding.tvMyServiceCategoryName.setText(myServiceCat.getName());
            Picasso.get().load(myServiceCat.getImage()).into(binding.imgServiceCategory);
        }
    }
}
