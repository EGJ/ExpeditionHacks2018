package com.example.expeditionhacks2018;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.ldoublem.loadingviewlib.view.LVBlock;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class splashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LVBlock mLVBlock = (LVBlock) findViewById(R.id.lv_block);

        mLVBlock.setViewColor(Color.rgb(245,209,22));
        mLVBlock.setShadowColor(Color.GRAY);
        mLVBlock.startAnim(1000);

        getMapMarkersFromFirebase();
    }

    private void getMapMarkersFromFirebase(){
        //Get Map.
        //Update Map.
        //?????
        //Profit.

        /*
        DataRelay dataRelay = (DataRelay) getApplicationContext();
        //dataRelay.map;
        */
        LVBlock mLVBlock = (LVBlock) findViewById(R.id.lv_block);
        mLVBlock.stopAnim();
        Intent intent = new Intent(splashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
