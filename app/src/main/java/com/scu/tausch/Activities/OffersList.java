package com.scu.tausch.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.scu.tausch.Adapters.CustomListAdapter;
import com.scu.tausch.Misc.Constants;
import com.scu.tausch.R;

import com.parse.ParseException;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Praneet on 1/31/16.
 */
public class OffersList extends Fragment implements DBListener{


    private List<ParseObject> itemObjects;
    private String[] arrayItemNames;
    private Bitmap[] arrayItemImages;
    private  String[] arrayItemCosts;
    private ListView listViewItems;
    private ProgressDialog progress;
    private boolean isFilterActive=false;
    private boolean isSearchActive=false;
    private TextView emptyListTextView;
    static List<ParseObject> retainItemObjects;

    public OffersList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress = new ProgressDialog(getActivity());
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    public void setRetainItemObjects(List<ParseObject> retainItemObjects){
        this.retainItemObjects=retainItemObjects;
    }

    private void setArraysForNamesImagesCost(List<ParseObject> arrayOfItemObjects){

        List<String> arrayTitles = new ArrayList<>();
        List<Bitmap> arrayImages = new ArrayList<>();
        List<String> arrayPrice = new ArrayList<>();

        for(ParseObject itemObject:arrayOfItemObjects){

            String itemTitle = (String)itemObject.get(Constants.DB_Offer_Title);
            arrayTitles.add(itemTitle);
            String itemPrice = (String)itemObject.get(Constants.DB_Price);
            arrayPrice.add(itemPrice);

            try {
                ParseFile bum = (ParseFile) itemObject.get(Constants.DB_Image_ONE);
                byte[] file = bum.getData();
                Bitmap image = BitmapFactory.decodeByteArray(file, 0, file.length);
                arrayImages.add(image);

            }
            catch (ParseException e){

            }

        }

        arrayItemNames = arrayTitles.toArray(new String[itemObjects.size()]);
        arrayItemCosts = arrayPrice.toArray(new String[itemObjects.size()]);
        arrayItemImages = arrayImages.toArray(new Bitmap[itemObjects.size()]);

    }


    public void searchList(List<ParseObject> searchedObjects){

        isSearchActive=true;

        itemObjects = null;
        itemObjects=searchedObjects;



        setArraysForNamesImagesCost(searchedObjects);

    }

 public void filterList(List<ParseObject> filteredObjects){

     isFilterActive=true;

     itemObjects = null;
     itemObjects=filteredObjects;


     setArraysForNamesImagesCost(filteredObjects);

 }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_offers_list, container, false);


        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                return false;
            }
        });


        listViewItems=(ListView)rootView.findViewById(R.id.list_items_in_category);
        Toolbar toolbarBottom = (Toolbar)rootView.findViewById(R.id.toolbarBottom);
        emptyListTextView=(TextView)rootView.findViewById(android.R.id.empty);

        Button buttonFilter = new Button(getActivity());
        int buttonFilter_X = 20;
        buttonFilter.setX(buttonFilter_X);

        buttonFilter.setText("Filter");
        buttonFilter.setTextColor(Color.WHITE);
        buttonFilter.setBackgroundColor(Color.TRANSPARENT);
        buttonFilter.setBackgroundColor(Color.TRANSPARENT);
        toolbarBottom.addView(buttonFilter);

        buttonFilter.setOnClickListener(new View.OnClickListener() {

            FilterFragment fragment = null;
            String title;

            @Override
            public void onClick(View v) {

                title = getString(R.string.app_name);

                fragment = new FilterFragment();
                if (itemObjects==null){
                    progress.dismiss();
                    return;
                }
                if (itemObjects.size()==0 || itemObjects==null){
                    itemObjects=retainItemObjects;
                }
                fragment.fetchedItemObjects(itemObjects);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();


            }
        });

        if (isFilterActive) {
            fetchedDataFromServer();
            isFilterActive=false;
            progress.dismiss();
        }

        if (isSearchActive){
            fetchedDataFromServer();
            isSearchActive=false;
            progress.dismiss();
        }


        // Inflate the layout for this fragment
        return rootView;
    }

    //Method is called when data has been fetched from server.
    public void fetchedDataFromServer(){

        CustomListAdapter customListAdapter = new CustomListAdapter(getActivity(),arrayItemNames,arrayItemCosts,arrayItemImages);
        listViewItems.setAdapter(customListAdapter);

        if (itemObjects.size() == 0) {
            emptyListTextView.setText("No items found.");
            listViewItems.setEmptyView(emptyListTextView);
        }

        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                DetailedItemFragment nextFrag = new DetailedItemFragment();

                nextFrag.setArguments(itemObjects.get(position), arrayItemImages, position, arrayItemNames, arrayItemCosts);

                OffersList.this.getFragmentManager().beginTransaction()
                        .replace(R.id.myItemsInCategoryWindow, nextFrag)
                        .addToBackStack(null)
                        .commit();


            }
        });


    }

    @Override
    public void callback(List<ParseObject> objects) {

        progress.dismiss();
        setRetainItemObjects(objects);
        itemObjects=objects;

        setArraysForNamesImagesCost(objects);

        fetchedDataFromServer();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
