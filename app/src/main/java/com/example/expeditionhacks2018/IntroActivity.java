package com.example.expeditionhacks2018;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_intro);
        enableLastSlideAlphaExitTransition(false);
        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.first_slide_background)
                        .buttonsColor(R.color.first_slide_buttons)
                        //.image(R.drawable.img_office)
                        .title("Crowed Sourced Reporting: Human Trafficking.")
                        .description("Would you pic?")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("We provide an all-in-one platform.");
                    }
                }, "Engage, Protect, Analyze"));

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.second_slide_background)
                .buttonsColor(R.color.second_slide_buttons)
                .title("Want more? Twitter + Machine Learning + Watson Sentiment Analysis.")
                .description("Go on")
                .build());

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.third_slide_background)
                        .buttonsColor(R.color.third_slide_buttons)
                        .possiblePermissions(null)
                        //.neededPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                        //.possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
                        .neededPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                        //.image(R.drawable.img_equipment)
                        .title("We need these permissions!")
                        .description("In order to get you the best analysis from our side, we need you to offer us these permissions")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("Thanks");
                    }
                }, "Give Permission!"));

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorSecondary)
                .title("Get Started?")
                .description("Sign Me Up!")
                .build());

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.first_slide_background)
                        .buttonsColor(R.color.first_slide_buttons)
                        //.image(R.drawable.img_office)
                        .title("Login")
                        .description("Would you try?")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                        //startActivity(intent1);
                        finish();
                    }
                }, "Let's get started."));
    }

    @Override
    public void onFinish() {
        super.onFinish();
        //Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
        //startActivity(intent1);

    }
}
