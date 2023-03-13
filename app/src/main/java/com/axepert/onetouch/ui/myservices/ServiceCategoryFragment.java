package com.axepert.onetouch.ui.myservices;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.axepert.onetouch.R;
import com.axepert.onetouch.adapters.AdapterMyServiceCategory;
import com.axepert.onetouch.databinding.FragmentServiceCategoryBinding;
import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.MyServiceCategoryRequest;
import com.axepert.onetouch.responses.MyServiceCategoryResponse;
import com.axepert.onetouch.utilities.Constants;
import com.axepert.onetouch.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceCategoryFragment extends Fragment {
    private FragmentServiceCategoryBinding binding;
    private PreferenceManager preferenceManager;
    private List<MyServiceCategoryResponse.Datum> myServiceCatList;
    private AdapterMyServiceCategory adapterMyServiceCategory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentServiceCategoryBinding.inflate(getLayoutInflater(), container, false);
        init();
        getMyCategories();
        return binding.getRoot();
    }

    private void init() {
        preferenceManager = new PreferenceManager(requireContext());
        binding.recyclerViewServiceCategory.setHasFixedSize(true);
        myServiceCatList = new ArrayList<>();
        adapterMyServiceCategory = new AdapterMyServiceCategory(myServiceCatList);
        binding.recyclerViewServiceCategory.setAdapter(adapterMyServiceCategory);
    }

    private void getMyCategories() {
        try {

            MyServiceCategoryRequest myServiceCategoryRequest = new MyServiceCategoryRequest(preferenceManager.getString(Constants.KEY_USER_ID));

            ApiClient.getRetrofit().create(ApiService.class).myServiceCategory(myServiceCategoryRequest)
                    .enqueue(new Callback<MyServiceCategoryResponse>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onResponse(@NonNull Call<MyServiceCategoryResponse> call, @NonNull Response<MyServiceCategoryResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().code == 200) {
                                    myServiceCatList.addAll(response.body().data);
                                    adapterMyServiceCategory.notifyDataSetChanged();
                                    binding.progress.setVisibility(View.GONE);
                                    binding.recyclerViewServiceCategory.setVisibility(View.VISIBLE);
                                } else {
                                    binding.progress.setVisibility(View.GONE);
                                    binding.imgEmptyBox.setVisibility(View.VISIBLE);
                                }
                            } else {
                                binding.progress.setVisibility(View.GONE);
                                binding.imgEmptyBox.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<MyServiceCategoryResponse> call, @NonNull Throwable t) {
                            Toast.makeText(requireContext(), "Failed due to : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            binding.progress.setVisibility(View.GONE);
                            binding.imgEmptyBox.setVisibility(View.VISIBLE);
                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            binding.progress.setVisibility(View.GONE);
            binding.imgEmptyBox.setVisibility(View.VISIBLE);
        }
    }

}