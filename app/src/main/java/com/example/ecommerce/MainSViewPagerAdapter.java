package com.example.ecommerce;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MainSViewPagerAdapter extends FragmentPagerAdapter {
    public MainSViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {

        this.uid = uid;

    }

    public MainSViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {

        Fragment A = new HomeFrag();
            Bundle bundle = new Bundle();
            bundle.putString("UID",uid);
        A.setArguments(bundle);
        return A;

        } else if (position == 1) {
            return new Cart();
        } else {
            return new OrderHistory();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if(position==0){
            return "Home";
        }
       else if(position==1){
           return "Cart";
        }
       else{
           return "Orders";
        }
    }
}
