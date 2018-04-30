package com.android.sagot.mynews.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.sagot.mynews.Controllers.Fragments.BusinessFragment;
import com.android.sagot.mynews.Controllers.Fragments.MostPopularFragment;
import com.android.sagot.mynews.Controllers.Fragments.TopStoriesFragment;

public class PageAdapter extends FragmentPagerAdapter {

    // Default Constructor
    public PageAdapter(FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public int getCount() {
        return(3); // Number of page to show
    }

    @Override
    public Fragment getItem(int position) {
        // Page to return
        switch (position){
            case 0: //Page number 1
                return TopStoriesFragment.newInstance();
            case 1: //Page number 2
                return MostPopularFragment.newInstance();
            case 2: //Page number 3
                return BusinessFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: //Page number 1
                return "TOP STORIES";
            case 1: //Page number 2
                return "MOST POPULAR";
            case 2: //Page number 3
                return "BUSINESS";
            default:
                return null;
        }
    }
}
