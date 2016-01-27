package com.scu.tausch;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Praneet on 1/17/16.
 */
public class HomePage extends AppCompatActivity {

    private Toolbar mToolbar;
    private final int textTitleWidth = 240;
    private final int marginToReduceFromWidth = 40;
    private ListView listViewCategories;

    //Names and images for categories.
    private String[] arrayCategoryNames = new String[]{"AUTOMOBILES","BOOKS","LAPTOPS","RENTALS"};
    private int[] arrayCategoryImages = new int[]{R.drawable.ic_action_add,R.drawable.ic_action_add,R.drawable.ic_action_add,R.drawable.ic_action_add};
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
        listViewCategories = (ListView)findViewById(R.id.list_categories);

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


        final Button buttonLeftMenu = new Button(this);
        buttonLeftMenu.setBackgroundResource(R.drawable.ic_action_menu);
        buttonLeftMenu.setX(-300);
        buttonLeftMenu.setWidth(20);
        buttonLeftMenu.setHeight(20);
        mToolbar.addView(buttonLeftMenu);

        buttonLeftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this,R.style.ThemeDialogCustom);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (item == MENU) {

                            Toast.makeText(getBaseContext(),"menu clicked",Toast.LENGTH_SHORT).show();

                        } else if (item == MY_OFFERS) {
                            Toast.makeText(getBaseContext(),"my offers clicked",Toast.LENGTH_SHORT).show();


                        } else if (item == MY_MESSAGES) {
                            Toast.makeText(getBaseContext(),"my messages clicked",Toast.LENGTH_SHORT).show();


                        }else if (item == SETTINGS) {
                            Toast.makeText(getBaseContext(),"settings clicked",Toast.LENGTH_SHORT).show();


                        }else if (item == HELP) {
                            Toast.makeText(getBaseContext(),"help clicked",Toast.LENGTH_SHORT).show();


                        }else if (item == ABOUT) {
                            Toast.makeText(getBaseContext(),"about clicked",Toast.LENGTH_SHORT).show();


                        }else if (item == SIGN_OUT) {
                            Toast.makeText(getBaseContext(),"signout clicked",Toast.LENGTH_SHORT).show();


                        }

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                //Following 4 commented lines - if we want to adjust Y coordinate of dialog box.

//                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
//                wmlp.gravity = Gravity.LEFT;
//                wmlp.x = 0;   //x position
//                wmlp.y = -180;   //y position

                dialog.show();

            }
        });


        //Show all the categories on this page.
        List<HashMap<String,String>> categoriesNamesImages = new ArrayList<HashMap<String, String>>();

        for (int i=0; i<arrayCategoryNames.length;i++){
            HashMap<String,String> hmPairs = new HashMap<>();
            hmPairs.put("category_name",arrayCategoryNames[i]);
            hmPairs.put("category_image",Integer.toString(arrayCategoryImages[i]));
            categoriesNamesImages.add(hmPairs);
        }

        //Keys used in HashMap.
        String [] from = {"category_image","category_name"};

        //Ids used in HashMap.
        int [] to = {R.id.category_image,R.id.category_name};


        // Instantiating an adapter to store each items
        // R.layout.home_category_list defines the layout of each item
        SimpleAdapter categoryListAdapter = new SimpleAdapter(getBaseContext(),categoriesNamesImages,R.layout.home_category_list,from,to);
        listViewCategories.setAdapter(categoryListAdapter);

        //Setting Y value as 168, that is height of toolbar.
        listViewCategories.setY(168);


        listViewCategories.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {

                Toast.makeText(getBaseContext(), "Item clicked at"+ position, Toast.LENGTH_SHORT).show();


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
        if (id == R.id.action_search) {

            Toast.makeText(this,"Search button tapped",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_add) {

            Toast.makeText(this,"Add button tapped",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
