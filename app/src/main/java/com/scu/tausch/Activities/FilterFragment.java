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
import android.widget.Button;

import com.scu.tausch.R;

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

        Button buttonNext = (Button)rootView.findViewById(R.id.button_next);

        buttonNext.setOnClickListener(new View.OnClickListener() {

            Fragment fragment = null;
            String title;

            @Override
            public void onClick(View v) {

                    title = getString(R.string.app_name);

                    fragment = new ImageAddFragment();
                    title = getString(R.string.title_filter);


                        ImageAddFragment nextFrag= new ImageAddFragment();

                        FilterFragment.this.getFragmentManager().beginTransaction()
                        .replace(R.id.container_body, nextFrag)
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
