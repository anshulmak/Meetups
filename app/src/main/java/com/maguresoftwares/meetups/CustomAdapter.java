package com.maguresoftwares.meetups;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CustomAdapter extends FragmentStatePagerAdapter {

    private final int ITEMS = 1;

    CustomAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return ITEMS;
    }

    @Override
    public Fragment getItem(int position) {

        return new loginfragment1();
    }
}
