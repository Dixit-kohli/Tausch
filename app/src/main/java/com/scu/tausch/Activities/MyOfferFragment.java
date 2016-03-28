package com.scu.tausch.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.scu.tausch.Adapters.CustomListAdapter;
import com.scu.tausch.DB.DBAccessor;
import com.scu.tausch.Misc.Constants;
import com.scu.tausch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Praneet on 1/29/16.
 */
public class MyOfferFragment extends Fragment implements DBListener{

    private List<ParseObject> itemObjects;
    public static HomePage context;
    private String[] arrayItemNames;
    private Bitmap[] arrayItemImages;
    private  String[] arrayItemCosts;
    private ListView listViewItems;
    private ProgressDialog progress;
    private boolean isFilterActive=false;
    private boolean isSearchActive=false;
    private TextView emptyListTextView;
    private List<ParseObject> retainItemObjects;
    private CustomListAdapter customListAdapter;
    public MyOfferFragment() {
        // Required empty public constructor
    }

    public CustomListAdapter getAdapter() {
        return customListAdapter;
    }
    public void setAdapter(CustomListAdapter customListAdapter) {
        this.customListAdapter = customListAdapter;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBAccessor.searchCode = Constants.SEARCH_CODE_HOME_PAGE;

        progress = new ProgressDialog(getActivity());
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myoffer, container, false);

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                return false;
            }
        });


        listViewItems=(ListView)rootView.findViewById(R.id.list_items_in_category);
        emptyListTextView=(TextView)rootView.findViewById(android.R.id.empty);

//        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
////WE NEED TO SHOW ENTIRE OFFER ADD PAGE, WITH ALL THE FIELDS ENTERED.
//
//                Fragment fragment = null;
//              //  String title;
//
//                fragment = new AddOfferFragment();
//              //  title = getString(R.string.title_filter);
//
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.container_body, fragment, Constants.TAG_Add_Offer_Fragment);
//                fragmentTransaction.commit();
//
//
////                DetailedItemFragment nextFrag = new DetailedItemFragment();
////
////                nextFrag.setArguments(itemObjects.get(position), arrayItemImages, position, arrayItemNames, arrayItemCosts);
////
////                OffersList.this.getFragmentManager().beginTransaction()
////                        .replace(R.id.myItemsInCategoryWindow, nextFrag)
////                        .addToBackStack(null)
////                        .commit();
//
//
//            }
//        });



        DBAccessor.getInstance().getItemsPostedByUser(context);
       // setArraysForNamesImagesCost();
       // fetchedDataFromServer();

        // Inflate the layout for this fragment
        return rootView;
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

    public void fetchedDataFromServer(){

        CustomListAdapter customListAdapter = new CustomListAdapter(getActivity(),arrayItemNames,arrayItemCosts,arrayItemImages);
        setAdapter(customListAdapter);
        listViewItems.setAdapter(customListAdapter);

        if (itemObjects.size() == 0) {
            emptyListTextView.setText("No items found.");
            listViewItems.setEmptyView(emptyListTextView);
        }

        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditOfferFragment nextFrag = new EditOfferFragment();

                nextFrag.setArgumentsForUpdate(itemObjects.get(position));

                MyOfferFragment.this.getFragmentManager().beginTransaction()
                        .replace(R.id.myOfferItemFragment, nextFrag,Constants.TAG_Edit_Offer_Fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // remove items
        // Create the listener for long item clicks
        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long rowid) {

                // Store selected item in global variable
                final String selectedItem = parent.getItemAtPosition(position).toString();
                final String objectToBeDeleted = itemObjects.get(position).getObjectId();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove " + selectedItem + "?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(getAdapter() != null) {
                            DBAccessor.getInstance().deleteOffer(objectToBeDeleted);
                            // TODO refresh the list or change arrays to lists and uncomment the next two lines
                            //getAdapter().remove(selectedItem);
                            //getAdapter().notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                // Create and show the dialog
                builder.show();
                // Signal OK to avoid further processing of the long click
                return true;
            }
        });
    }

    @Override
    public void callback(List<ParseObject> objects) {

        progress.dismiss();
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
