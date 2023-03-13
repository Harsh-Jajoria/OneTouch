package com.axepert.onetouch.ui.account;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.axepert.onetouch.BuildConfig;
import com.axepert.onetouch.R;
import com.axepert.onetouch.databinding.FragmentEditVideoBinding;
import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.responses.PlaceOrderResponse;
import com.axepert.onetouch.utilities.Constants;
import com.axepert.onetouch.utilities.PreferenceManager;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditVideoFragment extends Fragment {
    private FragmentEditVideoBinding binding;
    private PreferenceManager preferenceManager;
    private ProgressDialog progressDialog;
    String path;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditVideoBinding.inflate(getLayoutInflater(), container, false);
        init();
        setListener();
        return binding.getRoot();
    }

    private void init() {
        preferenceManager = new PreferenceManager(requireActivity());
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Please wait your video is uploading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void setListener() {
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        binding.btnChooseFile.setOnClickListener(v -> {
            if (hasPermission()) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                resultLauncher.launch(intent);
            } else {
                permissionsLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            }
        });
        binding.btnUploadFile.setOnClickListener(v -> uploadVideo(path));
        binding.imgPlay.setOnClickListener(v -> {
            if (binding.videoView.isPlaying()) {
                binding.imgPlay.setImageResource(R.drawable.ic_pause);
                binding.videoView.pause();
            } else {
                binding.imgPlay.setImageResource(R.drawable.ic_play);
                binding.videoView.start();
            }
        });
    }

    private final ActivityResultLauncher<String[]> permissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        Log.d("BlogFragment", "onActivityResult: " + result);
        boolean allGranted = true;
        for (String key : result.keySet()) {
            allGranted = allGranted && Boolean.TRUE.equals(result.get(key));
        }

        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showCustomDialog("Storage Permission", "Read storage permission is required to upload image in blogs.", "Ok", (dialogInterface, i) -> {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                startActivity(intent);
            }, "Cancel", null, false);
        }
    });

    private boolean hasPermission() {
        return requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result != null) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri uri = data.getData();
                        String[] filePathColumn = {MediaStore.Video.Media.DATA};
                        Cursor cursor = requireActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        path = cursor.getString(columnIndex);
                        File file = new File(path);
                        int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
                        Log.d("fileSize", "" + file_size);
                        if (file_size <= 5120) { // Comparing file size in MB
                            binding.videoView.setVideoPath(path);
                            binding.videoView.stopPlayback();
                            binding.btnUploadFile.setEnabled(true);
                        } else {
                            Toast.makeText(requireActivity(), "Selected file size too large.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    private void uploadVideo(String videoPath) {
        progressDialog.show();
        File videoFile = new File(videoPath);
        RequestBody requestBody = RequestBody.create(videoFile ,MediaType.parse("video/*"));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("seller_video", videoFile.getName(), requestBody);

        ApiClient.getRetrofit().create(ApiService.class).uploadVideo(
                RequestBody.create(preferenceManager.getString(Constants.KEY_USER_ID), MediaType.parse("text/plain")),
                filePart).enqueue(new Callback<PlaceOrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceOrderResponse> call, @NonNull Response<PlaceOrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().code == 200) {
                        Toast.makeText(requireActivity(), response.body().status, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Navigation.findNavController(binding.getRoot()).popBackStack();
                    } else {
                        Toast.makeText(requireActivity(), response.body().status, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaceOrderResponse> call, @NonNull Throwable t) {
                Toast.makeText(requireActivity(), "Failed due to : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    void showCustomDialog(String title, String message, String positiveBtnTitle, DialogInterface.OnClickListener positiveListener, String negativeBtnTitle, DialogInterface.OnClickListener negativeListener, boolean isCancelable) {
        new AlertDialog.Builder(requireActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveBtnTitle, positiveListener)
                .setNegativeButton(negativeBtnTitle, negativeListener)
                .setCancelable(isCancelable)
                .show();
    }

}