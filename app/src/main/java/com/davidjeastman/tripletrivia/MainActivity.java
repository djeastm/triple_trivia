package com.davidjeastman.tripletrivia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.main_view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) return ProfileFragment.newInstance();
                else if (position == 1) return SettingsFragment.newInstance();
                else if (position == 2) return AboutFragment.newInstance();
                else return null;
            }

            @Override
            public int getCount() {
                return 3;
            } // Change to add other tabs

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.profile_label);
                    case 1:
                        return getString(R.string.settings_label);
                    case 2:
                        return getString(R.string.about_label);
                }

                return null;
            }
        });

    }
}
