package com.axepert.onetouch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.ItemDashboardCardsBinding;
import com.axepert.onetouch.models.DashboardData;

import java.util.List;

public class AdapterDashboard extends RecyclerView.Adapter<AdapterDashboard.DashboardViewHolder> {
    List<DashboardData> dashboardDataList;
    Context context;

    public AdapterDashboard(List<DashboardData> dashboardDataList, Context context) {
        this.dashboardDataList = dashboardDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DashboardViewHolder(
                ItemDashboardCardsBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
        holder.setData(dashboardDataList.get(position), context);
    }

    @Override
    public int getItemCount() {
        return dashboardDataList.size();
    }


    static class DashboardViewHolder extends RecyclerView.ViewHolder {
        ItemDashboardCardsBinding binding;

        DashboardViewHolder(ItemDashboardCardsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(DashboardData data, Context context) {
            binding.mainCard.setCardBackgroundColor(ContextCompat.getColor(context, data.getColorId()));
            binding.tvTitle.setText(data.getTitle());
            binding.tvCount.setText(data.getCount());
        }

    }

}
