package com.axepert.onetouch.adapters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.ItemOrderBinding;
import com.axepert.onetouch.listeners.OrdersListener;
import com.axepert.onetouch.responses.OrdersResponse;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterOrders extends RecyclerView.Adapter<AdapterOrders.OrdersViewHolder>{
    List<OrdersResponse.Datum> orderList;
    OrdersListener ordersListener;

    public AdapterOrders(List<OrdersResponse.Datum> orderList, OrdersListener ordersListener) {
        this.orderList = orderList;
        this.ordersListener = ordersListener;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersViewHolder(
                ItemOrderBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        holder.setData(orderList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrdersViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding binding;

        OrdersViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(OrdersResponse.Datum order) {
            binding.productName.setText(Html.fromHtml(order.getProduct_details(), Html.FROM_HTML_MODE_LEGACY));
            binding.productQuantity.setText(String.format("QTY: %s", order.getQuantity()));
            binding.tvAddress.setText(order.getShipping_address());
            binding.tvName.setText(order.getCustomer_name());
            binding.tvOrderDate.setText(String.format("Order Date: %s", order.getOrder_date()));
            binding.tvOrderId.setText(String.format("Order ID: %s", order.getOrder_id()));
            binding.tvPrice.setText(amountFormat(order.getTotal_price()));
            Picasso.get().load(order.getProduct_image()).into(binding.imgProduct);
            if (order.getOrder_status().equals("1")) {
                binding.tvStatus.setText("Success");
                binding.tvStatus.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.green));
            } else {
                binding.tvStatus.setText("Failed");
                binding.tvStatus.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.red));
            }
            binding.imgProduct.setOnClickListener(v -> {

            });
            binding.getRoot().setOnClickListener(v -> ordersListener.onOrderClicked(order));
        }

        public String amountFormat(String amount) {
            Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
            return format.format(new BigDecimal(amount));
        }

    }

}
