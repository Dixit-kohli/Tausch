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
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.scu.tausch.DB.DBAccessor;
import com.scu.tausch.DTO.OfferDTO;
import com.scu.tausch.Misc.Constants;
import com.scu.tausch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditOfferFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditOfferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditOfferFragment extends Fragment implements DBListener{

    public static HomePage context;
    private OfferDTO offerDTO;
    private ProgressDialog progress;
    private TextView textCityName;
    private boolean isOfferEditable=false;
    private String updateSpinnerCategory;
    private String updateSpinnerCondition;
    private String updateEditTitle;
    private String updateEditDescription;
    private String updateEditPrice;
    private String updateEditZip;
    private String updateTextCityName;
    private int categoryPosition;
    private ParseObject editableObject;

   public EditOfferFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setArgumentsForUpdate(ParseObject itemObject){

        isOfferEditable=true;
        String categoryID = (String)itemObject.get("category_id");
        updateSpinnerCategory = getCategoryName(categoryID);
        updateSpinnerCondition = (String)itemObject.get("condition");
        updateEditTitle = (String)itemObject.get("offer_title");
        updateEditDescription=(String)itemObject.get("offer_description");
        updateEditPrice=(String)itemObject.get("price");
        updateEditZip=(String)itemObject.get("zipcode");
        updateTextCityName=(String)itemObject.get("city");
        editableObject=itemObject;

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


        //adding all categories in list.
        List<String> categories = new ArrayList<>();
        categories.add(Constants.Array_Category_Automobiles);
        categories.add(Constants.Array_Category_Books);
        categories.add(Constants.Array_Category_Laptops);
        categories.add(Constants.Array_Category_Furniture);
        categories.add(Constants.Array_Category_Rentals);

        //adding type whether used or new to list.
        List<String> conditions = new ArrayList<>();
        conditions.add(Constants.ITEM_TYPE_NEW);
        conditions.add(Constants.ITEM_TYPE_USED);



        //Creating and setting adapter to array of categories required in spinner.
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCategory.setAdapter(adapterCategories);

        //Creating and setting adapter to array of conditions required in spinner.
        ArrayAdapter<String> adapterConditions = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, conditions);
        adapterConditions.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCondition.setAdapter(adapterConditions);

        if (isOfferEditable){

            spinnerCategory.setSelection(categoryPosition);
            spinnerCondition.setSelection(updateSpinnerCondition.equals("New") ? 0 : 1);
            editTitle.setText(updateEditTitle);
            editDescription.setText(updateEditDescription);
            editPrice.setText(updateEditPrice);
            editZip.setText(updateEditZip);
            textCityName.setText(updateTextCityName);

        }

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

                progress = new ProgressDialog(getActivity());
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.show();

                //fetching the value of city from server by providing the zip code.
                DBAccessor.getInstance().getUpdatedCityForZip(editZip.getText().toString().trim(), context);




            }
        });



        // Inflate the layout for this fragment
        return rootView;
    }


    //Verification if all the required fields in the form are provided by user.
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

    //Get the id of category used in database.
    public String getCategoryId(String categoryName){

        if (categoryName.equals(Constants.Array_Category_Automobiles)){
            return Constants.CATEGORY_AUTOMOBILES_OBJECT_ID;
        }
        if (categoryName.equals(Constants.Array_Category_Books)){
            return Constants.CATEGORY_BOOKS_OBJECT_ID;
        }
        if (categoryName.equals(Constants.Array_Category_Laptops)){
            return Constants.CATEGORY_LAPTOPS_OBJECT_ID;
        }
        if (categoryName.equals(Constants.Array_Category_Furniture)){
            return Constants.CATEGORY_FURNITURE_OBJECT_ID;
        }
        if (categoryName.equals(Constants.Array_Category_Rentals)){
            return Constants.CATEGORY_RENTALS_OBJECT_ID;
        }
        return null;
    }

    public String getCategoryName(String categoryID){


        if (Constants.CATEGORY_AUTOMOBILES_OBJECT_ID.equals(categoryID)){
            categoryPosition=0;
            return Constants.Array_Category_Automobiles;
        }

        if (Constants.CATEGORY_BOOKS_OBJECT_ID.equals(categoryID)){
            categoryPosition=1;
            return Constants.Array_Category_Books;
        }
        if (Constants.CATEGORY_LAPTOPS_OBJECT_ID.equals(categoryID)){
            categoryPosition=2;
            return Constants.Array_Category_Laptops;
        }
        if (Constants.CATEGORY_FURNITURE_OBJECT_ID.equals(categoryID)){
            categoryPosition=3;
            return Constants.Array_Category_Furniture;
        }
        if (Constants.CATEGORY_RENTALS_OBJECT_ID.equals(categoryID)){
            categoryPosition=4;
            return Constants.Array_Category_Rentals;
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
        offerDTO.setCityId((String) (objects.get(0).get(Constants.DB_PRIMARY_CITY)));
        textCityName.setText(offerDTO.getCityId());

        boolean isCompleted = isFormComplete(offerDTO.getOfferTitle(),offerDTO.getOfferDescription(),offerDTO.getPrice(),offerDTO.getZip(),offerDTO.getCityId());

        if (isCompleted==false){
            return;
        }


        EditImageFragment nextFrag= new EditImageFragment();

        if (isOfferEditable && editableObject!=null){

            nextFrag.setArgumentsForUpdateComplete(editableObject);

            isOfferEditable=false;
        }

        nextFrag.currentOfferDetails(offerDTO);

        //Below, addToBackStack(null) means that if we transition to next fragment, this
        //fragment will not be destroyed and would appear again if user taps back button.
        //addToBackStack(null) works for current fragment, like
        //AddOfferFragment in this case.

        EditOfferFragment.this.getFragmentManager().beginTransaction()
                .replace(R.id.container_body, nextFrag, Constants.TAG_Image_Edit)
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
