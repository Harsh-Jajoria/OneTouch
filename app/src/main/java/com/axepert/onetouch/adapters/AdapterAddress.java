package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemAddressBinding;
import com.axepert.onetouch.listeners.AddressListener;
import com.axepert.onetouch.responses.AddressListResponse;

import java.util.List;

public class AdapterAddress extends RecyclerView.Adapter<AdapterAddress.AddressViewHolder>{
    List<AddressListResponse.Datum> addressList;
    private AddressListener addressListener;

    public AdapterAddress(List<AddressListResponse.Datum> addressList, AddressListener addressListener) {
        this.addressList = addressList;
        this.addressListener = addressListener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressViewHolder(
                ItemAddressBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        holder.setData(addressList.get(position));
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    class AddressViewHolder extends RecyclerView.ViewHolder{
        ItemAddressBinding binding;

        AddressViewHolder(ItemAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(AddressListResponse.Datum address) {
            binding.tvAddress.setText(String.format(
                    "%s, %s, %s, %s",
                    address.getAddress(),
                    address.getCity(),
                    address.getState(),
                    address.getPincode()));
            binding.tvLandmark.setText(String.format("Landmark - %s", address.getLandmark()));
            binding.tvName.setText(address.getName());
            binding.tvPhone.setText(address.getMobile());
            binding.tvEmail.setText(address.getEmail());
            binding.imgEdit.setOnClickListener(v -> addressListener.onEditAddressClicked(address));
            binding.getRoot().setOnClickListener(v -> addressListener.onAddressClicked(address));
        }
    }
}
