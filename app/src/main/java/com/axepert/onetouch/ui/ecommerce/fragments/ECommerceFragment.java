package com.axepert.onetouch.ui.ecommerce.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterBanners;
import com.axepert.onetouch.adapters.AdapterPopularProducts;
import com.axepert.onetouch.adapters.AdapterProductCategory;
import com.axepert.onetouch.adapters.AdapterTrendingProducts;
import com.axepert.onetouch.databinding.FragmentBecomeProfessionalBinding;
import com.axepert.onetouch.listeners.PopularProductListener;
import com.axepert.onetouch.listeners.ProductCategoryListener;
import com.axepert.onetouch.listeners.TrendingProductListener;
import com.axepert.onetouch.responses.Banners;
import com.axepert.onetouch.responses.PopularProduct;
import com.axepert.onetouch.responses.ProductCategory;
import com.axepert.onetouch.responses.TrendingProduct;
import com.axepert.onetouch.ui.ecommerce.viewmodel.ECommerceViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ECommerceFragment extends Fragment implements TrendingProductListener, PopularProductListener, ProductCategoryListener {
    private FragmentBecomeProfessionalBinding binding;
    private ECommerceViewModel viewModel;
    private List<TrendingProduct> trendingProductList;
    private AdapterTrendingProducts adapterTrendingProducts;
    private List<PopularProduct> popularProductList;
    private AdapterPopularProducts adapterPopularProducts;
    private List<ProductCategory> productCategoryList;
    private AdapterProductCategory adapterProductCategory;
    private AdapterBanners adapterBanners;
    private List<Banners> bannersList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBecomeProfessionalBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(this).get(ECommerceViewModel.class);
        init();
        setListener();
        loadCart();
        return binding.getRoot();
    }

    private void init() {
        binding.recyclerViewTrendingProducts.setHasFixedSize(true);
        trendingProductList = new ArrayList<>();
        adapterTrendingProducts = new AdapterTrendingProducts(trendingProductList, this);
        binding.recyclerViewTrendingProducts.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewTrendingProducts.setAdapter(adapterTrendingProducts);

        binding.recyclerViewPopularProducts.setHasFixedSize(true);
        popularProductList = new ArrayList<>();
        adapterPopularProducts = new AdapterPopularProducts(popularProductList, this);
        binding.recyclerViewPopularProducts.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewPopularProducts.setAdapter(adapterPopularProducts);

        binding.recyclerViewCategories.setHasFixedSize(true);
        productCategoryList = new ArrayList<>();
        adapterProductCategory = new AdapterProductCategory(productCategoryList, this);
        binding.recyclerViewCategories.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewCategories.setAdapter(adapterProductCategory);

        bannersList = new ArrayList<>();
        adapterBanners = new AdapterBanners(bannersList);
        binding.bannerViewPager.setOffscreenPageLimit(1);
        binding.bannerViewPager.setAdapter(adapterBanners);
        bannersList.add(new Banners("", "", "", "https://axepertexhibits.com/demonetouch/nestonetouch/assets/imgs/slider/banner.jpg"));
        bannersList.add(new Banners("", "", "", "https://axepertexhibits.com/demonetouch/nestonetouch/assets/imgs/slider/banner2.jpg"));

        fetchHomePage();
    }

    private void setListener() {
        binding.rlCart.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.cartFragment);
        });
        binding.rlSearch.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_professional_to_searchFragment));
        binding.swipeRefresh.setOnRefreshListener(this::refresh);
        binding.btnViewAllTrendingProducts.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_professional_to_productListFragment));
        binding.btnViewAllPopularProducts.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_professional_to_productListFragment));
        binding.btnViewAllCategories.setOnClickListener(v -> {

        });
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 101) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        int resultFromIntent = intent.getIntExtra("newCode", -1);
                        if (resultFromIntent == 1) {
                            Bundle bundle = new Bundle();
                            bundle.putString("slug", intent.getStringExtra("slug"));
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_professional_to_productDetailsFragment, bundle);
                        }
                    }
                }
            }
    );

    private void refresh() {
        viewModel.refreshEComm().observe(requireActivity(), eCommHomePageResponse -> {
            if (eCommHomePageResponse.code == 200) {
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchHomePage() {
        viewModel.ecommerceHomePage().observe(requireActivity(), eCommHomePageResponse -> {
            if (eCommHomePageResponse != null) {
                if (eCommHomePageResponse.code == 200) {
                    trendingProductList.addAll(eCommHomePageResponse.data.getTrending_product());
                    adapterTrendingProducts.notifyDataSetChanged();

                    popularProductList.addAll(eCommHomePageResponse.data.getPopular_product());
                    adapterPopularProducts.notifyDataSetChanged();

                    productCategoryList.addAll(eCommHomePageResponse.data.getCategory());
                    adapterProductCategory.notifyDataSetChanged();

                    binding.swipeRefresh.setVisibility(View.VISIBLE);
                    binding.loading.setVisibility(View.GONE);
                } else {
                    Toast.makeText(requireActivity(), eCommHomePageResponse.status, Toast.LENGTH_SHORT).show();
                    binding.loading.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onTrendingProductClicked(TrendingProduct trendingProduct) {
        Bundle bundle = new Bundle();
        bundle.putString("slug", trendingProduct.getSlug());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_professional_to_productDetailsFragment, bundle);
    }

    @Override
    public void onPopularProductClicked(PopularProduct popularProduct) {
        Bundle bundle = new Bundle();
        bundle.putString("slug", popularProduct.getSlug());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_professional_to_productDetailsFragment, bundle);
    }


    @Override
    public void onCategoryClicked(ProductCategory productCategory) {
        Bundle bundle = new Bundle();
        bundle.putString("cat_id", productCategory.getId());
        bundle.putString("cat_name", productCategory.getCategory_name());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_professional_to_productByCategoryFragment, bundle);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadCart() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.loadCartList().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartModels -> {
                    if (cartModels.size() > 0) {
                        binding.tvCartCount.setVisibility(View.VISIBLE);
                        binding.tvCartCount.setText(String.valueOf(cartModels.size()));
                    }
                    compositeDisposable.dispose();
                }));
    }

}