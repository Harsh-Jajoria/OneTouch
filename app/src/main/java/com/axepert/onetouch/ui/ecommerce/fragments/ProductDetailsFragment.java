package com.axepert.onetouch.ui.ecommerce.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterProductImages;
import com.axepert.onetouch.adapters.AdapterTrendingProducts;
import com.axepert.onetouch.adapters.AdapterVariant;
import com.axepert.onetouch.databinding.FragmentProductDetailsBinding;
import com.axepert.onetouch.listeners.TrendingProductListener;
import com.axepert.onetouch.listeners.VariantClickListener;
import com.axepert.onetouch.models.CartModel;
import com.axepert.onetouch.requests.RelatedProductRequest;
import com.axepert.onetouch.responses.ProductImage;
import com.axepert.onetouch.responses.TrendingProduct;
import com.axepert.onetouch.responses.Variants;
import com.axepert.onetouch.ui.ecommerce.viewmodel.ProductDetailsViewModel;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProductDetailsFragment extends Fragment implements VariantClickListener, TrendingProductListener {
    FragmentProductDetailsBinding binding;
    ProductDetailsViewModel viewModel;
    private List<ProductImage> productImageList;
    AdapterProductImages adapterProductImages;
    private List<Variants> variantsList;
    private AdapterVariant adapterVariant;
    private List<TrendingProduct> relatedProductList;
    private AdapterTrendingProducts adapterTrendingProducts;
    String slug, id;
    private final CartModel cartModel = new CartModel();
    private Boolean isProductAvailableInCart = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailsBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(this).get(ProductDetailsViewModel.class);
        if (getArguments() != null) {
            slug = getArguments().getString("slug");
        }
        getProductDetails();
        banners();
        getRelatedProducts();
        setListener();
        return binding.getRoot();
    }

    private void setListener() {
        binding.imgBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());
        binding.swipeRefresh.setOnRefreshListener(() -> {
            productImageList.clear();
            variantsList.clear();
            getProductDetails();
        });
        binding.tvAddToCart.setOnClickListener(v -> {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            if (isProductAvailableInCart) {
                compositeDisposable.add(viewModel.removeFromCart(cartModel)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            isProductAvailableInCart = false;
                            binding.tvAddToCart.setText(getString(R.string.add_to_cart));
                            Toast.makeText(requireActivity(), "Removed from cart", Toast.LENGTH_SHORT).show();
                            compositeDisposable.dispose();
                        }));
            } else {
                compositeDisposable.add(viewModel.addToCart(cartModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            binding.tvAddToCart.setText(getString(R.string.added_to_cart));
                            Toast.makeText(requireActivity(), "Added to cart", Toast.LENGTH_SHORT).show();
                        }));
            }
        });
    }

    private void checkProductInCart() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.getProductFromCart(id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartModel -> {
                    isProductAvailableInCart = true;
                    binding.tvAddToCart.setText(getString(R.string.added_to_cart));
                    compositeDisposable.dispose();
                }));
    }

    private void banners() {
        productImageList = new ArrayList<>();
        adapterProductImages = new AdapterProductImages(productImageList);
        binding.bannerViewPager.setOffscreenPageLimit(1);
        binding.bannerViewPager.setAdapter(adapterProductImages);

        variantsList = new ArrayList<>();
        adapterVariant = new AdapterVariant(variantsList, this);
        binding.recyclerViewVariants.setHasFixedSize(true);
        binding.recyclerViewVariants.setAdapter(adapterVariant);

        relatedProductList = new ArrayList<>();
        adapterTrendingProducts = new AdapterTrendingProducts(relatedProductList, this);
        binding.recyclerViewRelatedProducts.setHasFixedSize(true);
        binding.recyclerViewRelatedProducts.setAdapter(adapterTrendingProducts);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getProductDetails() {
        isLoading(true);
        try {
            viewModel.getProductDetails(slug).observe(requireActivity(), response -> {
                if (response != null) {
                    if (response.code == 200) {
                        id = response.data.getId();
                        binding.tvProductName.setText(response.data.getName());
                        binding.tvCategory.setText(response.data.getCat_name());
                        binding.tvMRP.setText(amountFormat(response.data.getProvariant().get(0).getMrp()));
                        binding.tvOfferPrice.setText(amountFormat(response.data.getProvariant().get(0).getPrice()));
                        binding.tvVariant.setText(response.data.getProvariant().get(0).getVarients());
                        binding.tvDescription.setText(Html.fromHtml(response.data.getDescription(), Html.FROM_HTML_MODE_LEGACY));
                        binding.tvReturnDuration.setText(response.data.getReturn_duration());
                        binding.tvManufacturerDesc.setText(Html.fromHtml(response.data.getManufacturer_description(), Html.FROM_HTML_MODE_LEGACY));

                        productImageList.addAll(response.data.getProvariant().get(0).getProimage());
                        adapterProductImages.notifyDataSetChanged();

                        variantsList.addAll(response.data.getProvariant());
                        adapterVariant.notifyDataSetChanged();
                        binding.swipeRefresh.setRefreshing(false);

                        // Adding details to cart
                        cartModel.setId(response.data.getId());
                        cartModel.setImage(response.data.getProvariant().get(0).getProimage().get(0).getProduct_image());
                        cartModel.setMrp(response.data.getProvariant().get(0).getMrp());
                        cartModel.setOfferPrice(response.data.getProvariant().get(0).getPrice());
                        cartModel.setProduct_name(response.data.getName());
                        cartModel.setProduct_category(response.data.getCat_name());
                        cartModel.setSlug(slug);
                        cartModel.setQuantity("1");

                        checkProductInCart();
                        isLoading(false);
                    } else {
                        isLoading(false);
                    }
                } else {
                    isLoading(false);
                }
            });
        }catch (Exception e) {
            Toast.makeText(requireActivity(), "Response failed due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
            isLoading(false);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getRelatedProducts() {
        RelatedProductRequest relatedProductRequest = new RelatedProductRequest(
                "onetouch.com",
                slug,
                "10",
                "0");
        viewModel.getRelatedProduct(relatedProductRequest).observe(getViewLifecycleOwner(), relatedProductResponse -> {
            if (relatedProductResponse != null) {
                if (relatedProductResponse.code == 200) {
                    relatedProductList.addAll(relatedProductResponse.data.getProducts());
                    adapterTrendingProducts.notifyDataSetChanged();
                }
            }
        });
    }

    public String amountFormat(String amount) {
        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        return format.format(new BigDecimal(amount));
    }

    @Override
    public void onVariantClicked(Variants variants) {
        binding.tvMRP.setText(amountFormat(variants.getMrp()));
        binding.tvOfferPrice.setText(amountFormat(variants.getPrice()));

        cartModel.setMrp(variants.getMrp());
        cartModel.setOfferPrice(variants.getPrice());
    }


    @Override
    public void onTrendingProductClicked(TrendingProduct trendingProduct) {
        Bundle bundle = new Bundle();
        bundle.putString("slug", trendingProduct.getSlug());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.productDetailsFragment, bundle);
    }

    private void isLoading(Boolean isLoading) {
        if (isLoading) {
            binding.swipeRefresh.setVisibility(View.GONE);
            binding.divider.setVisibility(View.GONE);
            binding.layoutBuyNow.setVisibility(View.GONE);
            binding.loading.setVisibility(View.VISIBLE);
        } else {
            binding.swipeRefresh.setVisibility(View.VISIBLE);
            binding.divider.setVisibility(View.VISIBLE);
            binding.layoutBuyNow.setVisibility(View.VISIBLE);
            binding.loading.setVisibility(View.GONE);
        }
    }

}