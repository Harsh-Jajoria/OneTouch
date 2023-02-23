package com.axepert.onetouch.ui.checkout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.axepert.onetouch.MainActivity;
import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterCart;
import com.axepert.onetouch.adapters.AdapterCheckoutProduct;
import com.axepert.onetouch.databinding.FragmentCheckOutBinding;
import com.axepert.onetouch.models.CartModel;
import com.axepert.onetouch.requests.OrderCodRequest;
import com.axepert.onetouch.requests.OrderOnlineRequest;
import com.axepert.onetouch.responses.AddressListResponse;
import com.axepert.onetouch.responses.PlaceOrderResponse;
import com.axepert.onetouch.utilities.Constants;
import com.axepert.onetouch.utilities.PreferenceManager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CheckOutFragment extends Fragment {
    private FragmentCheckOutBinding binding;
    private CheckOutViewModel checkOutViewModel;
    private AddressListResponse.Datum address = new AddressListResponse.Datum();
    private PreferenceManager preferenceManager;
    private List<CartModel> cartList;
    private AdapterCheckoutProduct adapterCheckoutProduct;
    String currentDate, orderId, trnxId, payableAmt;
    int resCode;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCheckOutBinding.inflate(getLayoutInflater(), container, false);
        checkOutViewModel = new ViewModelProvider(this).get(CheckOutViewModel.class);
        if (getArguments() != null) {
            address = (AddressListResponse.Datum) getArguments().getSerializable("address");
            binding.tvName.setText(address.getName());
            binding.tvAddress.setText(String.format("%s, %s, %s %s", address.getAddress(), address.getState(), address.getCity(), address.getPincode()));
            binding.tvPhone.setText(address.getMobile());
        }
        generateOrderAndTrnxId();
        init();
        setListener();
        loadCart();
        return binding.getRoot();
    }

    private void init() {
        preferenceManager = new PreferenceManager(requireActivity());
        cartList = new ArrayList<>();
        adapterCheckoutProduct = new AdapterCheckoutProduct(cartList);
        binding.recyclerViewCheckoutProducts.setHasFixedSize(false);
        binding.recyclerViewCheckoutProducts.setAdapter(adapterCheckoutProduct);
        currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    private void setListener() {
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());
        binding.tvCod.setOnClickListener(v -> confirmationDialog());
        binding.tvOnline.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), PaymentActivity.class);
            intent.putExtra("order_id", orderId);
            intent.putExtra("trnx_id", trnxId);
            intent.putExtra("amount", payableAmt);
            intent.putExtra("email", address.getEmail());
            intent.putExtra("phone", address.getMobile());
            onlineResultLauncher.launch(intent);
        });
    }

    private final ActivityResultLauncher<Intent> onlineResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 200) {
                    Intent data = result.getData();
                    if (data != null) {
                        String razorpay_id = data.getStringExtra("razorpay_id");
                        placeOrderOnline(razorpay_id);
                    }
                } else if (result.getResultCode() == 400) {
                    Intent data = result.getData();
                    if (data != null) {
                        String error = data.getStringExtra("error");
                        new AlertDialog.Builder(requireActivity())
                                .setTitle("Order failed")
                                .setMessage(error)
                                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                }
            }
    );

    @SuppressLint("NotifyDataSetChanged")
    private void loadCart() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(checkOutViewModel.loadCartList().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartModels -> {
                    if (cartList.size() > 0) {
                        cartList.clear();
                    }
                    cartList.addAll(cartModels);
                    adapterCheckoutProduct.notifyDataSetChanged();

                    totalBillCalculation();
                    compositeDisposable.dispose();
                }));
    }

    private void totalBillCalculation() {
        binding.tvSubTotal.setText(amountFormat(adapterCheckoutProduct.total(cartList)));
        binding.tvDiscount.setText(String.format("- %s", amountFormat(adapterCheckoutProduct.discount(cartList))));

        int totalMrp = Integer.parseInt(adapterCheckoutProduct.total(cartList));
        int totalDiscount = Integer.parseInt(adapterCheckoutProduct.discount(cartList));
        int finalAmount = totalMrp - totalDiscount;
        payableAmt = String.valueOf(finalAmount);
        binding.total.setText(amountFormat(String.valueOf(finalAmount)));
    }

    public String amountFormat(String amount) {
        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        return format.format(new BigDecimal(amount));
    }

    private void confirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Confirm Purchase");
        builder.setMessage("Click [Yes] to confirm your order.");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            placeOrder();
            dialog.dismiss();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.setCancelable(true);
        builder.show();
    }

    private void placeOrder() {
        for (int i = 0; i < cartList.size(); i++) {
            int p = Integer.parseInt(cartList.get(i).getOfferPrice());
            int q = Integer.parseInt(cartList.get(i).getQuantity());
            int total = p * q;

            OrderCodRequest codRequest = new OrderCodRequest(
                    address.getEmail(),
                    "onetouch.com",
                    address.getName(),
                    address.getMobile(),
                    cartList.get(i).getId(),
                    cartList.get(i).getQuantity(),
                    cartList.get(i).getOfferPrice(),
                    String.valueOf(total),
                    String.valueOf(total),
                    currentDate,
                    address.getAddress(),
                    "cod",
                    address.getState(),
                    address.getCity(),
                    address.getPincode(),
                    preferenceManager.getString(Constants.KEY_USER_ID),
                    "qwerty",
                    orderId,
                    trnxId);

            checkOutViewModel.orderCod(codRequest).observe(getViewLifecycleOwner(), response -> {
                if (response != null) {
                    if (response.code == 200) {
                        resCode = response.code;
                        Toast.makeText(requireContext(), response.status, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), response.status, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Order failed", Toast.LENGTH_SHORT).show();
                }
            });

            if (resCode != 200) {
                break;
            }
        }

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(checkOutViewModel.deleteAll()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    View view = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_order_success, null);
                    builder.setView(view);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();

                    new Handler().postDelayed(() -> {
                        alertDialog.dismiss();
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_checkOutFragment_to_nav_home);
                    }, 5000);

                    compositeDisposable.dispose();
                }));


    }

    private void placeOrderOnline(String razorpay_id) {
        for (int i = 0; i < cartList.size(); i++) {
            int p = Integer.parseInt(cartList.get(i).getOfferPrice());
            int q = Integer.parseInt(cartList.get(i).getQuantity());
            int total = p * q;

            OrderOnlineRequest onlineRequest = new OrderOnlineRequest(
                    address.getEmail(),
                    "onetouch.com",
                    address.getName(),
                    address.getMobile(),
                    cartList.get(i).getId(),
                    cartList.get(i).getQuantity(),
                    cartList.get(i).getOfferPrice(),
                    String.valueOf(total),
                    String.valueOf(total),
                    currentDate,
                    address.getAddress(),
                    "online",
                    address.getState(),
                    address.getCity(),
                    address.getPincode(),
                    orderId,
                    trnxId,
                    preferenceManager.getString(Constants.KEY_USER_ID),
                    "qwerty",
                    address.getAddress(),
                    address.getPincode(),
                    "success",
                    razorpay_id);

            checkOutViewModel.orderOnline(onlineRequest).observe(getViewLifecycleOwner(), response -> {
                if (response != null) {
                    if (response.code == 200) {
                        resCode = response.code;
                        Toast.makeText(requireContext(), response.status, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), response.status, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Order failed", Toast.LENGTH_SHORT).show();
                }
            });

            if (resCode != 200) {
                break;
            }
        }

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(checkOutViewModel.deleteAll()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    View view = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_order_success, null);
                    builder.setView(view);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();

                    new Handler().postDelayed(() -> {
                        alertDialog.dismiss();
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_checkOutFragment_to_nav_home);
                    }, 5000);

                    compositeDisposable.dispose();
                }));

    }

    private void generateOrderAndTrnxId() {
        long max = 9999999999L;
        long min = 1111111111L;
        long b = (long) (Math.random() * (max - min + 1) + min);
        Log.d("orderId", "generateOrderAndTrnxId: " + b);
        orderId = String.format("ORD%s", b);

        int trxMax = 999999999;
        int trxMin = 111111111;
        int t = (int) (Math.random() * (trxMax - trxMin - 1) + trxMin);
        trnxId = String.format("TRX%s", t);
    }

}