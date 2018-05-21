package es.dadm.practica2.Adapters;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.dadm.practica2.R;
import es.dadm.practica2.fragmentTiles;

public class TabsAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();

    public TabsAdapter(FragmentManager fm){
        super(fm);
    }

    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        notifyDataSetChanged();
    }

    public void removeTabPage(int position) {
        mFragmentList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
