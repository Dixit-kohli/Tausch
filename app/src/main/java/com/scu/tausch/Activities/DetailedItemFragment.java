package com.scu.tausch.Activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.scu.tausch.DB.DBAccessor;
import com.scu.tausch.Misc.Constants;
import com.scu.tausch.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Praneet on 1/31/16.
 */
public class DetailedItemFragment extends Fragment{

    private ImageButton messageButton;
    private String title;
    private String description;
    private Bitmap image_one, image_two, image_three, image_four, image_five;
    private String item_price;
    private String offeror;
    private String condition;
    private String city;
    String receiverEmail;
    String receiverName;
    String senderName;
    List<Bitmap> itemFiveImages;
    private int imageNumberToDisplay = 0;
    private ImageView leftArrow;
    private ImageView rightArrow;
    private TextView textOfferor;
    private boolean isOfferByOfferor = false;

private String receiverObjectId;

    public DetailedItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBAccessor.searchCode = Constants.SEARCH_CODE_HOME_PAGE;

    }

            public void setArguments(final ParseObject itemObject, Bitmap[] images,int positionInList,String[] titles, String[] prices){

                title = titles[positionInList];
                description = (String)itemObject.get(Constants.DB_OFFER_DESCRIPTION);
                image_one = images[positionInList];
                item_price = prices[positionInList];
                offeror = (String)itemObject.get(Constants.DB_OFFEROR);
                condition = (String)itemObject.get(Constants.DB_CONDITION);
                city = (String)itemObject.get(Constants.DB_CITY);

                itemFiveImages = OffersList.listOfImageLists.get(positionInList);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
               query.whereEqualTo("objectId", itemObject.get("user_id"));
// Retrieve the object by id

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        receiverEmail = (String) (objects.get(0).get("email"));
                        receiverObjectId = (String)itemObject.get("user_id");
                        receiverName = (String)(objects.get(0).get("firstname"));
                        senderName = (String) ParseUser.getCurrentUser().get("firstname");


                        if (receiverEmail.equals(ParseUser.getCurrentUser().getEmail())) {
                            isOfferByOfferor = true;
                            messageButton.setVisibility(View.GONE);
                            textOfferor.setText("You");

                            return;
                        } else {
                            isOfferByOfferor = false;
                            messageButton.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detailed_item, container, false);

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                return false;
            }
        });


        messageButton = (ImageButton)rootView.findViewById(R.id.icon_message_box);
        TextView textTitle = (TextView)rootView.findViewById(R.id.item_title);
        TextView textDescription=(TextView)rootView.findViewById(R.id.item_description);
        final ImageView imageItem = (ImageView) rootView.findViewById(R.id.item_image);
        textOfferor = (TextView) rootView.findViewById(R.id.value_name);
        TextView textPrice = (TextView)rootView.findViewById(R.id.value_price);
        TextView textCondition = (TextView)rootView.findViewById(R.id.value_condition);
        TextView textCity = (TextView)rootView.findViewById(R.id.value_city);
        leftArrow = (ImageView) rootView.findViewById(R.id.arrow_left);
        rightArrow = (ImageView) rootView.findViewById(R.id.arrow_right);


        textTitle.setText(title);
        textDescription.setText(description);
        imageItem.setImageBitmap(image_one);
        textOfferor.setText(offeror);
        textPrice.setText("$" + item_price);
        textCondition.setText(condition);
        textCity.setText(city);
        leftArrow.setVisibility(View.INVISIBLE);


        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageNumberToDisplay > 0) {
                    rightArrow.setVisibility(View.VISIBLE);
                    imageNumberToDisplay--;
                    imageItem.setImageBitmap(itemFiveImages.get(imageNumberToDisplay));
                    if (imageNumberToDisplay == 0) {
                        leftArrow.setVisibility(View.INVISIBLE);
                    }
                }


            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageNumberToDisplay < 4) {
                    leftArrow.setVisibility(View.VISIBLE);
                    imageNumberToDisplay++;
                    imageItem.setImageBitmap(itemFiveImages.get(imageNumberToDisplay));
                    if (imageNumberToDisplay == 4) {
                        rightArrow.setVisibility(View.INVISIBLE);
                    }
                }


            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Constants.WAS_LAST_SCREEN_ITEM_DESCRIPTION = true;

                ChatFragment nextFrag= new ChatFragment();
                nextFrag.setArgumentsForMessageSending(receiverEmail,receiverObjectId, receiverName);


                DetailedItemFragment.this.getFragmentManager().beginTransaction()
                        .replace(R.id.myDetailedItemFragment, nextFrag,Constants.TAG_Chat_Fragment)
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
        Constants.CURRENT_SCREEN = Constants.SCREEN_OFFER_DETAILS_CHAT_WINDOW;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
