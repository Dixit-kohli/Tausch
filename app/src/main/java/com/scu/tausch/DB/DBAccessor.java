package com.scu.tausch.DB;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.scu.tausch.DTO.LoginDTO;
import com.scu.tausch.DTO.RegistrationDTO;
import com.scu.tausch.Login;
import com.scu.tausch.Misc.Constants;
import com.scu.tausch.Registration;
import com.parse.SignUpCallback;
import com.parse.LogInCallback;

import java.util.List;

/**
 * Created by Praneet on 1/16/16.
 */
public class DBAccessor {

    private static DBAccessor instance = null;

    protected DBAccessor() {
        // Exists only to defeat instantiation.
    }

    //singleton class. only one object should be created
    public static DBAccessor getInstance() {
        if(instance == null) {
            instance = new DBAccessor();
        }
        return instance;
    }

    //method to see if username is already available during registation
    public void checkIfUsernameExists(RegistrationDTO regDTO, final Registration callbackReg){

        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();
        ParseUser user = new ParseUser();
        user.setUsername(regDTO.getEmail());
        user.setPassword(regDTO.getPassword());
        user.setEmail(regDTO.getEmail());

        // other fields can be set just like with ParseObject
        user.put(Constants.NUMBER, regDTO.getNumber());
        user.put(Constants.FIRST_NAME,regDTO.getFirstName());
        user.put(Constants.LAST_NAME,regDTO.getLastName());

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Sign up successful.
                    callbackReg.showSignupSuccessfulMessage();
                } else {
                    // Sign up failed.
                    callbackReg.showErrorMessageToUser(""+e);
                }
            }
        });

      }


    public void checkUsernamePasswordValidity(LoginDTO loginDTO, final Login callbackLogin){

        ParseUser.logInInBackground(loginDTO.getEmail(), loginDTO.getPassword(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    callbackLogin.loginSuccessful();
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    callbackLogin.showErrorMessageToUser("" + e);
                }
            }
        });
    }


    public void resetUserPassword(String usernameForPasswordReset, final Login callbackLogin){

        ParseUser.requestPasswordResetInBackground(usernameForPasswordReset,
                new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // An email was successfully sent with reset instructions.
                            callbackLogin.showMessageToCheckEmail();
                        } else {
                            // Something went wrong.
                            callbackLogin.showErrorInResetPassword("" + e);
                        }
                    }
                });
    }


}

