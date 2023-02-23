package com.axepert.onetouch.ui.sellerdetails;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.FragmentSellerDetailsBinding;
import com.axepert.onetouch.requests.AddReviewRequest;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class SellerDetailsFragment extends Fragment {
    private FragmentSellerDetailsBinding binding;
    private SellerDetailsViewModel viewModel;
    String id, url;
    MediaController mediaController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSellerDetailsBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(this).get(SellerDetailsViewModel.class);
        setSellerDetails();
        setListener();
        return binding.getRoot();
    }

    private void setListener() {
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());
        binding.btnReview.setOnClickListener(v -> {
            if (isValid()) {
                addReview();
            }
        });
        if (!url.isEmpty()) {
            Uri uri = Uri.parse(url);
            binding.videoView.setVideoURI(uri);
            mediaController = new MediaController(requireActivity());
            mediaController.setAnchorView(binding.videoView);
            mediaController.setMediaPlayer(binding.videoView);
//        binding.videoView.setMediaController(mediaController);
            binding.videoView.start();

            binding.videoView.setOnPreparedListener(mp -> binding.videoProgress.setVisibility(View.GONE));
            binding.videoView.setOnCompletionListener(mp -> binding.videoView.start());
            binding.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    binding.videoLayout.setVisibility(View.GONE);
                    return true;
                }
            });
        } else {
            binding.videoLayout.setVisibility(View.GONE);
        }
    }

    private void setSellerDetails() {
        assert getArguments() != null;
        id = getArguments().getString("id");
        binding.toolbar.setTitle(getArguments().getString("name"));
        binding.tvShopName.setText(getArguments().getString("shop_name"));
        binding.tvShopAddress.setText(getArguments().getString("shop_add"));
        binding.tvContactNo.setText(getArguments().getString("contact"));
        binding.tvEmailAddress.setText(getArguments().getString("email"));
        binding.tvDescription.setText(getArguments().getString("desc"));
        url = getArguments().getString("seller_video");
        if (!getArguments().getString("image").isEmpty()) {
            Picasso.get().load(getArguments().getString("image")).into(binding.imgSeller, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    binding.imgSeller.setImageResource(R.drawable.ic_launcher_background);
                }
            });
        }
    }

    private boolean isValid() {
        if (Objects.requireNonNull(binding.etName.getText()).toString().isEmpty()) {
            showToast("Enter your name");
            return false;
        } else if (Objects.requireNonNull(binding.etContact.getText()).toString().isEmpty()) {
            showToast("Enter your contact number");
            return false;
        } else if (binding.etContact.getText().toString().length() != 10) {
            showToast("Enter correct contact number");
            return false;
        } else if (Objects.requireNonNull(binding.etReview.getText()).toString().isEmpty()) {
            showToast("Enter your review");
            return false;
        } else {
            return true;
        }
    }

    private void addReview() {
        AddReviewRequest addReviewRequest = new AddReviewRequest(
                id,
                Objects.requireNonNull(binding.etName.getText()).toString(),
                Objects.requireNonNull(binding.etEmail.getText()).toString(),
                Objects.requireNonNull(binding.etContact.getText()).toString(),
                Objects.requireNonNull(binding.etReview.getText()).toString());

        viewModel.addReview(addReviewRequest).observe(requireActivity(), addReviewResponse -> {
            if (addReviewResponse != null) {
                if (addReviewResponse.code == 200) {
                    showToast("Thanks for your review");
                    binding.etName.setText(null);
                    binding.etEmail.setText(null);
                    binding.etContact.setText(null);
                    binding.etReview.setText(null);
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.videoView.stopPlayback();
    }
}