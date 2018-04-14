package com.example.expeditionhacks2018;

import android.app.ProgressDialog;//Deprecated.
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public boolean isFirstStart;

    private  DataRelay dataRelay;

    private ProgressDialog progressDialog;

    private LinearLayout li;
    private EditText mEmailField;
    private EditText mPasswordField;
    private View mLoginBtn;
    private TextView mSignupBtn;
    private TextView forgotPass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Checking for first time launch - before calling setContentView()
        // Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(LoginActivity.this, IntroActivity.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });
        // Start the thread
        t.start();


        mAuth = FirebaseAuth.getInstance();

        li = (LinearLayout) findViewById(R.id.lay123);

        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        mSignupBtn = (TextView) findViewById(R.id.signupBtn);
        forgotPass = findViewById(R.id.forgotPass);
        dataRelay = (DataRelay) getApplicationContext();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(LoginActivity.this, splashActivity.class);
                    startActivity(intent);
                    finish();
//                    //User has logged-in already
//                    //Move user directly to the Account Activity.
//                    setContentView(R.layout.activity_splash);
//                    mLVBlock = (LVBlock) findViewById(R.id.lv_block);
//
//                    mLVBlock.setViewColor(Color.rgb(245,209,22));
//                    mLVBlock.setShadowColor(Color.GRAY);
//                    mLVBlock.startAnim(1000);
//                    View splashView =  View.inflate(getApplicationContext(),R.layout.fragment_twitter_feed, );
//
//
//
//
//                    final HashMap<LatLng, Gradient> [] someArray = new HashMap[1];
//                    Thread pleaseWork = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                             location = getLastKnownLocation();
//                            //uncomment this and change the onMapReady function when showtime
//                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//                            List<Address> addresses = null;
//                            try {
//                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            String stateName = addresses.get(0).getAdminArea();
//                            HashMap<LatLng, Gradient> myMap = processMapLatLng(50, stateName);
//                            someArray[0] = myMap;
//
//
//
//                        }
//                    });
//                    pleaseWork.start();
//                    try {
//                        pleaseWork.join();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    dataRelay.theMap = someArray[0];
//                    dataRelay.someLocation = location;
//                    mLVBlock.stopAnim();
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();



//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            // This method will be executed once the timer is over
//                            // Start your app main activity
//                            mLVBlock.stopAnim();
//                            // close this activity
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
//
//                            //mProgress.setMessage("Give Us A Moment...");
//                            //mProgress.show();
//                        }
//                    }, ANIMATION_DELAY);
                }
            }
        };



        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startSignIn();

            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PasswordResetActivity.class));
            }
        });

        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
//                                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            }
        });

        progressDialog = new ProgressDialog(LoginActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void startSignIn() {

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            //User did not enter any email or password.
            Snackbar snackbar = Snackbar
                    .make(li, R.string.empty_field, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar snackbar = Snackbar
                    .make(li, R.string.incorrect_email, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else {

            progressDialog.setMessage("Logging in...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    progressDialog.dismiss();
                    if (!task.isSuccessful()) {
                        //if incorrect email/password entered.
                        Snackbar snackbar = Snackbar
                                .make(li, R.string.auth_failed, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(li, R.string.auth_success, Snackbar.LENGTH_LONG);
                        snackbar.show();

                        Intent intent = new Intent(LoginActivity.this, splashActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            });
        }

    }
}

