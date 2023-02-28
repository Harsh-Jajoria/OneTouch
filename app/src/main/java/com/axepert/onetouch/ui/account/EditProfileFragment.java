package com.axepert.onetouch.ui.account;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.axepert.onetouch.BuildConfig;
import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterServiceCategory;
import com.axepert.onetouch.databinding.FragmentEditProfileBinding;
import com.axepert.onetouch.listeners.ServiceCategoryListener;
import com.axepert.onetouch.requests.EditDealerProfileRequest;
import com.axepert.onetouch.requests.EditProfileRequest;
import com.axepert.onetouch.responses.MyServiceCategoryResponse;
import com.axepert.onetouch.utilities.Constants;
import com.axepert.onetouch.utilities.PreferenceManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditProfileFragment extends Fragment implements ServiceCategoryListener {
    private FragmentEditProfileBinding binding;
    private EditProfileViewModel viewModel;
    private PreferenceManager preferenceManager;
    private List<MyServiceCategoryResponse.Datum> serviceCategory;
    private AdapterServiceCategory adapterServiceCategory;
    private BottomSheetDialog bottomSheetDialog;
    private String catId, subCatId, encodedImage = "";
    private boolean catSelect;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(getLayoutInflater(), container, false);
        init();
        getDataFromPref();
        setListeners();
        return binding.getRoot();
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
        preferenceManager = new PreferenceManager(requireActivity());

        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void getDataFromPref() {
        binding.etName.setText(preferenceManager.getString(Constants.KEY_USERNAME));
        binding.etContact.setText(preferenceManager.getString(Constants.KEY_PHONE));
        if (preferenceManager.getString(Constants.KEY_ROLE).equals("user")) {
            binding.llDealer.setVisibility(View.GONE);
            binding.layoutProfile.setVisibility(View.GONE);
        } else {
            binding.llDealer.setVisibility(View.VISIBLE);
            binding.layoutProfile.setVisibility(View.VISIBLE);
            binding.etShopName.setText(preferenceManager.getString(Constants.KEY_SHOP_NAME));
            binding.etAddress.setText(preferenceManager.getString(Constants.KEY_SHOP_ADD));
            binding.etDescription.setText(preferenceManager.getString(Constants.KEY_DESC));
            binding.etGSTIN.setText(preferenceManager.getString(Constants.KEY_GST));
            catId = preferenceManager.getString(Constants.KEY_CAT_ID);
            subCatId = preferenceManager.getString(Constants.KEY_SUB_CAT_ID);
            binding.etCategory.setText(preferenceManager.getString(Constants.KEY_CAT_NAME));
            binding.etSubCategory.setText(preferenceManager.getString(Constants.KEY_SUB_CAT_NAME));
            Picasso.get().load(preferenceManager.getString(Constants.KEY_IMAGE)).into(binding.imgProfile);
        }
    }

    private void setListeners() {
        binding.btnUpdate.setOnClickListener(v -> {
            if (preferenceManager.getString(Constants.KEY_ROLE).equals("user")) {
                editUserProfile();
            } else {
                editDealerProfile();
            }
        });
        binding.etCategory.setOnClickListener(v -> {
            catSelect = true;
            getDropDownData();
        });
        binding.etSubCategory.setOnClickListener(v -> {
            catSelect = false;
            getSubCat(catId);
        });
        binding.imgEditProfile.setOnClickListener(v -> {
            if (hasPermission()) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent);
            } else {
                permissionsLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            }
        });
    }

    private void editUserProfile() {
        progressDialog.show();
        EditProfileRequest editProfileRequest = new EditProfileRequest(
                "onetouch.com",
                binding.etName.getText().toString(),
                binding.etContact.getText().toString(),
                preferenceManager.getString(Constants.KEY_USER_ID));

        viewModel.editUserProfile(editProfileRequest).observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                if (response.code == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(requireActivity(), response.status, Toast.LENGTH_SHORT).show();
                    preferenceManager.putString(Constants.KEY_USERNAME, binding.etName.getText().toString());
                    preferenceManager.putString(Constants.KEY_PHONE, binding.etContact.getText().toString());
                    Navigation.findNavController(binding.getRoot()).popBackStack();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(requireActivity(), response.status, Toast.LENGTH_SHORT).show();
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(requireActivity(), "Something went wrong! Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editDealerProfile() {
        progressDialog.show();
        EditDealerProfileRequest editDealerProfileRequest = new EditDealerProfileRequest(
                preferenceManager.getString(Constants.KEY_USER_ID),
                binding.etName.getText().toString().trim(),
                binding.etContact.getText().toString().trim(),
                binding.etShopName.getText().toString().trim(),
                catId,
                subCatId,
                binding.etAddress.getText().toString().trim(),
                binding.etDescription.getText().toString().trim(),
                binding.etGSTIN.getText().toString().trim(),
                encodedImage
        );

        viewModel.editDealerProfile(editDealerProfileRequest).observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                if (response.code == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(requireActivity(), response.status, Toast.LENGTH_SHORT).show();
                    preferenceManager.putString(Constants.KEY_USERNAME, binding.etName.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_IMAGE, response.image);
                    preferenceManager.putString(Constants.KEY_PHONE, binding.etContact.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_SHOP_NAME, binding.etShopName.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_SHOP_ADD, binding.etAddress.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_GST, binding.etGSTIN.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_DESC, binding.etDescription.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_CAT_ID, catId);
                    preferenceManager.putString(Constants.KEY_SUB_CAT_ID, subCatId);
                    preferenceManager.putString(Constants.KEY_CAT_NAME, binding.etCategory.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_SUB_CAT_NAME, binding.etSubCategory.getText().toString().trim());
                    Navigation.findNavController(binding.getRoot()).popBackStack();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(requireActivity(), response.status, Toast.LENGTH_SHORT).show();
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(requireActivity(), "Something went wrong! Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getDropDownData() {
        serviceCategory = new ArrayList<>();
        adapterServiceCategory = new AdapterServiceCategory(serviceCategory, this);

        viewModel.getServiceCategory(preferenceManager.getString(Constants.KEY_USER_ID)).observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                if (response.code == 200) {
                    serviceCategory.addAll(response.data);
                    adapterServiceCategory.notifyDataSetChanged();
                    dropdownBottomSheet("Select Category");
                } else {
                    Toast.makeText(requireActivity(), response.status, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getSubCat(String id) {
        serviceCategory = new ArrayList<>();
        adapterServiceCategory = new AdapterServiceCategory(serviceCategory, this);

        viewModel.getServiceSubCategory(id).observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                if (response.code == 200) {
                    serviceCategory.addAll(response.data);
                    adapterServiceCategory.notifyDataSetChanged();
                    dropdownBottomSheet("Select Sub Category");
                } else {
                    Toast.makeText(requireActivity(), response.status, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dropdownBottomSheet(String title) {
        bottomSheetDialog = new BottomSheetDialog(requireActivity());
        BottomSheetBehavior<View> bottomSheetBehavior;
        View layout = LayoutInflater.from(requireActivity()).inflate(R.layout.bottomsheet_drop_down, null);
        bottomSheetDialog.setContentView(layout);
        bottomSheetBehavior = BottomSheetBehavior.from((View) layout.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.setCancelable(true);

        MaterialToolbar toolbar = bottomSheetDialog.findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(title);
        ImageView imgClose = bottomSheetDialog.findViewById(R.id.imgClose);
        assert imgClose != null;
        imgClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        LinearLayout layoutProjectBottomSheet = bottomSheetDialog.findViewById(R.id.parentLL);
        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.recyclerView);
        assert layoutProjectBottomSheet != null;
        layoutProjectBottomSheet.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapterServiceCategory);
        bottomSheetDialog.show();
    }

    @Override
    public void onClick(MyServiceCategoryResponse.Datum data) {
        if (catSelect) {
            catId = data.getId();
            binding.etCategory.setText(data.getName());
            Log.d("CatID", "onClick: " + catId);
        } else {
            subCatId = data.getId();
            binding.etSubCategory.setText(data.getName());
            Log.d("CatID", "SubCat Id: " + subCatId);
        }
        bottomSheetDialog.dismiss();
    }


    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result != null) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri uri = data.getData();
                        try {
                            Bitmap photo = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                            binding.imgProfile.setImageBitmap(photo);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] bytes = stream.toByteArray();
                            encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                            Log.d("EncodedImage", "EncodedImage : " + encodedImage);
                            binding.imgProfile.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private boolean hasPermission() {
        return requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
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