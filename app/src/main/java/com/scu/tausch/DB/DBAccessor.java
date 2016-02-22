package com.scu.tausch.DB;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.scu.tausch.Activities.AddOfferFragment;
import com.scu.tausch.Activities.DBListener;
import com.scu.tausch.Activities.HomePage;
import com.scu.tausch.Activities.MyOfferFragment;
import com.scu.tausch.Activities.OffersList;
import com.scu.tausch.Activities.SearchListener;
import com.scu.tausch.DTO.LoginDTO;
import com.scu.tausch.DTO.RegistrationDTO;
import com.scu.tausch.Activities.Login;
import com.scu.tausch.Misc.Constants;
import com.scu.tausch.Activities.Registration;
import com.parse.SignUpCallback;
import com.parse.LogInCallback;
import com.scu.tausch.R;

import java.lang.reflect.Array;
import java.util.List;


/**
 * Created by Praneet on 1/16/16.
 */
public class DBAccessor {

    private static DBAccessor instance = null;
    private DBListener dbListener;
    private SearchListener searchListener;

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

    private void setDBListener(DBListener dbListener){
        this.dbListener=dbListener;
    }

    private void setSearchListener(SearchListener searchListener){
        this.searchListener=searchListener;
    }

    //method to see if username is already available during registation
    public void checkIfUsernameExists(final RegistrationDTO regDTO, final Registration callbackReg){

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
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    installation.put("username", regDTO.getEmail());
                    installation.saveInBackground();

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

    public void getItemsForCategory(String categoryID, final HomePage homePage){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Offers");
        query.whereEqualTo("category_id", categoryID);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                //Getting the fragment already created using tag.
                OffersList offerListFragment = (OffersList) homePage.getSupportFragmentManager().findFragmentByTag("tagOfferList");
                setDBListener(offerListFragment);

                if (dbListener != null) {
                    dbListener.callback(objects);
                }

            }
        });

    }

    public void getCityForZip(String zip,final HomePage homePage){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("zip_code_database");
        query.whereEqualTo("zip", zip);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                //Getting the fragment already created using tag.
                AddOfferFragment addOfferFragment = (AddOfferFragment) homePage.getSupportFragmentManager().findFragmentByTag("tagAddOfferFragment");
                setDBListener(addOfferFragment);

                if (dbListener != null) {
                    dbListener.callback(objects);
                }

            }
        });


    }


    public void getSearchResults(String itemsToSearch, final HomePage homePage){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Offers");
        query.whereEqualTo("offer_title", itemsToSearch);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                setSearchListener(homePage);

                if (searchListener != null) {
                    searchListener.searchResults(objects);
                }

            }
        });

    }


}

