package com.example.who.calendanot.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.who.calendanot.R;
import com.example.who.calendanot.retrifitservice.SpreadsheetWebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by who on 25.11.2016.
 */

public class SheetForm extends AppCompatActivity {

private EditText mEditTextCompany;
private EditText mEditTextName_Surname;
private EditText mEditTextPosition;
private EditText mEditTextTelephone;
private EditText mEditTextEmail;
private EditText mEditTextTheNameOfTheCafe;
private EditText mEditTextCoordinates;
private EditText mEditTextTheme;
private EditText mEditTextDetails;
private Button mButtonSubmit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_activity);
        mEditTextCompany = (EditText) findViewById(R.id.company);
        mEditTextName_Surname = (EditText) findViewById(R.id.name_Surname);
        mEditTextPosition = (EditText) findViewById(R.id.position);
        mEditTextTelephone = (EditText) findViewById(R.id.telephone);
        mEditTextEmail = (EditText) findViewById(R.id.email);
        mEditTextTheNameOfTheCafe = (EditText) findViewById(R.id.the_name_of_the_cafe_restaurant);
        mEditTextCoordinates = (EditText) findViewById(R.id.coordinates);
        mEditTextTheme = (EditText) findViewById(R.id.theme);
        mEditTextDetails = (EditText) findViewById(R.id.details);
        mButtonSubmit = (Button) findViewById(R.id.my_submit_button);


        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make sure all the fields are filled with values
                if(TextUtils.isEmpty(mEditTextCompany.getText().toString()) ||
                        TextUtils.isEmpty(mEditTextName_Surname.getText().toString()) ||
                        TextUtils.isEmpty(mEditTextPosition.getText().toString()) ||
                        TextUtils.isEmpty(mEditTextTelephone.getText().toString()) ||
                        TextUtils.isEmpty(mEditTextEmail.getText().toString()) ||
                        TextUtils.isEmpty(mEditTextTheNameOfTheCafe.getText().toString()) ||
                        TextUtils.isEmpty(mEditTextCoordinates.getText().toString()) ||
                        TextUtils.isEmpty(mEditTextTheme.getText().toString()) ||
                        TextUtils.isEmpty(mEditTextDetails.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Field can't be empty.",Toast.LENGTH_LONG).show();
                    return;
                }
                //Check if a valid email is entered
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mEditTextEmail.getText().toString()).matches())
                {
                    Toast.makeText(getApplicationContext(),"Please enter a valid email.",Toast.LENGTH_LONG).show();
                    return;
                }
                //Check if a valid phone number is entered
                if(!Patterns.PHONE.matcher(mEditTextTelephone.getText().toString()).matches())
                {
                    Toast.makeText(getApplicationContext(),"Please enter a valid email.",Toast.LENGTH_LONG).show();
                    return;
                }


                sendDataToGoogleSheets();

            }
        });
    }

    private void sendDataToGoogleSheets() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://docs.google.com/forms/d/")
                .build();
                String companyInput =     mEditTextCompany.getText().toString();
                String nameSurnameInput = mEditTextName_Surname.getText().toString();
                String positionInput =    mEditTextPosition.getText().toString();
                String telephoneInput =   mEditTextTelephone.getText().toString();
                String emailInput =       mEditTextEmail.getText().toString();
                String theNameOfTheCafeInput = mEditTextTheNameOfTheCafe.getText().toString();
                String coordinatesInput = mEditTextCoordinates.getText().toString();
                String themeInput =       mEditTextTheme.getText().toString();
                String detailsInput =     mEditTextDetails.getText().toString();


        //Creating object for our interface
        final SpreadsheetWebService spreadsheetWebService = retrofit.create(SpreadsheetWebService.class);
        Call<Void> completeQuestionnaireCall=spreadsheetWebService.
                completeQuestionnaire(companyInput, nameSurnameInput, positionInput,
                        telephoneInput, emailInput,theNameOfTheCafeInput, coordinatesInput,
                        themeInput, detailsInput);
                        completeQuestionnaireCall.enqueue(callCallback);}

        private final Callback<Void> callCallback = new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
//                BufferedReader reader = null;
//                String output = "";
//                InputStream is = response.raw().body().byteStream();
//                try {
//                    //Initializing buffered reader
//                    reader = new BufferedReader(new InputStreamReader(is));
//
//                    //Reading the output in the string
//                    output = reader.readLine();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //Displaying the output as a toast
//                Toast.makeText(SheetForm.this, output, Toast.LENGTH_LONG).show();
                Toast.makeText(SheetForm.this, "Thank you. Form send.", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }

        };


}


