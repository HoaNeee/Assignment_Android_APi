package com.hoanhph29102.assignment_api.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hoanhph29102.assignment_api.fragment.CartFragment;
import com.hoanhph29102.assignment_api.fragment.OrderFragment;
import com.hoanhph29102.assignment_api.fragment.FoodFragment;
import com.hoanhph29102.assignment_api.fragment.UserFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FoodFragment();
            case 1:
                return new CartFragment();
            case 2:
                return new OrderFragment();
            case 3:
                return new UserFragment();
            default:
                return new FoodFragment();

        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
