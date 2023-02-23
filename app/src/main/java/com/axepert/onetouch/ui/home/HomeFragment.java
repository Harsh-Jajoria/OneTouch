package com.axepert.onetouch.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterBanners;
import com.axepert.onetouch.adapters.AdapterBestSellers;
import com.axepert.onetouch.adapters.AdapterBlogs;
import com.axepert.onetouch.adapters.AdapterMostPopularServices;
import com.axepert.onetouch.adapters.AdapterOurCategories;
import com.axepert.onetouch.adapters.AdapterOurServiceProviders;
import com.axepert.onetouch.databinding.FragmentHomeBinding;
import com.axepert.onetouch.listeners.BestSellerListener;
import com.axepert.onetouch.listeners.BlogListener;
import com.axepert.onetouch.listeners.OurCategoryListener;
import com.axepert.onetouch.listeners.ServicesListener;
import com.axepert.onetouch.responses.Banners;
import com.axepert.onetouch.responses.Blog;
import com.axepert.onetouch.responses.Category;
import com.axepert.onetouch.responses.HomeScreenResponse;
import com.axepert.onetouch.responses.SellerInformation;
import com.axepert.onetouch.responses.ServiceProviders;
import com.axepert.onetouch.responses.Services;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ServicesListener, BestSellerListener, BlogListener, OurCategoryListener {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private AdapterMostPopularServices adapterMostPopularServices;
    private List<Services> servicesList;
    private AdapterOurCategories adapterOurCategories;
    private List<Category> categoryList;
    private AdapterOurServiceProviders adapterOurServiceProviders;
    private List<ServiceProviders> serviceProvidersList;
    private AdapterBestSellers adapterBestSellers;
    private List<SellerInformation> sellerInformationList;
    private AdapterBlogs adapterBlogs;
    private List<Blog> blogList;
    private AdapterBanners adapterBanners;
    private List<Banners> bannersList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        setListener();
        init();
        return binding.getRoot();
    }

    private void setListener() {
        binding.rlSearch.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_home_to_searchFragment));
        binding.btnViewAllBlogs.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_blog));
        binding.btnViewAllServices.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_home_to_allServicesFragment));
        binding.btnViewAllSellers.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_home_to_allSellersFragment));
        binding.btnViewAllServiceProviders.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_home_to_allServiceProvidersFragment));
        binding.swipeRefresh.setOnRefreshListener(this::refresh);
    }

    private void refresh() {
        loading(true);
        viewModel.refreshHomeScreen().observe(requireActivity(), homeScreenResponse -> {
            if (homeScreenResponse != null) {
                if (homeScreenResponse.code == 200) {
                    binding.swipeRefresh.setRefreshing(false);
                    loading(false);
                } else {
                    loading(false);
                    binding.tvNotFound.setVisibility(View.VISIBLE);
                }
            } else {
                loading(false);
                binding.tvNotFound.setVisibility(View.VISIBLE);
            }
        });
    }

    private void init() {
        servicesList = new ArrayList<>();
        binding.serviceRecyclerView.setHasFixedSize(true);
        adapterMostPopularServices = new AdapterMostPopularServices(servicesList, this);
        binding.serviceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.serviceRecyclerView.setAdapter(adapterMostPopularServices);

        categoryList = new ArrayList<>();
        binding.ourCategoryRecyclerView.setHasFixedSize(true);
        adapterOurCategories = new AdapterOurCategories(categoryList, this);
        binding.ourCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.ourCategoryRecyclerView.setAdapter(adapterOurCategories);

        serviceProvidersList = new ArrayList<>();
        binding.recyclerViewServiceProvider.setHasFixedSize(true);
        adapterOurServiceProviders = new AdapterOurServiceProviders(serviceProvidersList);
        binding.recyclerViewServiceProvider.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewServiceProvider.setAdapter(adapterOurServiceProviders);

        sellerInformationList = new ArrayList<>();
        binding.recyclerViewBestSeller.setHasFixedSize(true);
        adapterBestSellers = new AdapterBestSellers(sellerInformationList, this);
        binding.recyclerViewBestSeller.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewBestSeller.setAdapter(adapterBestSellers);

        blogList = new ArrayList<>();
        binding.recyclerViewBlogs.setHasFixedSize(true);
        adapterBlogs = new AdapterBlogs(blogList, this);
        binding.recyclerViewBlogs.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewBlogs.setAdapter(adapterBlogs);

        bannersList = new ArrayList<>();
        adapterBanners = new AdapterBanners(bannersList);
        binding.bannerViewPager.setOffscreenPageLimit(1);
        binding.bannerViewPager.setAdapter(adapterBanners);

        fetchHomePageData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchHomePageData() {
        loading(true);
        viewModel.fetchHomeScreen().observe(requireActivity(), homeScreenResponse -> {
            if (homeScreenResponse != null) {
                if (homeScreenResponse.code == 200) {
                    // Banners
                    bannersList.addAll(homeScreenResponse.data.getBanner());
                    adapterBanners.notifyDataSetChanged();
                    // Most Popular Service
                    servicesList.addAll(homeScreenResponse.data.getServices());
                    adapterMostPopularServices.notifyDataSetChanged();
                    // Our Category
                    categoryList.addAll(homeScreenResponse.data.getCategory());
                    adapterOurCategories.notifyDataSetChanged();
                    // Our Service Providers
                    serviceProvidersList.addAll(homeScreenResponse.data.getService_provider());
                    adapterOurServiceProviders.notifyDataSetChanged();
                    // Best Seller
                    sellerInformationList.addAll(homeScreenResponse.data.getSeller_info());
                    adapterBestSellers.notifyDataSetChanged();
                    // Blogs
                    blogList.addAll(homeScreenResponse.data.getBlogs());
                    adapterBlogs.notifyDataSetChanged();
                    binding.swipeRefresh.setRefreshing(false);

                    loading(false);
                } else {
                    loading(false);
                    binding.tvNotFound.setVisibility(View.VISIBLE);
                    binding.swipeRefresh.setVisibility(View.GONE);
                }
            } else {
                loading(false);
                binding.tvNotFound.setVisibility(View.VISIBLE);
                binding.swipeRefresh.setVisibility(View.GONE);
            }
        });
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            binding.loading.setVisibility(View.VISIBLE);
            binding.swipeRefresh.setVisibility(View.GONE);
        } else {
            binding.loading.setVisibility(View.GONE);
            binding.swipeRefresh.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(Services services) {
        Bundle bundle = new Bundle();
        bundle.putString("category", services.getService_type());
        bundle.putString("description", services.getDescription());
        bundle.putString("imageUrl", services.getServices_image());
        bundle.putString("id", services.getId());
        bundle.putInt("count", services.getProvider_count());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_home_to_serviceDetailsFragment, bundle);
    }

    @Override
    public void onSellerClick(SellerInformation sellerInformation) {
        Bundle bundle = new Bundle();
        bundle.putString("id", sellerInformation.getId());
        bundle.putString("name", sellerInformation.getName());
        bundle.putString("email", sellerInformation.getEmail());
        bundle.putString("contact", sellerInformation.getContact());
        bundle.putString("image", sellerInformation.getImage());
        bundle.putString("shop_name", sellerInformation.getShop_name());
        bundle.putString("shop_add", sellerInformation.getShop_address());
        bundle.putString("desc", sellerInformation.getDescription());
        bundle.putString("seller_video", sellerInformation.getSeller_video());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_home_to_sellerDetailsFragment, bundle);
    }

    @Override
    public void onBlogClicked(Blog blog) {
        Bundle bundle = new Bundle();
        bundle.putString("title", blog.getHeading());
        bundle.putString("desc", blog.getDescription());
        bundle.putString("service", blog.getService_name());
        bundle.putString("image", blog.getImage());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_home_to_blogInfoFragment, bundle);
    }

    @Override
    public void onOurCategoryClicked(Category category) {
        Bundle bundle = new Bundle();
        bundle.putString("cat_id", category.getId());
        bundle.putString("cat_name", category.getCategory_name());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_home_to_productByCategoryFragment, bundle);
    }
}