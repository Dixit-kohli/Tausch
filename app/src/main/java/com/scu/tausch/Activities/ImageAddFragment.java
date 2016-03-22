package com.scu.tausch.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.content.DialogInterface;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.scu.tausch.DB.DBAccessor;
import com.scu.tausch.DTO.OfferDTO;
import com.scu.tausch.Misc.Constants;
import com.scu.tausch.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Praneet on 2/11/16.
 */
public class ImageAddFragment extends Fragment {

    private final int REQUEST_CAMERA = 1;
    private final int SELECT_FILE = 2;
    private ImageView currentImageView;
    private OfferDTO offerDTO;
    public static HomePage context;
    //private boolean isOfferEditable=false;
//    private Bitmap bitmapImageOne;
//    private ParseObject editableItemObject;

    public ImageAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBAccessor.searchCode = Constants.SEARCH_CODE_HOME_PAGE;

    }

//    public void setArgumentsForUpdateComplete(ParseObject itemObject){
//
//        isOfferEditable=true;
//
//        editableItemObject=itemObject;
//
//        ParseFile imageFileOne = (ParseFile)itemObject.get("image_one");
//        imageFileOne.getDataInBackground(new GetDataCallback() {
//            @Override
//            public void done(byte[] data, com.parse.ParseException e) {
//                if (e == null) {
//                    // data has the bytes for the image
//                    bitmapImageOne = BitmapFactory.decodeByteArray(data, 0, data.length);
//
//
//                } else {
//                    // something went wrong
//                }
//            }
//        });
//
//
//
//
//    }

    public void currentOfferDetails(OfferDTO offerDTO){
        this.offerDTO=offerDTO;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_add, container, false);

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                return false;
            }
        });

        Button buttonPost = (Button) rootView.findViewById(R.id.button_post);
        Button buttonCancel = (Button) rootView.findViewById(R.id.button_cancel);
        final ImageView imageViewOne = (ImageView) rootView.findViewById(R.id.image_one);
        final ImageView imageViewTwo = (ImageView) rootView.findViewById(R.id.image_two);
        final ImageView imageViewThree = (ImageView) rootView.findViewById(R.id.image_three);
        final ImageView imageViewFour = (ImageView) rootView.findViewById(R.id.image_four);
        final ImageView imageViewFive = (ImageView) rootView.findViewById(R.id.image_five);

//        if (isOfferEditable){
//            imageViewOne.setImageBitmap(Bitmap.createScaledBitmap(bitmapImageOne, 120, 120, false));
//        }

        imageViewOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageView = imageViewOne;
                selectImage();
            }
        });

        imageViewTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageView=imageViewTwo;
                selectImage();
            }
        });

        imageViewThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageView=imageViewThree;
                selectImage();
            }
        });

        imageViewFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageView=imageViewFour;
                selectImage();
            }
        });

        imageViewFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageView=imageViewFive;
                selectImage();
            }
        });

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser user = ParseUser.getCurrentUser();
                boolean isUserVerified = user.getBoolean("emailVerified");


                if(isUserVerified==false){

                    showDialogBoxForUnverfiedUser();
                    return;
                }

//                if (isOfferEditable){
//
//                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Offers");
//                    query.whereEqualTo("objectId", editableItemObject.get("objectId"));
//                    query.findInBackground(new FindCallback<ParseObject>() {
//                        @Override
//                        public void done(List<ParseObject> objects, com.parse.ParseException e) {
//
//                            if (e == null) {
//
//                                Bitmap bitmap = ((BitmapDrawable)imageViewOne.getDrawable()).getBitmap();
//
//                                // Convert it to byte
//                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                // Compress image to lower quality scale 1 - 100
//                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                                byte[] image = stream.toByteArray();
//
//                                // Create the ParseFile
//                                ParseFile file = new ParseFile("image.png", image);
//                                // Upload the image into Parse Cloud
//                                file.saveInBackground();
//
//                                // Create a New Class called "ImageUpload" in Parse
//                                ParseObject imgupload = editableItemObject;
//
//                                // Create a column named "ImageName" and set the string
//                                String objectIdUser = (String)ParseUser.getCurrentUser().getObjectId();
//                                imgupload.put("user_id", objectIdUser);
//
//                                imgupload.put("category_id",offerDTO.getCategoryId());
//
//                                imgupload.put("offer_title",offerDTO.getOfferTitle());
//
//                                imgupload.put("offer_description",offerDTO.getOfferDescription());
//
//                                imgupload.put("price",offerDTO.getPrice());
//
//                                imgupload.put("condition",offerDTO.getCondition());
//
//                                imgupload.put("zipcode",offerDTO.getZip());
//
//                                imgupload.put("offeror",offerDTO.getOfferorName());
//
//                                imgupload.put("city",offerDTO.getCityId());
//
//                                // Create a column named "ImageFile" and insert the image
//                                imgupload.put("image_one", file);
//
//                                // Create the class and the columns
//                                imgupload.saveInBackground();
//
//                                Fragment fragmentToRemove = getFragmentManager().findFragmentByTag("tagImageAdd");
//                                getActivity().getSupportFragmentManager().beginTransaction().remove(fragmentToRemove).commit();
//
//                                //After removing fragment in above line, we popBackStack() to remove from stack.
//                                getFragmentManager().popBackStack();
//
//                                HomeFragment nextFrag= new HomeFragment();
//
//                                ImageAddFragment.this.getFragmentManager().beginTransaction()
//                                        .replace(R.id.container_body, nextFrag)
//                                        .commit();
//
//                                isOfferEditable=false;
//
//                                return;
//                            } else {
//                                Log.d("Post retrieval", "Error: " + e.getMessage());
//                            }
//
//                        }
//                    });
//
//
//                }

                Bitmap bitmap = ((BitmapDrawable)imageViewOne.getDrawable()).getBitmap();

                // Convert it to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();

                // Create the ParseFile
                ParseFile file = new ParseFile("image.png", image);
                // Upload the image into Parse Cloud
                file.saveInBackground();

                // Create a New Class called "ImageUpload" in Parse
                ParseObject imgupload = new ParseObject("Offers");

                // Create a column named "ImageName" and set the string
                String objectIdUser = (String)ParseUser.getCurrentUser().getObjectId();
                imgupload.put("user_id", objectIdUser);

                imgupload.put("category_id",offerDTO.getCategoryId());

                imgupload.put("offer_title",offerDTO.getOfferTitle());

                imgupload.put("offer_description",offerDTO.getOfferDescription());

                imgupload.put("price",offerDTO.getPrice());

                imgupload.put("condition",offerDTO.getCondition());

                imgupload.put("zipcode",offerDTO.getZip());

                imgupload.put("offeror",offerDTO.getOfferorName());

                imgupload.put("city",offerDTO.getCityId());

                // Create a column named "ImageFile" and insert the image
                imgupload.put("image_one", file);

                // Create the class and the columns
                imgupload.saveInBackground();

                Fragment fragmentToRemove = getFragmentManager().findFragmentByTag("tagImageAdd");
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragmentToRemove).commit();

                //After removing fragment in above line, we popBackStack() to remove from stack.
                getFragmentManager().popBackStack();

                HomeFragment nextFrag= new HomeFragment();

                ImageAddFragment.this.getFragmentManager().beginTransaction()
                        .replace(R.id.container_body, nextFrag)
                        .commit();

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragmentToRemove = getFragmentManager().findFragmentByTag("tagImageAdd");
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragmentToRemove).commit();

                //After removing fragment in above line, we popBackStack() to remove from stack.
                getFragmentManager().popBackStack();

                HomeFragment nextFrag= new HomeFragment();

                ImageAddFragment.this.getFragmentManager().beginTransaction()
                        .replace(R.id.container_body, nextFrag)
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


    //Options provided for image selection. Either gallery or camera.
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    //Fetching image and applying it to imageView after compressing it.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    bytes.close();
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentImageView.setImageBitmap(thumbnail);
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(getActivity(), selectedImageUri, projection, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                currentImageView.setImageBitmap(bm);
            }
        }

    }

    private void showDialogBoxForUnverfiedUser(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("You must verify your Email!");

        alertDialogBuilder.setPositiveButton("Resend Email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                DBAccessor.getInstance().updateEmailForVerificationAgain(context);

                Fragment fragmentToRemove = getFragmentManager().findFragmentByTag(Constants.TAG_Image_Add);
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragmentToRemove).commit();

                //After removing fragment in above line, we popBackStack() to remove from stack.
                getFragmentManager().popBackStack();

                HomeFragment nextFrag= new HomeFragment();

                ImageAddFragment.this.getFragmentManager().beginTransaction()
                        .replace(R.id.container_body, nextFrag)
                        .commit();




            }
        });

        alertDialogBuilder.setNegativeButton("Cancel",null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}