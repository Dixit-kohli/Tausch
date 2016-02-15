package com.scu.tausch.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.scu.tausch.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Praneet on 1/31/16.
 */
public class DetailedItemFragment extends Fragment{

    private ImageButton messageButton;
    private String title;
    private String description;
    private Bitmap image_one;
    private String item_price;
    private String offeror;
    private String condition;
    private String city;

    public DetailedItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

            public void setArguments(ParseObject itemObject, Bitmap[] images,int positionInList,String[] titles, String[] prices){

                title = titles[positionInList];
                description = (String)itemObject.get("offer_description");
                image_one = images[positionInList];
                item_price = prices[positionInList];
                offeror = (String)itemObject.get("offeror");
                condition = (String)itemObject.get("condition");
                city = (String)itemObject.get("city");

            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detailed_item, container, false);

        messageButton = (ImageButton)rootView.findViewById(R.id.icon_message_box);
        TextView textTitle = (TextView)rootView.findViewById(R.id.item_title);
        TextView textDescription=(TextView)rootView.findViewById(R.id.item_description);
        ImageView imageItem = (ImageView)rootView.findViewById(R.id.item_image);
        TextView textOfferor = (TextView)rootView.findViewById(R.id.value_name);
        TextView textPrice = (TextView)rootView.findViewById(R.id.value_price);
        TextView textCondition = (TextView)rootView.findViewById(R.id.value_condition);
        TextView textCity = (TextView)rootView.findViewById(R.id.value_city);


        textTitle.setText(title);
        textDescription.setText(description);
        imageItem.setImageBitmap(image_one);
        textOfferor.setText(offeror);
        textPrice.setText("$"+item_price);
        textCondition.setText(condition);
        textCity.setText(city);


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
//                DetailedItemFragment.this.getFragmentManager().beginTransaction()
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
