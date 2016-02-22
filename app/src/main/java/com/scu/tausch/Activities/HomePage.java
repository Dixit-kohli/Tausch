package com.scu.tausch.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.Toast;
import android.content.DialogInterface;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.scu.tausch.DB.DBAccessor;
import com.scu.tausch.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by Praneet on 1/17/16.
 */
public class HomePage extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,SearchListener{


    private static String TAG = HomePage.class.getSimpleName();

    private Toolbar mToolbar,toolbarBottom;
    private FragmentDrawer drawerFragment;
    private Fragment fragment = null;


    private final int textTitleWidth = 240;
    private final int marginToReduceFromWidth = 40;

    //Names and images for categories.
    private CharSequence[] items = {"Home", "My Offers", "My Messages","Settings","Help","About","Sign out"};
    private final int HOME = 0;
    private final int MY_OFFERS = 1;
    private final int MY_MESSAGES = 2;
    private final int SETTINGS = 3;
    private final int HELP = 4;
    private final int ABOUT = 5;
    private final int SIGN_OUT = 6;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        AddOfferFragment.context=this;

        // display the first navigation drawer view on app launch
        displayView(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch_screen, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                showProgressForSearch();
                DBAccessor.getInstance().getSearchResults(query, HomePage.this);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });



        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;

//  int buttonFilter_X = 20;
//      //  int buttonSort_X = (width - (buttonSort.getWidth()+40+20));
//
//        buttonFilter.setX(buttonFilter_X);
//       // buttonSort.setX(buttonSort_X);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {


            return true;
        }
        if (id == R.id.action_add) {

            Fragment fragment = null;
            String title;

            fragment = new AddOfferFragment();
            title = getString(R.string.title_filter);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment,"tagAddOfferFragment");
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        String title = getString(R.string.app_name);
        switch (position) {
            case HOME:
                fragment = new HomeFragment();
                break;
            case MY_OFFERS:
                fragment = new MyOfferFragment();
                break;
            case MY_MESSAGES:
                fragment = new MyMessagesFragment();
                break;
            case SETTINGS:
                fragment = new SettingsFragment();
                break;
            case HELP:
                fragment = new HelpFragment();
                break;
            case ABOUT:
                fragment = new AboutFragment();
                break;
            case SIGN_OUT:
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Do you really want to Sign out?");

                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();


                        ParseUser.getCurrentUser().logOut();
                        Intent intent = new Intent(HomePage.this,Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                });

                alertDialogBuilder.setNegativeButton("Cancel",null);

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            default:
                break;
        }

        if (fragment != null) {

            //Passing context of this class to fragment.
            HomeFragment.context = this;

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void searchResults(List<ParseObject> objects) {

        progress.dismiss();

        OffersList fragment = new OffersList();
        fragment.searchList(objects);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment,"tagOfferList");
        fragmentTransaction.commit();

    }

    public void showProgressForSearch(){

        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}
