package com.scu.tausch.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ListView;

import com.scu.tausch.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Praneet on 1/31/16.
 */
public class OffersList extends Fragment {

    /*
    * ##################################
    * DATA FOR FOLLOWING 3 ARRAYS WILL BE REPLACED BY THE DATA WE ARE GOING TO FETCH FROM PARSE SERVER.
    * TABLE WOULD HAVE IMAGE, TITLE, DESCRIPTION, COST ETC.
    * ##################################
    * */


    private String[] arrayItemNames = new String[]{"Table","Chair","bicycle","Nortan Antivirus","Shoes","Handmade cotton bookholder and a pack of socks as well as tapes","Table","Table","Table","Table"};
    private int[] arrayItemImages = new int[]{R.mipmap.item_placeholder,R.mipmap.item_placeholder,R.mipmap.item_placeholder,R.mipmap.item_placeholder,R.mipmap.item_placeholder,R.mipmap.item_placeholder,R.mipmap.item_placeholder,R.mipmap.item_placeholder,R.mipmap.item_placeholder,R.mipmap.item_placeholder};
    private  String[] arrayItemCosts = new String[]{"$12","$12","$12","$12","$12","$12","$12","$12","$12","$12"};
    private ListView listViewItems;


    public OffersList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_offers_list, container, false);

        List<HashMap<String,String>> itemNamesImagesCosts = new ArrayList<>();

        for (int i=0; i<arrayItemNames.length;i++){
            HashMap<String,String> hmPairs = new HashMap<>();
            hmPairs.put("item_name",arrayItemNames[i]);
            hmPairs.put("item_image",Integer.toString(arrayItemImages[i]));
            hmPairs.put("item_cost",arrayItemCosts[i]);
            itemNamesImagesCosts.add(hmPairs);
        }

        //Keys used in HashMap.
        String [] from = {"item_image","item_name","item_cost"};

        //Ids used in HashMap.
        int [] to = {R.id.item_image,R.id.item_name,R.id.item_cost};


        // Instantiating an adapter to store each items
        // R.layout.home_category_list defines the layout of each item
        SimpleAdapter itemListAdapter = new SimpleAdapter(getActivity().getBaseContext(),itemNamesImagesCosts,R.layout.category_items_list_row,from,to);

        listViewItems=(ListView)rootView.findViewById(R.id.list_items_in_category);
        listViewItems.setAdapter(itemListAdapter);

        //Setting Y value as 168, that is height of toolbar.
//        listViewCategories.setY(168);


        //On tap of particular item, new fragment should be shown where descriptive information would
        //be available.
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {

//                Toast.makeText(getActivity().getBaseContext(), "Item clicked at" + position, Toast.LENGTH_SHORT).show();

                DetailedItemFragement nextFrag= new DetailedItemFragement();

                OffersList.this.getFragmentManager().beginTransaction()
                        .replace(R.id.myItemsInCategoryWindow, nextFrag)
                        .addToBackStack(null)
                        .commit();

            }
        });



        // Inflate the layout for this fragment
        return rootView;
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
