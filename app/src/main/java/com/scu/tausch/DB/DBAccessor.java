package com.scu.tausch.DB;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.scu.tausch.DTO.LoginDTO;
import com.scu.tausch.DTO.RegistrationDTO;
import com.scu.tausch.Login;
import com.scu.tausch.Misc.Constants;
import com.scu.tausch.Registration;

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

    public boolean saveRegistrationDataToParse(RegistrationDTO regDto){

        boolean isDataSaved = false;

        //creating parse object with all the attributes
        ParseObject regObject = new ParseObject(Constants.TABLE_REGISTRATION);
        regObject.put(Constants.FIRST_NAME,regDto.getFirstName());
        regObject.put(Constants.LAST_NAME,regDto.getLastName());
        regObject.put(Constants.NUMBER,regDto.getNumber());
        regObject.put(Constants.USERNAME, regDto.getEmail());
        regObject.put(Constants.PASSWORD,regDto.getPassword());
        regObject.put(Constants.CONFIRM_PASSWORD,regDto.getConfirmPassword());
        regObject.saveInBackground();

        isDataSaved = true;
        return isDataSaved;
    }

    //method to see if username is already available during registation
    public void checkIfUsernameExists(RegistrationDTO regDTO, final Registration callbackReg){

        final Boolean[] isUsernameAvailable = new Boolean[1];
        isUsernameAvailable[0] = true;

        //Query to search in Registration table
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.TABLE_REGISTRATION);

        //checking if username(email) already exists
        query.whereEqualTo(Constants.USERNAME, regDTO.getEmail());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {

                if (e == null) {

                    //results contain number of same emails(from edittext) already there in database.
                    if (results.size() > 0) {
                        isUsernameAvailable[0] = false;
                    }

                    //calling method in Registration when findInBackground completed execution
                    callbackReg.readyToSavaData(isUsernameAvailable[0]);

                } else {
                    // error - display error message to user
                    callbackReg.showErrorMessageToUser();
                }

            }
        });

    }



    public void checkUsernamePasswordValidity(LoginDTO loginDTO, final Login callbackLogin){

        final Boolean[] isUsernamePasswordValid = new Boolean[1];
        isUsernamePasswordValid[0] = false;

        //Query to search in Registration table
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Registration");

        //checking if username(email) and password are valid
        query.whereEqualTo(Constants.USERNAME, loginDTO.getEmail());
        query.whereEqualTo(Constants.PASSWORD, loginDTO.getPassword());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {

                if (e == null) {

                    //results contain number of matches with username and password, in database.
                    if (results.size() > 0) {
                        isUsernamePasswordValid[0] = true;
                    }

                    //calling method in Registration when findInBackground completed execution
                    callbackLogin.doLogin(isUsernamePasswordValid[0]);

                } else {
                    // error - display error message to user
                    callbackLogin.showErrorMessageToUser();
                }

            }
        });
    }



}

