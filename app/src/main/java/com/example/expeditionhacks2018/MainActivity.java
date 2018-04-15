package com.example.expeditionhacks2018;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, NavigationView.OnNavigationItemSelectedListener, MapTrackYourself.OnFragmentInteractionListener {
    private FirebaseAuth mAuth;
    private  DrawerLayout dl;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);

        dl  = findViewById(R.id.drawer_layout);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, dl, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Track Yourself"));
        tabLayout.addTab(tabLayout.newTab().setText("Reporting Tool"));
        tabLayout.addTab(tabLayout.newTab().setText("TBD?"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        //we want to create this here because if we were to return a new version in ther viewpager, it would create it over and over
        //and we don't want to keep recreating the map.

        MapReportingTool MapReportingTool = new MapReportingTool();
        MapTrackYourself mapTrackYourself = new MapTrackYourself();



        viewPager.setOffscreenPageLimit(3);

        final PageAdapter adapter = new PageAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), mapTrackYourself, MapReportingTool);
        viewPager.setAdapter(adapter);

        tabLayout.setOnTabSelectedListener(this);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //GoToSplashActivity here to fill in the data from firebase about the reporting tool

//


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Intent intent;
        if (id == R.id.nav_profile) {
            //UNCOMMENT THIS WHEN WE HAVE USERQUESTIONS
//            intent = new Intent(this, UserQuestions.class);
//            intent.putExtra("fromMain", "true");

            //we don't want to use this because getFragment is a private API call and is not best practice
//            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//            for (android.app.Fragment fragment : getFragmentManager().getFragments())
//            {
//                fragmentTransaction.remove(fragment);
//            }

            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);




//            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Snackbar snackbar = Snackbar
                    .make(dl, "Signed out", Snackbar.LENGTH_LONG);
            snackbar.show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        else {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;



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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
