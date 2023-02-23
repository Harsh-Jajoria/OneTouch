package com.axepert.onetouch.ui.cart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterCart;
import com.axepert.onetouch.databinding.FragmentCartBinding;
import com.axepert.onetouch.listeners.CartListener;
import com.axepert.onetouch.models.CartModel;
import com.axepert.onetouch.ui.login.LoginActivity;
import com.axepert.onetouch.utilities.Constants;
import com.axepert.onetouch.utilities.PreferenceManager;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CartFragment extends Fragment implements CartListener {
    private FragmentCartBinding binding;
    private CartViewModel cartViewModel;
    private List<CartModel> cartList;
    private AdapterCart adapterCart;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(getLayoutInflater(), container, false);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        init();
        setListener();
        loadCart();
        return binding.getRoot();
    }

    private void init() {
        preferenceManager = new PreferenceManager(requireActivity());
        cartList = new ArrayList<>();
        adapterCart = new AdapterCart(cartList, this);
        binding.recyclerViewCart.setHasFixedSize(false);
        binding.recyclerViewCart.setAdapter(adapterCart);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadCart() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(cartViewModel.loadCartList().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartModels -> {
                    if (cartList.size() > 0) {
                        cartList.clear();
                    }
                    cartList.addAll(cartModels);
                    adapterCart.notifyDataSetChanged();

                    if (cartList.size() == 0) {
                        binding.nestedScrollView.setVisibility(View.GONE);
                        binding.llEmpty.setVisibility(View.VISIBLE);
                    } else {
                        binding.nestedScrollView.setVisibility(View.VISIBLE);
                        binding.llEmpty.setVisibility(View.GONE);
                    }
                    totalBillCalculation();
                    compositeDisposable.dispose();
                }));
    }

    private void setListener() {
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        binding.btnPayment.setOnClickListener(v -> {
            if (preferenceManager.getBoolean(Constants.KEY_IS_LOGIN)) {
                Bundle bundle = new Bundle();
                bundle.putInt("payment", 1);
                Navigation.findNavController(v).navigate(R.id.addressesFragment, bundle);
            } else {
                startActivity(new Intent(requireActivity(), LoginActivity.class));
            }
        });
        binding.btnShopNow.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
    }

    private void totalBillCalculation() {
        binding.tvSubTotal.setText(amountFormat(adapterCart.total(cartList)));
        binding.tvDiscount.setText(String.format("- %s", amountFormat(adapterCart.discount(cartList))));

        int totalMrp = Integer.parseInt(adapterCart.total(cartList));
        int totalDiscount = Integer.parseInt(adapterCart.discount(cartList));
        int finalAmount = totalMrp - totalDiscount;
        binding.total.setText(amountFormat(String.valueOf(finalAmount)));
    }


    @Override
    public void onProductClicked(CartModel cartModel) {
        Bundle bundle = new Bundle();
        bundle.putString("slug", cartModel.getSlug());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.productDetailsFragment, bundle);
    }

    @Override
    public void removeFromCart(CartModel cartModel, int position) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(cartViewModel.removeFromCart(cartModel)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    cartList.remove(position);
                    adapterCart.notifyItemRemoved(position);
                    adapterCart.notifyItemRangeChanged(position, adapterCart.getItemCount());
                    totalBillCalculation();

                    if (cartList.size() == 0) {
                        binding.nestedScrollView.setVisibility(View.GONE);
                        binding.llEmpty.setVisibility(View.VISIBLE);
                    } else {
                        binding.nestedScrollView.setVisibility(View.VISIBLE);
                        binding.llEmpty.setVisibility(View.GONE);
                    }

                    compositeDisposable.dispose();
                }));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void increaseQuantity(CartModel cartModel) {
        int currentQty = Integer.parseInt(cartModel.getQuantity());
        currentQty++;
        cartModel.setQuantity(""+currentQty);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(cartViewModel.productQuantity(cartModel)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    adapterCart.notifyDataSetChanged();
                    totalBillCalculation();
                    compositeDisposable.dispose();
                }));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void decreaseQuantity(CartModel cartModel) {
        if (!cartModel.getQuantity().equals("1")) {
            int currentQty = Integer.parseInt(cartModel.getQuantity());
            currentQty--;
            cartModel.setQuantity(""+currentQty);
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(cartViewModel.productQuantity(cartModel)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        adapterCart.notifyDataSetChanged();
                        totalBillCalculation();
                        compositeDisposable.dispose();
                    }));
        }
    }

    public String amountFormat(String amount) {
        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        return format.format(new BigDecimal(amount));
    }
}