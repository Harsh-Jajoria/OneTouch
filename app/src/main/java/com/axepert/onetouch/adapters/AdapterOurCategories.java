package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.ItemOurCategoryBinding;
import com.axepert.onetouch.listeners.OurCategoryListener;
import com.axepert.onetouch.responses.Category;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterOurCategories extends RecyclerView.Adapter<AdapterOurCategories.OurCategoryViewHolder> {
    List<Category> categoryList;
    OurCategoryListener ourCategoryListener;

    public AdapterOurCategories(List<Category> categoryList, OurCategoryListener ourCategoryListener) {
        this.categoryList = categoryList;
        this.ourCategoryListener = ourCategoryListener;
    }

    @NonNull
    @Override
    public OurCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OurCategoryViewHolder(
                ItemOurCategoryBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OurCategoryViewHolder holder, int position) {
        holder.setData(categoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class OurCategoryViewHolder extends RecyclerView.ViewHolder {
        ItemOurCategoryBinding binding;

        OurCategoryViewHolder(ItemOurCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Category category) {
            binding.getRoot().setOnClickListener(v -> ourCategoryListener.onOurCategoryClicked(category));
            binding.tvCategoryName.setText(category.getCategory_name());
            binding.tvCategoryCount.setText(String.format("%s Products", category.getProduct_count()));
            binding.imgServices.setAlpha(0f);
            Picasso.get().load(category.getCategory_icon()).into(binding.imgServices, new Callback() {
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
        }
    }
}
