package com.android.fahim.livewallpaper.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.fahim.livewallpaper.Fragment.CategoryFragment;
import com.android.fahim.livewallpaper.Fragment.RecentsFragment;
import com.android.fahim.livewallpaper.Fragment.TrendingFragment;


/**
 * Created by HSBC on 21-02-2018.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {
    private Context context;

    public MyFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0)
            return CategoryFragment.getInstance();
        else if (position == 1)
            return TrendingFragment.getInstance();
        else if (position == 2)
            return new RecentsFragment(context);
        else return null;

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0: {
                return "Category";

            }
            case 1:
                return "Trending";
            case 2:
                return "Recents";
        }
        return "";
    }
}
