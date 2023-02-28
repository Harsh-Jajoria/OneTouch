package com.axepert.onetouch.ui.blog;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
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
import com.axepert.onetouch.databinding.FragmentAddBlogBinding;
import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.AddBlogRequest;
import com.axepert.onetouch.responses.PlaceOrderResponse;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBlogFragment extends Fragment {
    private FragmentAddBlogBinding binding;
    String encodedString, category, TAG = "AddBlogFragment";
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddBlogBinding.inflate(getLayoutInflater(), container, false);
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        if (getArguments() != null) {
            category = getArguments().getString("cat");
        }

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
        binding.btnAddBlog.setOnClickListener(v -> {
            if (isValid()) {
                addBlog();
            }
        });
    }

    private void addBlog() {
        progressDialog.show();
        try {

            AddBlogRequest addBlogRequest = new AddBlogRequest(
                    Objects.requireNonNull(binding.etName.getText()).toString(),
                    Objects.requireNonNull(binding.etEmail.getText()).toString(),
                    Objects.requireNonNull(binding.etContact.getText()).toString(),
                    Objects.requireNonNull(binding.etTitle.getText()).toString(),
                    Objects.requireNonNull(binding.etDescription.getText()).toString(),
                    category,
                    encodedString
            );

            ApiClient.getRetrofit().create(ApiService.class).addBlog(addBlogRequest).enqueue(new Callback<PlaceOrderResponse>() {
                @Override
                public void onResponse(@NonNull Call<PlaceOrderResponse> call, @NonNull Response<PlaceOrderResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().code == 200) {
                            progressDialog.dismiss();
                            showCustomDialog("Blog Added", "Your blog added successfully and sent for approval.", "Okay", (dialog, which) -> {
                                dialog.dismiss();
                                Navigation.findNavController(binding.getRoot()).popBackStack();
                            }, null, null, false);
                        } else {
                            progressDialog.dismiss();
                            Snackbar.make(binding.getRoot(), response.body().status, Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Snackbar.make(binding.getRoot(), "Unable to add blog right now.", Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlaceOrderResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(requireActivity(), "Failed due to : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(requireActivity(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onFailure: " + e.getMessage());
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
                        try {
                            Bitmap photo = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                            binding.imgBlogImage.setImageBitmap(photo);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] bytes = stream.toByteArray();
                            encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
                            binding.imgBlogImage.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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

    private boolean isValid() {
        if (Objects.requireNonNull(binding.etName.getText()).toString().isEmpty()) {
            binding.tilName.setError("Enter your name");
            binding.tilName.requestFocus();
            return false;
        } else if (Objects.requireNonNull(binding.etEmail.getText()).toString().isEmpty()) {
            binding.tilName.setErrorEnabled(false);
            binding.tilEmail.setError("Enter your email");
            binding.tilEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString()).matches()) {
            binding.tilEmail.setError("Email address is not correct");
            binding.tilEmail.requestFocus();
            return false;
        } else if (Objects.requireNonNull(binding.etContact.getText()).toString().isEmpty()) {
            binding.tilEmail.setErrorEnabled(false);
            binding.tilContact.setError("Enter your contact number");
            binding.tilContact.requestFocus();
            return false;
        } else if (binding.etContact.length() != 10) {
            binding.tilContact.setError("Enter correct contact number.");
            binding.tilContact.requestFocus();
            return false;
        } else if (Objects.requireNonNull(binding.etTitle.getText()).toString().isEmpty()) {
            binding.tilContact.setErrorEnabled(false);
            binding.tilTitle.setError("Enter blog title");
            binding.tilTitle.requestFocus();
            return false;
        } else if (Objects.requireNonNull(binding.etDescription.getText()).toString().isEmpty()) {
            binding.tilTitle.setErrorEnabled(false);
            binding.tilDescription.setError("Enter blog description");
            binding.tilDescription.requestFocus();
            return false;
        } else {
            binding.tilDescription.setErrorEnabled(false);
            return true;
        }
    }

}