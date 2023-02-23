package com.axepert.onetouch.ui.blog;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.axepert.onetouch.BuildConfig;
import com.axepert.onetouch.databinding.FragmentAddBlogBinding;
import com.axepert.onetouch.utilities.FileUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddBlogFragment extends Fragment {
    private FragmentAddBlogBinding binding;
    String filePath = "";
    MultipartBody.Part filePart = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddBlogBinding.inflate(getLayoutInflater(), container, false);
        setListener();
        return binding.getRoot();
    }

    private void setListener() {
        binding.cvAddImage.setOnClickListener(v -> {
            if (hasPermission()) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent);
            } else {
                permissionsLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            }
        });
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());
    }

    private void addBlog() {
        try {
            File file = new File(Uri.parse(filePath).getPath());
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));

            if (filePath.isEmpty()) {
                filePart = null;
            } else {
                filePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
            }



        } catch (Exception e) {
            e.getMessage();
        }
    }

    private boolean hasPermission() {
        return requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
               if (result != null) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri uri = data.getData();
                        binding.imgBlogImage.setImageURI(uri);
                        filePath = FileUtils.getPath(requireActivity().getApplicationContext(), uri);
                        Log.d("Filepath", filePath);
                    }
               }
            });

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