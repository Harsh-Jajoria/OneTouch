package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemReviewsBinding;
import com.axepert.onetouch.responses.ReviewsResponse;

import java.util.List;

public class AdapterReviews extends RecyclerView.Adapter<AdapterReviews.ReviewsViewHolder> {
    private List<ReviewsResponse.Datum> reviewsList;

    public AdapterReviews(List<ReviewsResponse.Datum> reviewsList) {
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewsViewHolder(
                ItemReviewsBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        holder.setData(reviewsList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    static class ReviewsViewHolder extends RecyclerView.ViewHolder {
        ItemReviewsBinding binding;

        ReviewsViewHolder(ItemReviewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(ReviewsResponse.Datum reviews) {
            binding.tvName.setText(reviews.getName());
            binding.tvReview.setText(reviews.getReview());
        }
    }

}
