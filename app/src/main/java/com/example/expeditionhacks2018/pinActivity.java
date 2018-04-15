package com.example.expeditionhacks2018;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chaos.view.PinView;

public class pinActivity extends AppCompatActivity {
    private TextView wrongAttemptText;
    private FloatingActionButton enterPin;
    private PinView pinView;
    private DataRelay dataRelay;
    private int attempts = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        wrongAttemptText = findViewById(R.id.wrongAttemptText);
        enterPin = findViewById(R.id.enterPin);
        pinView = findViewById(R.id.pinView);
        dataRelay = (DataRelay) getApplicationContext();


        enterPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinView.getText().toString().equals(dataRelay.pin))
                {
                    finish();
                }
                else
                {


                    attempts++;
                    if (attempts == 3)
                    {
                        //call police api
                    }

                    wrongAttemptText.setVisibility(View.VISIBLE);
                    wrongAttemptText.setText("Wrong Attempt " + attempts);
                }
            }
        });


    }
}
