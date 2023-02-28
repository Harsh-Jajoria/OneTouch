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
import com.axepert.onetouch.adapters.AdapterMyServiceSubCategory;
import com.axepert.onetouch.databinding.FragmentServiceSubCategoryBinding;
import com.axepert.onetouch.network.ApiClient;
import com.axepert.onetouch.network.ApiService;
import com.axepert.onetouch.requests.MyServiceCategoryRequest;
import com.axepert.onetouch.responses.MyServiceSubCategoryResponse;
import com.axepert.onetouch.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceSubCategoryFragment extends Fragment {
    private FragmentServiceSubCategoryBinding binding;
    private PreferenceManager preferenceManager;
    private List<MyServiceSubCategoryResponse.Datum> subCatList;
    private AdapterMyServiceSubCategory adapterMyServiceSubCategory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentServiceSubCategoryBinding.inflate(getLayoutInflater(), container, false);
        init();
        getSubCat();
        return binding.getRoot();
    }

    private void init() {
        preferenceManager = new PreferenceManager(requireActivity());
        binding.recyclerViewServiceSubCategory.setHasFixedSize(true);
        subCatList = new ArrayList<>();
        adapterMyServiceSubCategory = new AdapterMyServiceSubCategory(subCatList);
        binding.recyclerViewServiceSubCategory.setAdapter(adapterMyServiceSubCategory);
    }

    private void getSubCat() {
        try {

            MyServiceCategoryRequest myServiceCategoryRequest = new MyServiceCategoryRequest("7");

            ApiClient.getRetrofit().create(ApiService.class).myServiceSubCategory(myServiceCategoryRequest)
                    .enqueue(new Callback<MyServiceSubCategoryResponse>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onResponse(@NonNull Call<MyServiceSubCategoryResponse> call, @NonNull Response<MyServiceSubCategoryResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().code == 200) {
                                    subCatList.addAll(response.body().data);
                                    adapterMyServiceSubCategory.notifyDataSetChanged();
                                    binding.progress.setVisibility(View.GONE);
                                    binding.recyclerViewServiceSubCategory.setVisibility(View.VISIBLE);
                                } else {
                                    binding.progress.setVisibility(View.GONE);
                                }
                            } else {
                                binding.progress.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<MyServiceSubCategoryResponse> call, @NonNull Throwable t) {
                            binding.progress.setVisibility(View.GONE);
                            Toast.makeText(requireActivity(), "Failed due to : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();binding.progress.setVisibility(View.GONE);
            Toast.makeText(requireActivity(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}