package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemCheckoutProductBinding;
import com.axepert.onetouch.models.CartModel;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterCheckoutProduct extends RecyclerView.Adapter<AdapterCheckoutProduct.CheckOutViewHolder> {
    List<CartModel> cartModelList;

    public AdapterCheckoutProduct(List<CartModel> cartModelList) {
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public CheckOutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckOutViewHolder(
                ItemCheckoutProductBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOutViewHolder holder, int position) {
        holder.bindData(cartModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    static class CheckOutViewHolder extends RecyclerView.ViewHolder {
        ItemCheckoutProductBinding binding;

        CheckOutViewHolder(ItemCheckoutProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(CartModel cart) {
            Picasso.get().load(cart.getImage()).into(binding.productImage);
            binding.tvProductName.setText(cart.getProduct_name());
            binding.tvProductCat.setText(cart.getProduct_category());
            binding.tvQty.setText(String.format("Qty: %s", cart.getQuantity()));
            int price = Integer.parseInt(cart.getOfferPrice()) * Integer.parseInt(cart.getQuantity());
            binding.tvOfferPrice.setText(amountFormat(String.valueOf(price)));
            int mrp = Integer.parseInt(cart.getMrp()) * Integer.parseInt(cart.getQuantity());
            binding.tvMRP.setText(amountFormat(String.valueOf(mrp)));
            if (binding.tvOfferPrice.getText().toString().equals(binding.tvMRP.getText().toString())) {
                binding.tvMRP.setVisibility(View.GONE);
                binding.hLine.setVisibility(View.GONE);
            }
        }

        public String amountFormat(String amount) {
            Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
            return format.format(new BigDecimal(amount));
        }

    }

    public String total(List<CartModel> cart) {
        int sum = 0;
        for (int i = 0; i < cart.size(); i++) {
            int a = Integer.parseInt(cart.get(i).getMrp());
            sum = sum + a;
            sum = sum * Integer.parseInt(cart.get(i).getQuantity());
        }
        return String.valueOf(sum);
    }

    public String discount(List<CartModel> cart) {
        int sum = 0;
        for (int i = 0; i < cart.size(); i++) {
            int a = Integer.parseInt(cart.get(i).getOfferPrice());
            sum = sum + a;
            sum = sum * Integer.parseInt(cart.get(i).getQuantity());
        }
        int totalDiscount = Integer.parseInt(total(cart)) - sum;
        return String.valueOf(totalDiscount);
    }

}
