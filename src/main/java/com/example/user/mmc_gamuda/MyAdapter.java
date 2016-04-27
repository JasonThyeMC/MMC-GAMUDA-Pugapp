package com.example.user.mmc_gamuda;



import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;;

/**
 * Created by c766847 on 28/01/2015.
 */

public class MyAdapter extends FragmentPagerAdapter {

    private static final int FRAGMENT_ONE = 0;
    private static final int FRAGMENT_TWO = 1;
    private static final int FRAGMENT_THREE = 2;
    private int access;
    SharedPreferences pref;

    public MyAdapter(FragmentManager fm, int accessLevel, SharedPreferences pref) {

        super(fm);
        access = accessLevel;
        this.pref = pref;
    }

    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case FRAGMENT_ONE:
                return new reportFrag();
            case FRAGMENT_TWO:
                return new RingFrag();
            case FRAGMENT_THREE:
                return new FeedFrag();
            default:
                return new reportFrag();
        }

    }

    @Override
    public int getCount() {

        return access;
    }

    public void setCount(int count){
         access = count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case FRAGMENT_ONE:
                return "Frag1";
            case FRAGMENT_TWO:
                return "Frag2";
            case FRAGMENT_THREE:
                return "Frag3";
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


/*
    @Override
    public int getItemPosition(Object object) {
        boolean loggedIn = object instanceof RingFrag;

        Log.i("Adapter", String.valueOf(object.toString().contains("RingFrag")));
        Log.i("Adapter", String.valueOf(pref.getBoolean("authState",false)));

        if(object.toString().contains("RingFrag") && !pref.getBoolean("authState",false)){
            Log.i("Adapter", "Ran");
            return POSITION_NONE;
        } else if (object.toString().contains("webFrag") && !pref.getBoolean("authState",false)){
            return POSITION_NONE;
        } else {
            return POSITION_UNCHANGED;
        }

    }*/
}
