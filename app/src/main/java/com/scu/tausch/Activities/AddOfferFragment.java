package com.scu.tausch.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.scu.tausch.DB.DBAccessor;
import com.scu.tausch.DTO.OfferDTO;
import com.scu.tausch.Misc.Constants;
import com.scu.tausch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Praneet on 2/11/16.
 */
public class AddOfferFragment extends Fragment implements DBListener{

    public static HomePage context;
    private OfferDTO offerDTO;
    private ProgressDialog progress;
    private TextView textCityName;

    public AddOfferFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_offer, container, false);

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                return false;
            }
        });

        final Spinner spinnerCategory = (Spinner)rootView.findViewById(R.id.spinner_category);
        final Spinner spinnerCondition = (Spinner)rootView.findViewById(R.id.spinner_condition);
        final EditText editTitle = (EditText)rootView.findViewById(R.id.edit_titleListing);
        final EditText editDescription = (EditText)rootView.findViewById(R.id.edit_description);
        final EditText editPrice = (EditText)rootView.findViewById(R.id.edit_price);
        final EditText editZip = (EditText)rootView.findViewById(R.id.edit_zip);
        textCityName=(TextView)rootView.findViewById(R.id.text_city);

        List<String> categories = new ArrayList<>();
        categories.add("Automobiles");
        categories.add("Books");
        categories.add("Laptops");
        categories.add("Furniture");
        categories.add("Rentals");

        List<String> conditions = new ArrayList<>();
        conditions.add("New");
        conditions.add("Used");




        ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCategory.setAdapter(adapterCategories);

        ArrayAdapter<String> adapterConditions = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, conditions);
        adapterConditions.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCondition.setAdapter(adapterConditions);









        Button buttonNext = (Button)rootView.findViewById(R.id.button_next);

        buttonNext.setOnClickListener(new View.OnClickListener() {

          //  Fragment fragment = null;
            String title;

            @Override
            public void onClick(View v) {

                    title = getString(R.string.app_name);

                offerDTO = new OfferDTO();

                offerDTO.setOfferTitle(editTitle.getText().toString().trim());
                offerDTO.setOfferDescription(editDescription.getText().toString().trim());
                offerDTO.setPrice(editPrice.getText().toString().trim());
                offerDTO.setZip(editZip.getText().toString().trim());
                offerDTO.setCategoryId(getCategoryId(spinnerCategory.getSelectedItem().toString()));
                offerDTO.setCondition(spinnerCondition.getSelectedItem().toString());
                offerDTO.setOfferorName((String) ParseUser.getCurrentUser().get("firstname"));

                DBAccessor.getInstance().getCityForZip(editZip.getText().toString().trim(), context);

                progress = new ProgressDialog(getActivity());
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.show();



            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public boolean isFormComplete(String title, String description,String price, String zip, String city){

        boolean isComplete = true;

        if (title.length()==0){
            isComplete=false;
            showDialogBox();
        }
        else if (description.length()==0){
            isComplete=false;
            showDialogBox();
        }
        else if (price.length()==0){
            isComplete=false;
            showDialogBox();
        }
        else if(zip.length()==0){
            isComplete=false;
            showDialogBox();
        }


        return isComplete;
    }

    public void showDialogBox(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Incomplete!");
        alertDialog.setMessage("Please fill all the fields.");

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();

    }

    public void showDialogBoxForIncorrectZip(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Incorrect Zip Code!");
        alertDialog.setMessage("Please provide valid zip code.");

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();

    }
    public String getCategoryId(String categoryName){

        if (categoryName.equals("Automobiles")){
            return Constants.CATEGORY_AUTOMOBILES_OBJECT_ID;
        }
        if (categoryName.equals("Books")){
            return Constants.CATEGORY_BOOKS_OBJECT_ID;
        }
        if (categoryName.equals("Laptops")){
            return Constants.CATEGORY_LAPTOPS_OBJECT_ID;
        }
        if (categoryName.equals("Furniture")){
            return Constants.CATEGORY_FURNITURE_OBJECT_ID;
        }
        if (categoryName.equals("Rentals")){
            return Constants.CATEGORY_RENTALS_OBJECT_ID;
        }
        return null;
    }

    @Override
    public void callback(List<ParseObject> objects) {

        progress.dismiss();
        if (objects==null || objects.size()==0){
            showDialogBoxForIncorrectZip();
            return;
        }
        offerDTO.setCityId((String) (objects.get(0).get("primary_city")));
        textCityName.setText(offerDTO.getCityId());

        boolean isCompleted = isFormComplete(offerDTO.getOfferTitle(),offerDTO.getOfferDescription(),offerDTO.getPrice(),offerDTO.getZip(),offerDTO.getCityId());

        if (isCompleted==false){
            return;
        }


        ImageAddFragment nextFrag= new ImageAddFragment();
        nextFrag.currentOfferDetails(offerDTO);

        //Below, addToBackStack(null) means that if we transition to next fragment, this
        //fragment will not be destroyed and would appear again if user taps back button.
        //addToBackStack(null) works for current fragment, like
        //AddOfferFragment in this case.

        AddOfferFragment.this.getFragmentManager().beginTransaction()
                .replace(R.id.container_body, nextFrag,"tagImageAdd")
                .addToBackStack(null)
                .commit();


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