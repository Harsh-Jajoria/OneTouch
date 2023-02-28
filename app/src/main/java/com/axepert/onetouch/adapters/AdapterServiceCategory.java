package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemDropdownBinding;
import com.axepert.onetouch.listeners.ServiceCategoryListener;
import com.axepert.onetouch.responses.MyServiceCategoryResponse;

import java.util.List;

public class AdapterServiceCategory extends RecyclerView.Adapter<AdapterServiceCategory.ServiceCategoryViewHolder>{
    private List<MyServiceCategoryResponse.Datum> serviceCategory;
    private ServiceCategoryListener serviceCategoryListener;

    public AdapterServiceCategory(List<MyServiceCategoryResponse.Datum> serviceCategory, ServiceCategoryListener serviceCategoryListener) {
        this.serviceCategory = serviceCategory;
        this.serviceCategoryListener = serviceCategoryListener;
    }

    @NonNull
    @Override
    public ServiceCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceCategoryViewHolder(
                ItemDropdownBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceCategoryViewHolder holder, int position) {
        holder.setData(serviceCategory.get(position));
    }

    @Override
    public int getItemCount() {
        return serviceCategory.size();
    }

    class ServiceCategoryViewHolder extends RecyclerView.ViewHolder {
        ItemDropdownBinding binding;

        ServiceCategoryViewHolder(ItemDropdownBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(MyServiceCategoryResponse.Datum data) {
            binding.tvDropDownItem.setText(data.getName());
            binding.tvDropDownItem.setOnClickListener(v -> serviceCategoryListener.onClick(data));
        }
    }

}
