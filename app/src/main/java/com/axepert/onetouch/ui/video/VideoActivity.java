package com.axepert.onetouch.ui.video;

import android.os.Bundle;

import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;

import com.axepert.onetouch.databinding.ActivityVideoBinding;

public class VideoActivity extends AppCompatActivity {
    private ActivityVideoBinding binding;

    @Override
    @OptIn(markerClass = UnstableApi.class)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String url = getIntent().getStringExtra("url");

        // Setup exoplayer
        ExoPlayer player = new ExoPlayer.Builder(this).build();

        binding.playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(url);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

    }
}