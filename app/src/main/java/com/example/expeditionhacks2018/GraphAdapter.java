package com.example.expeditionhacks2018;

/**
 * Created by Greg on 2/3/18.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Greg on 12/2/17.
 */

public class GraphAdapter extends FragmentStatePagerAdapter {
    int numOftabs;
    PieChart pieChart;
    BarChart barChart;

    public GraphAdapter (FragmentManager fm, int numOfTabs, PieChart pieChart, BarChart barChart)
    {
        super(fm);
        this.numOftabs = numOfTabs;
        this.barChart = barChart;
        this.pieChart = pieChart;


    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return pieChart;
            case 1:
                return barChart;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOftabs;
    }
}
