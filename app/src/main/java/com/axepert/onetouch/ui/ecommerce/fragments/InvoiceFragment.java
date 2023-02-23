package com.axepert.onetouch.ui.ecommerce.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterInvoice;
import com.axepert.onetouch.databinding.FragmentInvoiceBinding;
import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.InvoiceRequest;
import com.axepert.onetouch.responses.InvoiceResponse;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceFragment extends Fragment {
    private FragmentInvoiceBinding binding;
    private List<InvoiceResponse.Datum> invoiceList;
    private AdapterInvoice adapterInvoice;
    private String orderId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInvoiceBinding.inflate(getLayoutInflater(), container, false);
        init();
        setListener();
        return binding.getRoot();
    }

    private void setListener() {
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());
    }

    private void init() {
        if (getArguments() != null) {
            orderId = getArguments().getString("order_id");
        }

        invoiceList = new ArrayList<>();
        adapterInvoice = new AdapterInvoice(invoiceList);
        binding.recyclerViewProducts.setHasFixedSize(true);
        binding.recyclerViewProducts.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerViewProducts.setAdapter(adapterInvoice);
        getInvoice();

    }

    private void getInvoice() {
        loading(true);
        try {
            InvoiceRequest invoiceRequest = new InvoiceRequest("onetouch.com", orderId);
            ApiClient.getRetrofit().create(ApiService.class).invoice(invoiceRequest).enqueue(new Callback<InvoiceResponse>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<InvoiceResponse> call, @NonNull Response<InvoiceResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().code == 200) {
                            InvoiceResponse.Datum invoice = response.body().data.get(0);
                            binding.tvTransactionId.setText(invoice.getTxn_id());
                            binding.tvOrderId.setText(invoice.getOrder_id());
                            binding.tvDate.setText(invoice.getOrder_date());
                            binding.tvPaymentStatus.setText(invoice.getPayment_status());
                            binding.tvOrderStatus.setText(invoice.getOrder_status());
                            binding.tvRazorpayId.setText(invoice.getRazorpay_payment_id());
                            binding.tvToAddress.setText(String.format("Name : %s\nEmail : %s\nMobile : %s\n%s", invoice.getCustomer_name(), invoice.getCustomer_email(), invoice.getCustomer_mobile(), invoice.getShipping_address()));
                            invoiceList.addAll(response.body().data);
                            adapterInvoice.notifyDataSetChanged();
                            binding.tvGrandTotal.setText(amountFormat(adapterInvoice.total(response.body().data)));
                            loading(false);
                        } else {
                            loading(false);
                        }
                    } else {
                        loading(false);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<InvoiceResponse> call, @NonNull Throwable t) {
                    loading(false);
                }
            });
        } catch (Exception e) {
            e.getLocalizedMessage();
            loading(false);
        }
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.invoiceLayout.setVisibility(View.GONE);
            binding.progress.setVisibility(View.VISIBLE);
        } else {
            binding.invoiceLayout.setVisibility(View.VISIBLE);
            binding.progress.setVisibility(View.GONE);
        }
    }

    public String amountFormat(String amount) {
        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        return format.format(new BigDecimal(amount));
    }

}