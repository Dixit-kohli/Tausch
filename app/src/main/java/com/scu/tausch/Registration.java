package com.scu.tausch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.scu.tausch.DB.DBAccessor;
import com.scu.tausch.DTO.RegistrationDTO;
import com.parse.FunctionCallback;
import com.parse.ParseException;
import com.scu.tausch.Misc.Constants;

import java.util.HashMap;

/**
 * Created by Praneet on 12/21/15.
 */
public class Registration extends Activity {

    private EditText editFirstName, editLastName, editEmail, editNumber, editPassword, editConfirmPassword;
    private Button buttonSubmit;
    private DBAccessor dbAccessor;
    private RegistrationDTO regDTO;
    private ProgressDialog progress;
    private CheckBox checkTermsConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editFirstName = (EditText) findViewById(R.id.first_name);
        editLastName = (EditText) findViewById(R.id.last_name);
        editEmail = (EditText) findViewById(R.id.email);
        editNumber = (EditText) findViewById(R.id.mobile_number);
        editPassword = (EditText) findViewById(R.id.password);
        editConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        checkTermsConditions = (CheckBox) findViewById(R.id.check_box);
        buttonSubmit = (Button) findViewById(R.id.submit_button);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the mainmenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch_screen, menu);
        return true;
    }

    public void onSubmitClicked(View view){

        //Put data in RegistrationDTO object
        regDTO = new RegistrationDTO();

        regDTO.setFirstName(editFirstName.getText().toString().trim());
        regDTO.setLastName(editLastName.getText().toString().trim());
        regDTO.setEmail(editEmail.getText().toString().trim());
        regDTO.setNumber(editNumber.getText().toString().trim());
        regDTO.setPassword(editPassword.getText().toString().trim());
        regDTO.setConfirmPassword(editConfirmPassword.getText().toString().trim());


        //do email verification stuff
        if (!isEmailValid(regDTO.getEmail())){
            showIncorrectEMailDialogBox();
            editEmail.setText(null);
            return;
        }

        if (checkIfFieldsEmpty()){
            showDialogForEmptyFields();
            return;
        }

        if (!isPasswordAndConfirmPasswordSame()){
            showDialogToEnterSamePassword();
        }

        if (!isTermsConditionAccepted()){
            showDialogBoxForAcceptingTermsConditions();
            return;
        }

        //getting shared instance
        dbAccessor = DBAccessor.getInstance();

        //show progressdialog while findInBackground would start working
        progress = new ProgressDialog(this);
        progress.setMessage("Verifying...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        dbAccessor.checkIfUsernameExists(regDTO,this);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void showErrorMessageToUser(String error){
        progress.dismiss();


        int index = 0;
        String toShowFromParseErrorMessage = error;

        //Remove all unwanted text from parse error.
        while (toShowFromParseErrorMessage.contains(":")){

            index = toShowFromParseErrorMessage.indexOf(":");
            toShowFromParseErrorMessage = toShowFromParseErrorMessage.substring(index + 2);
        }

        Toast.makeText(this,toShowFromParseErrorMessage,Toast.LENGTH_SHORT).show();
        //  Toast.makeText(this,"Username already exists.",Toast.LENGTH_SHORT).show();
    }

    public void showSignupSuccessfulMessage(){
        progress.dismiss();
        Toast.makeText(this,"Registration Successful",Toast.LENGTH_SHORT).show();

        //Login process with entered username and password.
        loginAfterSuccessfulRegistration();
        sendFeedbackMailToNewUser();

        finish();
        Login.loginConext.finish();
    }

    public void loginAfterSuccessfulRegistration(){

        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);

    }

    public boolean isEmailValid(String email){

        boolean isValid = true;


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false;
        }

        if (!email.contains(Constants.EMAIL_DOMAIN_SCU)){
            isValid=false;
        }

        return isValid;
    }

    private void showIncorrectEMailDialogBox(){

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please enter SCU email!");

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean checkIfFieldsEmpty(){

        boolean isFieldEmpty =false;

        if (regDTO.getFirstName().length()==0 || regDTO.getLastName().length()==0 || regDTO.getNumber().length()==0 || regDTO.getEmail().length()==0 || regDTO.getPassword().length()==0 ||regDTO.getConfirmPassword().length()==0){
            isFieldEmpty=true;
        }

        return isFieldEmpty;
    }

    private void showDialogForEmptyFields(){

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please fill all the fields.");

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean isTermsConditionAccepted(){

        boolean isTermsAccepted = true;

        if (!checkTermsConditions.isChecked()){
            isTermsAccepted = false;
        }

        return isTermsAccepted;
    }

    private void showDialogBoxForAcceptingTermsConditions(){

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please accept terms and conditions.");

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean isPasswordAndConfirmPasswordSame(){

        boolean arePasswordsSame = true;

        if(!regDTO.getPassword().equals(regDTO.getConfirmPassword())){
            arePasswordsSame = false;
        }
        return arePasswordsSame;
    }

    private void showDialogToEnterSamePassword(){

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Password and Confirm Password should be same.");

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void sendFeedbackMailToNewUser(){

        //Setting arguments for email related details of user.
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("toEmail", editEmail.getText().toString());
        params.put("toName", "Tausch User");

        //Calling cloud method written for sending email.
        ParseCloud.callFunctionInBackground("sendEmailToUser", params, new FunctionCallback<String>() {
            public void done(String str, ParseException e) {
                if (e == null) {

                }
            }
        });

    }


}