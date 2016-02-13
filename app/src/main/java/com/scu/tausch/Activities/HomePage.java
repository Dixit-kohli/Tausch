package com.scu.tausch.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.widget.AdapterView.OnItemClickListener;

import com.scu.tausch.Misc.Constants;
import com.scu.tausch.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Praneet on 1/17/16.
 */
public class HomePage extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{


    private static String TAG = HomePage.class.getSimpleName();

    private Toolbar mToolbar,toolbarBottom;
    private FragmentDrawer drawerFragment;
    private ImageButton buttonFilter;
    private ImageButton buttonSort;

    private final int textTitleWidth = 240;
    private final int marginToReduceFromWidth = 40;
//    private ListView listViewCategories;

    //Names and images for categories.
//    private String[] arrayCategoryNames = new String[]{"AUTOMOBILES","BOOKS","LAPTOPS","RENTALS"};
//    private int[] arrayCategoryImages = new int[]{R.drawable.ic_action_add,R.drawable.ic_action_add,R.drawable.ic_action_add,R.drawable.ic_action_add};
    private CharSequence[] items = {"Menu", "My Offers", "My Messages","Settings","Help","About","Sign out"};
    private final int MENU = 0;
    private final int MY_OFFERS = 1;
    private final int MY_MESSAGES = 2;
    private final int SETTINGS = 3;
    private final int HELP = 4;
    private final int ABOUT = 5;
    private final int SIGN_OUT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarBottom = (Toolbar) findViewById(R.id.toolbarBottom);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


        buttonFilter = new ImageButton(this);
        buttonFilter.setImageResource(R.mipmap.ic_filter);
        buttonFilter.setBackgroundColor(Color.TRANSPARENT);
        buttonFilter.setColorFilter(Color.argb(255, 255, 255, 255));
        buttonFilter.setBackgroundColor(Color.TRANSPARENT);
        toolbarBottom.addView(buttonFilter);

        buttonSort = new ImageButton(this);
        buttonSort.setImageResource(R.mipmap.ic_sort);
        buttonSort.setBackgroundColor(Color.TRANSPARENT);
        buttonSort.setColorFilter(Color.argb(255, 255, 255, 255));
        buttonSort.setBackgroundColor(Color.TRANSPARENT);
        toolbarBottom.addView(buttonSort);


        buttonFilter.setOnClickListener(new View.OnClickListener() {

            Fragment fragment = null;
            String title;

            @Override
            public void onClick(View v) {

                title = getString(R.string.app_name);

                fragment = new SortFragment();
                title = getString(R.string.title_sort);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();

                // set the toolbar title
                getSupportActionBar().setTitle(title);

            }
        });

        buttonSort.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


            }
        });

        // display the first navigation drawer view on app launch
        displayView(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch_screen, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

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

        int buttonFilter_X = 20;
        int buttonSort_X = (width - (buttonSort.getWidth()+40+20));

        buttonFilter.setX(buttonFilter_X);
        buttonSort.setX(buttonSort_X);

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

            Toast.makeText(this,"Search button tapped",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_add) {

            Fragment fragment = null;
            String title;

            fragment = new FilterFragment();
            title = getString(R.string.title_filter);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment,"tagFilterFragment");
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
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case MENU:
                fragment = new MenuFragment();
                title = getString(R.string.title_menu);
                break;
            case MY_OFFERS:
                fragment = new MyOfferFragment();
                title = getString(R.string.title_myoffer);
                break;
            case MY_MESSAGES:
                fragment = new MyMessagesFragment();
                title = getString(R.string.title_mymessages);
                break;
            case SETTINGS:
                fragment = new SettingsFragment();
                title = getString(R.string.title_settings);
                break;
            case HELP:
                fragment = new HelpFragment();
                title = getString(R.string.title_help);
                break;
            case ABOUT:
                fragment = new AboutFragment();
                title = getString(R.string.title_about);
                break;
            case SIGN_OUT:
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Do you really want to Sign out?");

                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        //Setting value - user is login
//                        SharedPreferences sharedPreferences = getSharedPreferences(Constants.USER_PREFS_NAME, Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("isLogin", "false");
//                        editor.commit();

//                        Intent intent = new Intent(HomePage.this,Login.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();

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
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }


}
