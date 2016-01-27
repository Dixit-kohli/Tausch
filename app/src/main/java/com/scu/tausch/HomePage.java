package com.scu.tausch;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Praneet on 1/17/16.
 */
public class HomePage extends AppCompatActivity {

    private Toolbar mToolbar;
    private final int textTitleWidth = 240;
    private final int marginToReduceFromWidth = 40;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Following line is added to remove title.
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        TextView textTitle = new TextView(this);

        //name of application from strings xml.
        textTitle.setText(getResources().getString(R.string.app_name));
        textTitle.setTextColor(Color.WHITE);
        textTitle.setWidth(textTitleWidth);
        textTitle.setGravity(Gravity.CENTER);
        textTitle.setTextSize(24);
        textTitle.setTypeface(null,Typeface.BOLD);

        float remainingWidth = width-textTitleWidth;
        float textTitleXPos = remainingWidth/2;

        //deducting some value from deduced x position to make it look centre aligned.
        textTitle.setX(textTitleXPos - marginToReduceFromWidth);
        mToolbar.addView(textTitle);


        Button buttonLeftMenu = new Button(this);
        buttonLeftMenu.setBackgroundResource(R.drawable.ic_action_menu);
        buttonLeftMenu.setX(-300);
        buttonLeftMenu.setWidth(20);
        buttonLeftMenu.setHeight(20);
        mToolbar.addView(buttonLeftMenu);

        buttonLeftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getApplicationContext(),"Show menu items",Toast.LENGTH_SHORT).show();



            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //Commented following lines because settings item is deleted from menu_launch_screen.xml.
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
