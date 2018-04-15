package com.example.expeditionhacks2018;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Greg on 4/14/18.
 */

    public class PageAdapter extends FragmentStatePagerAdapter {
    protected int numTabs = 0;
    MapReportingTool mapReportingTool;
    MapTrackYourself mapTrackYourself;
    SocialFragment socialFragment;

    public PageAdapter(FragmentManager fm, int numTabs, MapTrackYourself mty, MapReportingTool mr)
    {
        super(fm);
        this.numTabs = numTabs;
        this.mapReportingTool = mr;
        this.mapTrackYourself = mty;
        //do whatever here

    }



    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return mapTrackYourself;
            case 1:
                return mapReportingTool;
            case 2:
                socialFragment = new SocialFragment();
                return socialFragment;
            default:
                return null;
        }
    }



    @Override
    public int getCount() {
        return numTabs;
    }
}
