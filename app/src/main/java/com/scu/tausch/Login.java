package com.scu.tausch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scu.tausch.DB.DBAccessor;
import com.scu.tausch.DTO.LoginDTO;

/**
 * Created by Praneet on 12/21/15.
 */
public class Login extends Activity {

    private EditText editUsername, editPassword;
    private Button buttonLogin;
    private DBAccessor dbAccessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = (EditText)findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        buttonLogin = (Button) findViewById(R.id.button_login);

        //login details until testing.delete later
        editUsername.setText("pjain3@scu.edu");
        editPassword.setText("12345");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch_screen, menu);
        return true;
    }

    public void onLoginClicked(View view){

        LoginDTO loginDTO = new LoginDTO();

        //for login, setting only two required attributes
        loginDTO.setEmail(editUsername.getText().toString());
        loginDTO.setPassword(editPassword.getText().toString());

        dbAccessor = DBAccessor.getInstance();
        dbAccessor.checkUsernamePasswordValidity(loginDTO,this);

    }


    public void onSignupClicked(View view){

        Intent i = new Intent(Login.this,Registration.class);
        startActivity(i);
    }

    public void onForgotPasswordClicked(View view){

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

    public void doLogin(boolean isUsernamePasswordValid){

        if (isUsernamePasswordValid){

            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);

        }
        else{

            editUsername.setText(null);
            editPassword.setText(null);
            Toast.makeText(this,"Invalid username or password.",Toast.LENGTH_SHORT).show();

        }

    }

    public void showErrorMessageToUser(){
        Toast.makeText(this, "Error occured. Please try again.", Toast.LENGTH_SHORT).show();
    }
}
