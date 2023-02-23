package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemCartBinding;
import com.axepert.onetouch.listeners.CartListener;
import com.axepert.onetouch.models.CartModel;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.CartViewHolder> {
    List<CartModel> cartModelList;
    CartListener cartListener;

    public AdapterCart(List<CartModel> cartModelList, CartListener cartListener) {
        this.cartModelList = cartModelList;
        this.cartListener = cartListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(
                ItemCartBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.setData(cartModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;

        CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(CartModel cart) {
            binding.tvProductName.setText(cart.getProduct_name());
            binding.tvProductCat.setText(cart.getProduct_category());
            int price = Integer.parseInt(cart.getOfferPrice()) * Integer.parseInt(cart.getQuantity());
            binding.prodPrice.setText(amountFormat(String.valueOf(price)));
            binding.tvQuantity.setText(cart.getQuantity());
            Picasso.get().load(cart.getImage()).into(binding.productImage);
            binding.getRoot().setOnClickListener(v -> cartListener.onProductClicked(cart));
            binding.imgDelete.setOnClickListener(v -> cartListener.removeFromCart(cart, getAbsoluteAdapterPosition()));
            binding.imgAdd.setOnClickListener(v -> cartListener.increaseQuantity(cart));
            binding.imgMinus.setOnClickListener(v -> cartListener.decreaseQuantity(cart));
            binding.imgMinus.setOnClickListener(v -> cartListener.decreaseQuantity(cart));
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
