package com.scu.tausch.Activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.scu.tausch.R;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

/**
 * Created by Praneet on 2/11/16.
 */
public class FilterFragment extends Fragment {
    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);

        Spinner spinnerCategory = (Spinner)rootView.findViewById(R.id.spinner_category);
        Spinner spinnerCondition = (Spinner)rootView.findViewById(R.id.spinner_condition);
        Spinner spinnerCity = (Spinner)rootView.findViewById(R.id.spinner_city);

        List<String> categories = new ArrayList<>();
        categories.add("Automobiles");
        categories.add("Books");
        categories.add("Laptops");
        categories.add("Rentals");

        List<String> conditions = new ArrayList<>();
        conditions.add("New");
        conditions.add("Used");

        List<String> cities = new ArrayList<>();
        cities.add("Santa Clara");
        cities.add("San Jose");
        cities.add("Santa Monica");
        cities.add("San Bernardino");
        cities.add("San Diego");
        cities.add("Sacramento");
        cities.add("Santa Barbara");
        cities.add("San Francisco");
        cities.add("Oakland");
        cities.add("Pasadena");
        cities.add("Anaheim");
        cities.add("Beverly Hills");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");
        cities.add("San Francisco");


        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCategory.setAdapter(adapterCategories);

        ArrayAdapter<String> adapterConditions = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, conditions);
        adapterConditions.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCondition.setAdapter(adapterConditions);

        ArrayAdapter<String> adapterCities = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, cities);
        adapterCities.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCity.setAdapter(adapterCities);







        Button buttonNext = (Button)rootView.findViewById(R.id.button_next);

        buttonNext.setOnClickListener(new View.OnClickListener() {

          //  Fragment fragment = null;
            String title;

            @Override
            public void onClick(View v) {

                    title = getString(R.string.app_name);

                 //   fragment = new ImageAddFragment();
                    title = getString(R.string.title_filter);


                        ImageAddFragment nextFrag= new ImageAddFragment();

                //Below, addToBackStack(null) means that if we transition to next fragment, this
                //fragment will not be destroyed and would appear again if user taps back button.
                //addToBackStack(null) works for current fragment, like
                //FilterFragment in this case.

                        FilterFragment.this.getFragmentManager().beginTransaction()
                        .replace(R.id.container_body, nextFrag,"tagImageAdd")
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
