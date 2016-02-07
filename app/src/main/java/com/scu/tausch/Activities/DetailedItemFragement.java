package com.scu.tausch.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.scu.tausch.R;

/**
 * Created by Praneet on 1/31/16.
 */
public class DetailedItemFragement extends Fragment{

    private ImageButton messageButton;

    public DetailedItemFragement() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detailed_item, container, false);

        messageButton = (ImageButton)rootView.findViewById(R.id.icon_message_box);

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(getActivity().getBaseContext(),"Message button is tapped",Toast.LENGTH_SHORT).show();


                /*
                * DO NOT DELETE FOLLOWING LINES OF CODE. TO BE REORGANIZED AFTER ALL OTHER VIEW ARE DONE.
                *
                * */
//                ChatFragment nextFrag= new ChatFragment();
//
//                DetailedItemFragement.this.getFragmentManager().beginTransaction()
//                        .replace(R.id.myDetailedItemFragment, nextFrag)
//                        .addToBackStack(null)
//                        .commit();

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
