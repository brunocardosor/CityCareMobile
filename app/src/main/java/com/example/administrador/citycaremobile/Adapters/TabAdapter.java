package com.example.administrador.citycaremobile.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.administrador.citycaremobile.Fragments.FeedFragment;
import com.example.administrador.citycaremobile.Fragments.MapsFragment;
import com.example.administrador.citycaremobile.Fragments.ProfileFragment;

/**
 * Created by Administrador on 17/09/2017.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    private int numeroTabs;

    public TabAdapter(FragmentManager fm, int numeroTabs) {
        super(fm);
        this.numeroTabs = numeroTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                FeedFragment tab1 = new FeedFragment();
                return tab1;
            case 1:
                MapsFragment tab2 = new MapsFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numeroTabs;
    }
}
