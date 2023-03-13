package com.axepert.onetouch.ui.blog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterBlogs;
import com.axepert.onetouch.databinding.FragmentBlogBinding;
import com.axepert.onetouch.listeners.BlogListener;
import com.axepert.onetouch.responses.Blog;
import com.axepert.onetouch.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class BlogFragment extends Fragment implements BlogListener {
    private FragmentBlogBinding binding;
    private HomeViewModel viewModel;
    private AdapterBlogs adapterBlogs;
    private List<Blog> blogList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBlogBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        setListener();
        initBlogRV();
        return binding.getRoot();
    }

    private void setListener() {
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        binding.swipeRefresh.setOnRefreshListener(() -> {
            blogList.clear();
            fetchBlog();
        });
    }

    private void initBlogRV() {
        blogList = new ArrayList<>();
        binding.recyclerViewBlogs.setHasFixedSize(true);
        adapterBlogs = new AdapterBlogs(blogList, this);
        binding.recyclerViewBlogs.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewBlogs.setAdapter(adapterBlogs);
        fetchBlog();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchBlog() {
        binding.swipeRefresh.setRefreshing(true);
        viewModel.fetchHomeScreen().observe(requireActivity(), homeScreenResponse -> {
            if (homeScreenResponse != null) {
                if (homeScreenResponse.code == 200) {
                    // Blogs
                    binding.swipeRefresh.setRefreshing(false);
                    binding.recyclerViewBlogs.setVisibility(View.VISIBLE);
                    blogList.addAll(homeScreenResponse.data.getBlogs());
                    adapterBlogs.notifyDataSetChanged();
                } else {
                    binding.swipeRefresh.setRefreshing(false);
                    binding.imgEmptyBox.setVisibility(View.VISIBLE);
                }
            } else {
                binding.swipeRefresh.setRefreshing(false);
                binding.imgEmptyBox.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBlogClicked(Blog blog) {
        Bundle bundle = new Bundle();
        bundle.putString("title", blog.getHeading());
        bundle.putString("desc", blog.getDescription());
        bundle.putString("service", blog.getService_name());
        bundle.putString("image", blog.getImage());
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_blog_to_blogInfoFragment, bundle);
    }
}