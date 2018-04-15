package com.example.expeditionhacks2018;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;

import java.util.HashMap;

public class graphActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    HashMap<LatLng, AnalysisResults> analysisRegionHashMap = new HashMap<>();
    DataRelay dataRelay;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        dataRelay = (DataRelay) getApplicationContext();
        analysisRegionHashMap = dataRelay.analysisResults;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        TabLayout tabLayout = (TabLayout) findViewById(R.id.graph_tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Pie Chart"));
        tabLayout.addTab(tabLayout.newTab().setText("Bar Chart"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.graph_pager);
        //ASK WHY IT CRASHES WHEN I REMOVE OR. OR RATHER, HOW DO I FIX IT?
        MapFragment mapFragment = new MapFragment();
        viewPager.setOffscreenPageLimit(2);
        PieChart pieChart = new PieChart();
        BarChart barChart = new BarChart();

        final GraphAdapter adapter = new GraphAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), pieChart, barChart);
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(graphActivity.this);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));





    }



    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());


    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
