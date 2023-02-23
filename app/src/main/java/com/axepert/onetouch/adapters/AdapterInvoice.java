package com.axepert.onetouch.adapters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemInvoiceProductsBinding;
import com.axepert.onetouch.models.CartModel;
import com.axepert.onetouch.responses.InvoiceResponse;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterInvoice extends RecyclerView.Adapter<AdapterInvoice.InvoiceViewHolder>{
    List<InvoiceResponse.Datum> invoiceList;

    public AdapterInvoice(List<InvoiceResponse.Datum> invoiceList) {
        this.invoiceList = invoiceList;
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InvoiceViewHolder(
                ItemInvoiceProductsBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        holder.setData(invoiceList.get(position));
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    static class InvoiceViewHolder extends RecyclerView.ViewHolder {
        ItemInvoiceProductsBinding binding;

        InvoiceViewHolder(ItemInvoiceProductsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(InvoiceResponse.Datum invoice) {
            binding.tvProductName.setText(Html.fromHtml(invoice.getProduct_details(), Html.FROM_HTML_MODE_LEGACY));
            binding.tvPrice.setText(amountFormat(invoice.getPriceperitem()));
            binding.tvTotal.setText(amountFormat(invoice.getTotal_price()));
            binding.tvQuantity.setText(invoice.getQuantity());
        }

        public String amountFormat(String amount) {
            Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
            return format.format(new BigDecimal(amount));
        }

    }

    public String total(List<InvoiceResponse.Datum> invoice) {
        int sum = 0;
        for (int i = 0; i < invoice.size(); i++) {
            int a = Integer.parseInt(invoice.get(i).getTotal_price());
            sum = sum + a;
        }
        return String.valueOf(sum);
    }

}
