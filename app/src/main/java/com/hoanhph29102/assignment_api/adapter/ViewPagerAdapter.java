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
    private FoodFragment foodFragment;
    private CartFragment cartFragment;
    private OrderFragment orderFragment;
    private UserFragment userFragment;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, FoodFragment foodFragment, CartFragment cartFragment, OrderFragment orderFragment, UserFragment userFragment) {
        super(fragmentActivity);
        this.foodFragment = foodFragment;
        this.cartFragment = cartFragment;
        this.orderFragment = orderFragment;
        this.userFragment = userFragment;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return foodFragment;
            case 1:
                return cartFragment;
            case 2:
                return orderFragment;
            case 3:
                return userFragment;
            default:
                return foodFragment;

        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
