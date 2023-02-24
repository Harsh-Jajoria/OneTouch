package com.axepert.onetouch.ui.blog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.FragmentBlogInfoBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class BlogInfoFragment extends Fragment {
    private FragmentBlogInfoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBlogInfoBinding.inflate(getLayoutInflater(), container, false);
        setBlogDetails();
        setListener();
        return binding.getRoot();
    }

    private void setListener() {
        binding.imgBack.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());
        binding.btnAddBlog.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("cat", binding.tvServiceCategory.getText().toString());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_blogInfoFragment_to_addBlogFragment, bundle);
        });
    }

    private void setBlogDetails() {
        loading(true);
        assert getArguments() != null;
        binding.tvBlogTitle.setText(getArguments().getString("title"));
        binding.tvDescription.setText(getArguments().getString("desc"));
        binding.tvServiceCategory.setText(getArguments().getString("service"));
        binding.imgBanner.setAlpha(0f);
        Picasso.get().load(getArguments().getString("image")).noFade().into(binding.imgBanner, new Callback() {
            @Override
            public void onSuccess() {
                binding.imgBanner.animate().alpha(1f).setDuration(300).start();
                loading(false);
            }

            @Override
            public void onError(Exception e) {
                binding.imgBanner.animate().alpha(1f).setDuration(300).start();
                binding.imgBanner.setImageResource(R.drawable.ic_launcher_background);
                loading(false);
            }
        });
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            binding.scrollView.setVisibility(View.GONE);
            binding.loading.setVisibility(View.VISIBLE);
        } else {
            binding.scrollView.setVisibility(View.VISIBLE);
            binding.loading.setVisibility(View.GONE);
        }
    }

}