package com.example.riri;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class fragment_container_slider extends FragmentPagerAdapter {

    final int number_Of_tabs;

    public fragment_container_slider(@NonNull FragmentManager fm, int number_Of_tabs) {
        super(fm, number_Of_tabs);
        this.number_Of_tabs = number_Of_tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                return new prev_reminder();
            case 1:
                return new new_reminder();
            case 2:
                return new setting_reminder();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return number_Of_tabs;
    }
}
