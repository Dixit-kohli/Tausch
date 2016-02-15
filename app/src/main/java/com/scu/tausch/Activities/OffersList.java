package com.scu.tausch.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.scu.tausch.Adapters.CustomListAdapter;
import com.scu.tausch.R;

import com.parse.ParseException;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_offers_list, container, false);


        listViewItems=(ListView)rootView.findViewById(R.id.list_items_in_category);




        // Inflate the layout for this fragment
        return rootView;
    }

    //Method is called when data has been fetched from server.
    public void fetchedDataFromServer(){

        CustomListAdapter customListAdapter = new CustomListAdapter(getActivity(),arrayItemNames,arrayItemCosts,arrayItemImages);
        listViewItems.setAdapter(customListAdapter);

        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                DetailedItemFragment nextFrag= new DetailedItemFragment();

                nextFrag.setArguments(itemObjects.get(position),arrayItemImages,position,arrayItemNames,arrayItemCosts);

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
        itemObjects=objects;

        List<String> arrayTitles = new ArrayList<>();
        List<Bitmap> arrayImages = new ArrayList<>();
        List<String> arrayPrice = new ArrayList<>();

        for(ParseObject itemObject:itemObjects){

            String itemTitle = (String)itemObject.get("offer_title");
            arrayTitles.add(itemTitle);
            String itemPrice = (String)itemObject.get("price");
            arrayPrice.add(itemPrice);

            try {
                ParseFile bum = (ParseFile) itemObject.get("image_one");
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
