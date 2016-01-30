package com.scu.tausch.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.scu.tausch.R;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemClickListener;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Praneet on 1/29/16.
 */
public class MenuFragment extends Fragment{
    private String[] arrayCategoryNames = new String[]{"AUTOMOBILES","BOOKS","LAPTOPS","RENTALS"};
    private int[] arrayCategoryImages = new int[]{R.mipmap.ic_category_automobiles,R.mipmap.ic_category_books,R.mipmap.ic_category_laptops,R.mipmap.ic_category_rentals};
    private ListView listViewCategories;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        List<HashMap<String,String>> categoriesNamesImages = new ArrayList<>();

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
        SimpleAdapter categoryListAdapter = new SimpleAdapter(getActivity().getBaseContext(),categoriesNamesImages,R.layout.home_category_list,from,to);

       listViewCategories=(ListView)rootView.findViewById(R.id.list_categories);
        listViewCategories.setAdapter(categoryListAdapter);

        //Setting Y value as 168, that is height of toolbar.
//        listViewCategories.setY(168);


        listViewCategories.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {

                Toast.makeText(getActivity().getBaseContext(), "Item clicked at" + position, Toast.LENGTH_SHORT).show();


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