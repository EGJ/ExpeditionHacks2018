package com.example.expeditionhacks2018;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

/**
 * Created by Eric on 12/2/2017.
 */

public class UserQuestions extends Activity implements VerticalStepperForm {
    private LinearLayout parentLayout;
    private VerticalStepperFormLayout verticalStepperForm;

    private Spinner state;
    private Spinner city;
    private EditText age;
    private RadioGroup sex;
    private EditText height;
    private EditText weight;
    private EditText income;
    private RadioGroup employment;
    private RadioGroup maritialStatus;
    private EditText emergencyContact;
    private EditText pin;
    private EditText additionalInfo;

    private String stateInput = "";
    private String sexInput = "";
    private String ageInput;
    private String employmentInput = "";
    private String maritalstatusInput = "";
    private String weightInput;
    private String heightInput;
    private String incomeInput;
    private String emergencyContactInput;
    private String pinInput;
    private String additionalInfoInput;



    boolean fromMain = false;
    private Button doneButton;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions);
        Intent i = this.getIntent();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser();
        mDatabase = database.getReference();
        //if we're coming from the main activity, it means we've already signed in before and so we can
        //get the snapshots from the database and pre-fill the data to be updated or changed.
        if ((i.getExtras()!=null) && (i.getExtras().get("fromMain").equals("true"))) {
            fromMain = true;





//            String[] mySteps = {"State", "City", "Age", "Sex", "Height (in)", "Weight (lbs)", "Annual Income", "Employment", "Maritial Status", "Smoker", "Optional Insured", "People Covered", "Preconditions"};
//            int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
//            int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);

//            LayoutInflater inflater = LayoutInflater.from(getBaseContext());
//            parentLayout = (LinearLayout) inflater.inflate(R.layout.views, null, false);
//            // Finding the view

            //DO FIREBASE HERE AND API CALL HERE.
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = database.getReference();

            String user = mAuth.getCurrentUser().getUid();
            final DatabaseReference dbResponse = mDatabase.child("Users").child(user);
            dbResponse.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Prefill all the user's information if they are already logged in
                    ageInput = (String) dataSnapshot.child("age").getValue();
                    stateInput = (String) dataSnapshot.child("state").getValue();
                    sexInput = (String) dataSnapshot.child("sex").getValue();
                    employmentInput = (String) dataSnapshot.child("employment").getValue();
                    maritalstatusInput = (String) dataSnapshot.child("maritalstatus").getValue();
                    weightInput = (String) dataSnapshot.child("weight").getValue();
                    heightInput = (String) dataSnapshot.child("height").getValue();
                    incomeInput = (String) dataSnapshot.child("income").getValue();
                    emergencyContactInput = (String) dataSnapshot.child("emergencyContact").getValue();
                    pinInput = (String) dataSnapshot.child("pin").getValue();
                    additionalInfoInput = (String) dataSnapshot.child("additionalInfo").getValue();

                    verticalStepper();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });



        }
        else
        {
            verticalStepper();
        }


    }

    public void verticalStepper()
    {
        String[] mySteps = {"State", "Age", "Sex", "Height (in)", "Weight (lbs)", "Annual Income", "Employment", "Maritial Status", "Emergency Contact", "5 Digit Pin", "Additional Information"};
        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);

        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        parentLayout = (LinearLayout) inflater.inflate(R.layout.views, null, false);

        verticalStepperForm = findViewById(R.id.vertical_stepper_form);

        // Setting up and initializing the form
        VerticalStepperFormLayout.Builder.newInstance(verticalStepperForm, mySteps, this, this)
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .displayBottomNavigation(false) // Defaults to true
                .init();
    }


    @Override
    public View createStepContentView(int stepNumber) {
        View view = null;
        switch (stepNumber) {
            case 0:
                view = createStateStep();
                break;
            case 1:
                view = createAgeStep();
                break;
            case 2:
                view = createSexStep();
                break;
            case 3:
                view = createHeightStep();
                break;
            case 4:
                view = createWeightStep();
                break;
            case 5:
                view = createIncomeStep();
                break;
            case 6:
                view = createEmploymentStep();
                break;
            case 7:
                view = createMaritialStatusStep();
                break;
            case 8:
                view = createEmergencyContactStep();
                break;
            case 9:
                view = createPinStep();
                break;
            case 10:
                view = createAdditionalInfoStep();
                break;
            default:
                break;
        }
        return view;
    }

    @Override
    public void onStepOpening(int stepNumber) {
        switch (stepNumber) {
            case 0:
                //First selection is selected by default
                verticalStepperForm.setActiveStepAsCompleted();
                break;
            case 1:
                checkAge();
                break;
            case 2:
                checkSex();
                break;
            case 3:
                checkHeight();
                break;
            case 4:
                checkWeight();
                break;
            case 5:
                checkIncome();
                break;
            case 6:
                checkEmployment();
                break;
            case 7:
                checkMaritialStatus();
                break;
            case 8:
                checkEmergencyContact();
                break;
            case 9:
                checkPin();
            case 10:
                //Additional Information Optional
                verticalStepperForm.setActiveStepAsCompleted();
                break;
            default:
                break;
        }
    }

    @Override
    public void sendData() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = database.getReference();
        String user = mAuth.getCurrentUser().getUid();

        mDatabase.child("Users").child(user).child("age").setValue(age.getText().toString());
        mDatabase.child("Users").child(user).child("state").setValue(state.getSelectedItem().toString());
        RadioButton MaleradioButton = findViewById(R.id.maleRB);
        if (MaleradioButton.isChecked())
        {
            mDatabase.child("Users").child(user).child("sex").setValue("M");

        }
        else {
            mDatabase.child("Users").child(user).child("sex").setValue("F");

        }
        mDatabase.child("Users").child(user).child("height").setValue(height.getText().toString());
        mDatabase.child("Users").child(user).child("weight").setValue(weight.getText().toString());
        mDatabase.child("Users").child(user).child("income").setValue(income.getText().toString());
        RadioButton employedRB = findViewById(R.id.employedRB);
        if (employedRB.isChecked())
        {
            mDatabase.child("Users").child(user).child("employment").setValue("employed");

        }
        else
        {
            mDatabase.child("Users").child(user).child("employment").setValue("unemployed");
        }
        RadioButton maritalRB = findViewById(R.id.marriedRB);
        if (maritalRB.isChecked())
        {
            mDatabase.child("Users").child(user).child("maritalstatus").setValue("married");
        }
        else {
            mDatabase.child("Users").child(user).child("maritalstatus").setValue("single");
        }
        mDatabase.child("Users").child(user).child("emergencyContact").setValue(emergencyContact.getText().toString());
        mDatabase.child("Users").child(user).child("pin").setValue(pin.getText().toString());
        mDatabase.child("Users").child(user).child("additionalInfo").setValue(additionalInfo.getText().toString());
    }

    private View createStateStep() {
        if (fromMain)
        {
            state = parentLayout.findViewById(R.id.personState);
            state.setPrompt(stateInput);
            ((ViewGroup) state.getParent()).removeView(state);
            return state;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout spinnerLayout = (LinearLayout) inflater.inflate(R.layout.views, null, false);
        state = spinnerLayout.findViewById(R.id.personState);
        ((ViewGroup) state.getParent()).removeView(state);
        return state;
    }

    private View createAgeStep() {
        if(fromMain) {
            age = parentLayout.findViewById(R.id.personAge);
            age.setText(ageInput.toString());
            ((ViewGroup) age.getParent()).removeView(age);
            age.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    checkAge();
                }
                @Override
                public void afterTextChanged(Editable editable) {}
            });
            return age;
        }
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout spinnerLayout = (LinearLayout) inflater.inflate(R.layout.views, null, false);
        age = spinnerLayout.findViewById(R.id.personAge);
        ((ViewGroup) age.getParent()).removeView(age);
        age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkAge();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return age;
    }

    private View createSexStep() {
        if (fromMain) {
            sex = parentLayout.findViewById(R.id.genderGroup);

            if (sexInput.equals("M"))
            {
                RadioButton radioButton = parentLayout.findViewById(R.id.maleRB);
                radioButton.setChecked(true);
            }
            else
            {
                RadioButton radioButton = parentLayout.findViewById(R.id.femaleRB);
                radioButton.setChecked(true);
            }
            ((ViewGroup) sex.getParent()).removeView(sex);
            sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    checkSex();
                    sex.setOnCheckedChangeListener(null);
                }
            });
            return sex;
        }
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout spinnerLayout = (LinearLayout) inflater.inflate(R.layout.views, null, false);
        sex = spinnerLayout.findViewById(R.id.genderGroup);
        ((ViewGroup) sex.getParent()).removeView(sex);
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkSex();
                sex.setOnCheckedChangeListener(null);
            }
        });
        return sex;
    }

    private View createHeightStep() {
        height = parentLayout.findViewById(R.id.personHeight);
        if (fromMain)
        {

            height.setText(heightInput.toString());
        }
        ((ViewGroup) height.getParent()).removeView(height);
        height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkHeight();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return height;
    }

    private View createWeightStep() {
        weight = parentLayout.findViewById(R.id.personWeight);
        if (fromMain)
        {

            weight.setText(weightInput.toString());
        }
        ((ViewGroup) weight.getParent()).removeView(weight);
        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkWeight();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return weight;
    }

    private View createIncomeStep() {
        income = parentLayout.findViewById(R.id.annualIncome);
        if (fromMain)
        {

            income.setText(incomeInput.toString());
        }
        ((ViewGroup) income.getParent()).removeView(income);
        income.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String incomeString = income.getText().toString();
                if (!incomeString.startsWith("$")) {
                    income.setText("$" + incomeString.replace("$", ""));
                }
                checkIncome();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return income;
    }

    private View createEmploymentStep() {
        employment = parentLayout.findViewById(R.id.employementGroup);
        if (fromMain) {

            if (employmentInput.equals("employed"))
            {
                RadioButton radioButton = parentLayout.findViewById(R.id.employedRB);
                radioButton.setChecked(true);
            }
            else {
                RadioButton radioButton = parentLayout.findViewById(R.id.unemployedRB);
                radioButton.setChecked(true);
            }
        }

        ((ViewGroup) employment.getParent()).removeView(employment);
        employment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkEmployment();
                employment.setOnCheckedChangeListener(null);
            }
        });
        return employment;
    }

    private View createMaritialStatusStep() {
        maritialStatus = parentLayout.findViewById(R.id.maritialGroup);
        if (fromMain) {

            if (maritalstatusInput.equals("single"))
            {
                RadioButton radioButton = parentLayout.findViewById(R.id.singledRB);
                radioButton.setChecked(true);
            }
            else {
                RadioButton radioButton = parentLayout.findViewById(R.id.marriedRB);
                radioButton.setChecked(true);
            }
        }
        ((ViewGroup) maritialStatus.getParent()).removeView(maritialStatus);
        maritialStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkMaritialStatus();
                maritialStatus.setOnCheckedChangeListener(null);
            }
        });
        return maritialStatus;
    }

    private View createEmergencyContactStep(){
        emergencyContact = parentLayout.findViewById(R.id.emergencyContact);
        if (fromMain)
        {

            emergencyContact.setText(emergencyContactInput.toString());
        }
        ((ViewGroup) emergencyContact.getParent()).removeView(emergencyContact);
        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkEmergencyContact();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return emergencyContact;
    }

    private View createPinStep(){
        pin = parentLayout.findViewById(R.id.pinNumber);
        if (fromMain)
        {

            pin.setText(pinInput.toString());
        }
        ((ViewGroup) pin.getParent()).removeView(pin);
        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkPin();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return pin;
    }

    private View createAdditionalInfoStep(){
        additionalInfo = parentLayout.findViewById(R.id.additionalComments);
        if (fromMain)
        {

            additionalInfo.setText(additionalInfoInput.toString());
        }
        ((ViewGroup) additionalInfo.getParent()).removeView(additionalInfo);
        return additionalInfo;
    }

    private void checkAge() {
        if (!age.getText().toString().equals("")) {
            verticalStepperForm.setStepAsCompleted(1);
        } else {
            verticalStepperForm.setStepAsUncompleted(1, "Must Enter An Age");
        }
    }

    private void checkSex() {
        if (sex.getCheckedRadioButtonId() != -1) {
            verticalStepperForm.setStepAsCompleted(2);
        } else {
            verticalStepperForm.setStepAsUncompleted(2, "Must Select A Value");
        }
    }

    private void checkHeight() {
        if (!height.getText().toString().equals("")) {
            verticalStepperForm.setStepAsCompleted(3);
        } else {
            verticalStepperForm.setStepAsUncompleted(3, "Must Enter A Height");
        }
    }

    private void checkWeight() {
        if (!weight.getText().toString().equals("")) {
            verticalStepperForm.setStepAsCompleted(4);
        } else {
            verticalStepperForm.setStepAsUncompleted(4, "Must Enter An Weight");
        }
    }

    private void checkIncome() {
        if (!income.getText().toString().equals("$")) {
            verticalStepperForm.setStepAsCompleted(5);
        } else {
            verticalStepperForm.setStepAsUncompleted(5, "Must Enter An Income");
        }
    }

    private void checkEmployment() {
        if (employment.getCheckedRadioButtonId() != -1) {
            verticalStepperForm.setStepAsCompleted(6);
        } else {
            verticalStepperForm.setStepAsUncompleted(6, "Must Select An Employment Status");
        }
    }

    private void checkMaritialStatus() {
        if (maritialStatus.getCheckedRadioButtonId() != -1) {
            verticalStepperForm.setStepAsCompleted(7);
        } else {
            verticalStepperForm.setStepAsUncompleted(7, "Must Select A Marital Status");
        }
    }

    private void checkEmergencyContact() {
        //if (!age.getText().toString().equals("")) {
            verticalStepperForm.setStepAsCompleted(8);
        //} else {
        //    verticalStepperForm.setStepAsUncompleted(8, "Must Enter An Age");
        //}
    }

    private void checkPin() {
        if (pin.getText().toString().equals("")) {
            verticalStepperForm.setStepAsUncompleted(9, "Please Enter A Pin");
        } else if(pin.getText().length() != 5){
            verticalStepperForm.setStepAsUncompleted(9, "Pin Must Be 5 Digits");
        }else{
            verticalStepperForm.setStepAsCompleted(9);
        }
    }
}
