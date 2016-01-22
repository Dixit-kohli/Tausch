package com.scu.tausch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.scu.tausch.DB.DBAccessor;
import com.scu.tausch.DTO.RegistrationDTO;
import com.parse.FunctionCallback;
import com.parse.ParseException;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        final HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("templateName", "The Matrix");
        params.put("toEmail", "praneetchhabra@gmail.com");
        params.put("toName", "PJ");


        ParseCloud.callFunctionInBackground("sendTemplate", params, new FunctionCallback<Float>() {
            public void done(Float ratings, ParseException e) {
                if (e == null) {
                    // ratings is 4.5
                }
            }
        });













        editFirstName = (EditText) findViewById(R.id.first_name);
        editLastName = (EditText) findViewById(R.id.last_name);
        editEmail = (EditText) findViewById(R.id.email);
        editNumber = (EditText) findViewById(R.id.mobile_number);
        editPassword = (EditText) findViewById(R.id.password);
        editConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        buttonSubmit = (Button) findViewById(R.id.submit_button);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch_screen, menu);
        return true;
    }

    public void onSubmitClicked(View view){

        //Put data in RegistrationDTO object
        regDTO = new RegistrationDTO();

        regDTO.setFirstName(editFirstName.getText().toString());
        regDTO.setLastName(editLastName.getText().toString());
        regDTO.setEmail(editEmail.getText().toString());
        regDTO.setNumber(editNumber.getText().toString());
        regDTO.setPassword(editPassword.getText().toString());
        regDTO.setConfirmPassword(editConfirmPassword.getText().toString());

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

    public void readyToSavaData(boolean isUsernameAvailable){

        //dismiss progressdialog when callback method is called after findInBackground completes execution
        progress.dismiss();

        if(isUsernameAvailable == false){
            editEmail.setText(null);
            Toast.makeText(this,"Username already exists. Please try again.",Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isDataSaved = dbAccessor.saveRegistrationDataToParse(regDTO);
        if (isDataSaved==true){
            Toast.makeText(this,"Registration Successful",Toast.LENGTH_SHORT).show();
        }


        // close activity
        finish();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showErrorMessageToUser(){
        Toast.makeText(this,"Error occured. Please try again.",Toast.LENGTH_SHORT).show();
    }

}
