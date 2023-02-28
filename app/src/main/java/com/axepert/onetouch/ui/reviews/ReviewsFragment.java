package com.axepert.onetouch.ui.reviews;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.axepert.onetouch.adapters.AdapterReviews;
import com.axepert.onetouch.databinding.FragmentReviewsBinding;
import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.MyServiceCategoryRequest;
import com.axepert.onetouch.responses.ReviewsResponse;
import com.axepert.onetouch.utilities.Constants;
import com.axepert.onetouch.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsFragment extends Fragment {
    private FragmentReviewsBinding binding;
    private PreferenceManager preferenceManager;
    private List<ReviewsResponse.Datum> reviewsList;
    private AdapterReviews adapterReviews;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReviewsBinding.inflate(getLayoutInflater(), container, false);
        init();
        getReviews();
        return binding.getRoot();
    }

    private void init() {
        preferenceManager = new PreferenceManager(requireActivity());
        binding.recyclerViewReviews.setHasFixedSize(true);
        reviewsList = new ArrayList<>();
        adapterReviews = new AdapterReviews(reviewsList);
        binding.recyclerViewReviews.setAdapter(adapterReviews);
        binding.recyclerViewReviews.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
    }

    private void getReviews() {
        try {

            MyServiceCategoryRequest myServiceCategoryRequest = new MyServiceCategoryRequest(preferenceManager.getString(Constants.KEY_USER_ID));

            ApiClient.getRetrofit().create(ApiService.class).myReviews(myServiceCategoryRequest).enqueue(new Callback<ReviewsResponse>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<ReviewsResponse> call, @NonNull Response<ReviewsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().code == 200) {
                            reviewsList.addAll(response.body().data);
                            adapterReviews.notifyDataSetChanged();
                            binding.progress.setVisibility(View.GONE);
                            binding.recyclerViewReviews.setVisibility(View.VISIBLE);
                        } else {
                            binding.progress.setVisibility(View.GONE);
                            binding.noReviews.setVisibility(View.VISIBLE);
                        }

                    } else {
                        binding.progress.setVisibility(View.GONE);
                        binding.noReviews.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ReviewsResponse> call, @NonNull Throwable t) {
                    binding.progress.setVisibility(View.GONE);
                    binding.noReviews.setVisibility(View.VISIBLE);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            binding.noReviews.setVisibility(View.VISIBLE);
            binding.progress.setVisibility(View.GONE);
        }
    }

}