package com.axepert.onetouch.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.databinding.ItemSearchResultBinding;
import com.axepert.onetouch.listeners.SearchListener;
import com.axepert.onetouch.responses.SearchProductResponse;

import java.util.List;

public class AdapterSearchResult extends RecyclerView.Adapter<AdapterSearchResult.SearchResultViewHolder>{
    List<SearchProductResponse.Datum> searchList;
    SearchListener searchListener;

    public AdapterSearchResult(List<SearchProductResponse.Datum> searchList, SearchListener searchListener) {
        this.searchList = searchList;
        this.searchListener = searchListener;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchResultViewHolder(
                ItemSearchResultBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        holder.setData(searchList.get(position));
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder {
        ItemSearchResultBinding binding;

        SearchResultViewHolder(ItemSearchResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(SearchProductResponse.Datum search) {
            binding.tvSearchResult.setText(search.getName());
            binding.tvSearchResult.setOnClickListener(v -> searchListener.onSearchItemClicked(search));
        }
    }

}
