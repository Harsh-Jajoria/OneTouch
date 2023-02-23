package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.ItemVariantsBinding;
import com.axepert.onetouch.listeners.VariantClickListener;
import com.axepert.onetouch.responses.Variants;

import java.util.List;

public class AdapterVariant extends RecyclerView.Adapter<AdapterVariant.VariantViewHolder> {
    List<Variants> variantList;
    VariantClickListener variantClickListener;
    private int checkedPosition = 0;

    public AdapterVariant(List<Variants> variantList, VariantClickListener variantClickListener) {
        this.variantList = variantList;
        this.variantClickListener = variantClickListener;
    }

    @NonNull
    @Override
    public VariantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VariantViewHolder(
                ItemVariantsBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull VariantViewHolder holder, int position) {
        holder.setData(variantList.get(position));
    }

    @Override
    public int getItemCount() {
        return variantList.size();
    }

    class VariantViewHolder extends RecyclerView.ViewHolder {
        ItemVariantsBinding binding;

        VariantViewHolder(ItemVariantsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Variants variants) {
            if (checkedPosition == -1) {
                binding.getRoot().setBackground(ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.background_varient_not_selected));
                binding.tvVariant.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.primary));
            } else {
                if (checkedPosition == getAbsoluteAdapterPosition()) {
                    binding.getRoot().setBackground(ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.background_variant_selected));
                    binding.tvVariant.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.blue));
                } else {
                    binding.getRoot().setBackground(ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.background_varient_not_selected));
                    binding.tvVariant.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.primary));
                }
            }

            binding.tvVariant.setText(variants.getVarients());
            binding.getRoot().setOnClickListener(v -> {
                binding.getRoot().setBackground(ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.background_variant_selected));
                binding.tvVariant.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.blue));
                if (checkedPosition != getAbsoluteAdapterPosition()) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = getAbsoluteAdapterPosition();
                }
                variantClickListener.onVariantClicked(variants);
            });
        }
    }

}
