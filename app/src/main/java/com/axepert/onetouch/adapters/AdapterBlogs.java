package com.axepert.onetouch.adapters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.ItemBlogsBinding;
import com.axepert.onetouch.listeners.BlogListener;
import com.axepert.onetouch.responses.Blog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterBlogs extends RecyclerView.Adapter<AdapterBlogs.BlogViewHolder>{
    private List<Blog> blogList;
    private BlogListener blogListener;

    public AdapterBlogs(List<Blog> blogList, BlogListener blogListener) {
        this.blogList = blogList;
        this.blogListener = blogListener;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BlogViewHolder(
                ItemBlogsBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        holder.setData(blogList.get(position));
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    class BlogViewHolder extends RecyclerView.ViewHolder {
        ItemBlogsBinding binding;

        BlogViewHolder(ItemBlogsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setData(Blog blog) {
            binding.tvBlogTitle.setText(blog.getHeading());
            binding.tvServiceCategory.setText(blog.getService_name());
            binding.tvDescription.setText(Html.fromHtml(blog.getDescription(), Html.FROM_HTML_MODE_LEGACY));
            binding.imgServices.setAlpha(0f);
            Picasso.get().load(blog.getImage()).noFade().into(binding.imgServices, new Callback() {
                @Override
                public void onSuccess() {
                    binding.imgServices.animate().alpha(1f).setDuration(300).start();
                }

                @Override
                public void onError(Exception e) {
                    binding.imgServices.animate().alpha(1f).setDuration(300).start();
                    binding.imgServices.setImageResource(R.drawable.ic_launcher_background);
                }
            });
            binding.getRoot().setOnClickListener(v -> blogListener.onBlogClicked(blog));
        }
    }
}
