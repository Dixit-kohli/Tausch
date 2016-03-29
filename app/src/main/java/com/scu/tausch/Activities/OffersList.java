package com.scu.tausch.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.scu.tausch.Adapters.CustomListAdapter;
import com.scu.tausch.DB.DBAccessor;
import com.scu.tausch.DTO.OfferDTO;
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

        DBAccessor.searchCode = Constants.SEARCH_CODE_HOME_PAGE;

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


    public void searchList(List<ParseObject> searchedObjects, String searchStr){

        isSearchActive=true;

        itemObjects = null;
        itemObjects=searchedObjects;
        List<OfferDTO> offers = new ArrayList<OfferDTO>();

        // commented as we do not want this function any more

        offers = searchOffersWithASearchStr(searchedObjects, offers, searchStr);

        setArraysForSearchResults(offers);
        // setArraysForNamesImagesCost(searchedObjects);

    }


    public List<OfferDTO>  searchOffersWithASearchStr(List<ParseObject> offersParseList, List<OfferDTO> offersList, String searchStr){

        // till here we fetched all the offers specific to a category and which are open offers... not closed
        List<OfferDTO> searchedOffers = new ArrayList<OfferDTO>();
        //progress.dismiss();
        if(offersParseList.size()>0) {
            for (ParseObject parseOffer : offersParseList) {
                Bitmap image = null;
                try {
                    ParseFile bum = (ParseFile) parseOffer.get(Constants.DB_Image_ONE);
                    byte[] file = bum.getData();
                    image = BitmapFactory.decodeByteArray(file, 0, file.length);

                }
                catch (ParseException e){
                }
                OfferDTO offer = new OfferDTO(
                        parseOffer.getObjectId(),
                        parseOffer.getString("offeror"),
                        parseOffer.getString("category_id"),
                        parseOffer.getString("offer_title"),
                        parseOffer.getString("offer_description"),
                        parseOffer.getString("city"),
                        parseOffer.getString("zipcode"),
                        parseOffer.getString("condition"),
                        parseOffer.getString("rental_type"),
                        parseOffer.getString("price"),
                        image,
                        parseOffer.getString("offeror"),
                        parseOffer.getBoolean("status")
                );
                offersList.add(offer);
            }
        }

        // now perform the search on the offersList to search the string -
        // currently performed search on below columns: offer-title, offer_description, zipcode, price and condition
        // Can add others but they are all IDs stored like category_id,condition_id
        if(offersList!=null && !offersList.isEmpty()) {
            for (OfferDTO offer : offersList) {
                if (
                        offer.getOfferTitle().toLowerCase().contains(searchStr.toLowerCase()) ||
                                offer.getOfferDescription().toLowerCase().contains(searchStr.toLowerCase()) ||
                                offer.getPrice().toLowerCase().contains(searchStr.toLowerCase()) ||
                                //offer.getZip().toLowerCase().contains(searchStr.toLowerCase()) ||
                                offer.getCondition().toLowerCase().contains(searchStr.toLowerCase()) ||
                                offer.getCityId().toLowerCase().contains(searchStr.toLowerCase())
                        )
                    searchedOffers.add(offer);
            }
        }

        // searchedOffers will only contain those offers which has the string that is to be searched
        if(searchedOffers!=null && !searchedOffers.isEmpty()) {
            {
               // Toast.makeText(this,Toast.LENGTH_SHORT).show();
                int count = 0;
                for (OfferDTO o : searchedOffers) {
                    count++;
                 //   Toast.makeText(this, "Offer " + count + ": " + o.getOfferId()+" , "+o.getOfferTitle() + " , " + o.getOfferDescription(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        //else
       //     Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();

        //finish();
        return searchedOffers;
    }



    private void setArraysForSearchResults(List<OfferDTO> offers){

        List<String> arrayTitles = new ArrayList<>();
        List<Bitmap> arrayImages = new ArrayList<>();
        List<String> arrayPrice = new ArrayList<>();

        for(OfferDTO offer:offers){

            String itemTitle = offer.getOfferTitle();
            arrayTitles.add(itemTitle);
            String itemPrice = offer.getPrice();
            arrayPrice.add(itemPrice);
                //ParseFile bum = (ParseFile) itemObject.get(Constants.DB_Image_ONE);
                //byte[] file = bum.getData();
                Bitmap image = offer.getImage_one();
                arrayImages.add(image);
        }

        arrayItemNames = arrayTitles.toArray(new String[offers.size()]);
        arrayItemCosts = arrayPrice.toArray(new String[offers.size()]);
        arrayItemImages = arrayImages.toArray(new Bitmap[offers.size()]);

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


        /*Spinner spinnerSort = new Spinner(getActivity());
        //adding sort criteria
        List<String> sortCriteria = new ArrayList<>();
        sortCriteria.add(Constants.SORT_PRICE_LOW_TO_HIGH);
        sortCriteria.add(Constants.SORT_PRICE_HIGH_TO_LOW);
        sortCriteria.add(Constants.SORT_DATE_OLD_TO_NEW);
        sortCriteria.add(Constants.SORT_DATE_NEW_TO_OLD);
        //Creating and setting adapter to array of sort criteria required in spinner.
        ArrayAdapter<String> adapterSortCriteria = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, sortCriteria);
        adapterSortCriteria.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerSort.setAdapter(adapterSortCriteria);

        int spinnerSort_X = 20;
        spinnerSort.setX(spinnerSort_X);
        spinnerSort.setBackgroundColor(Color.TRANSPARENT);*/

        Button buttonFilter = new Button(getActivity());
        int buttonFilter_X = 20;
        buttonFilter.setX(buttonFilter_X);

        buttonFilter.setText("Filter");
        buttonFilter.setTextColor(Color.WHITE);
        buttonFilter.setBackgroundColor(Color.TRANSPARENT);
        buttonFilter.setBackgroundColor(Color.TRANSPARENT);
        toolbarBottom.addView(buttonFilter);
        //toolbarBottom.addView(spinnerSort);

        buttonFilter.setOnClickListener(new View.OnClickListener() {

            FilterFragment fragment = null;
            String title;

            @Override
            public void onClick(View v) {

                title = getString(R.string.app_name);

                fragment = new FilterFragment();
                if (itemObjects == null) {
                    progress.dismiss();
                    return;
                }
                if (itemObjects.size() == 0 || itemObjects == null) {
                    itemObjects = retainItemObjects;
                }
                fragment.fetchedItemObjects(itemObjects);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
            }
        });

        //spinnerSort.getSelectedItem().toString()

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

        /*String categoryId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            categoryId = extras.getString("CATEGORY_ID");
        }

        //Put data in OfferDTO object
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setCategoryId(categoryId);
        String selectedValue = String.valueOf(spinnerSort.getSelectedItem());
        if(selectedValue != null && (!selectedValue.equals(""))) {
            offerDTO.setSortCriteriaSelected(selectedValue);
        }
        //getting shared instance
        DBAccessor dbAccessor = DBAccessor.getInstance();
        List<ParseObject> sortResults = dbAccessor.sortOffersInCategory(offerDTO);*/

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
