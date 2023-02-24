package com.axepert.onetouch.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.axepert.onetouch.ui.myservices.ServiceCategoryFragment;
import com.axepert.onetouch.ui.myservices.ServiceSubCategoryFragment;

public class AdapterMyServiceTabs extends FragmentStateAdapter {

    public AdapterMyServiceTabs(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new ServiceSubCategoryFragment();
        }
        return new ServiceCategoryFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
