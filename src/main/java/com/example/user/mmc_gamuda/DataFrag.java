package com.example.user.mmc_gamuda;

import android.app.Activity;

import android.media.Image;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;
import com.shaded.fasterxml.jackson.annotation.JsonProperty;

import org.w3c.dom.Text;

public class DataFrag extends Fragment implements FABProgressListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView img;
    private FABProgressCircle fabProgressCircle;

    private OnFragmentInteractionListener mListener;
    private String ringNum;
    private String equipment;
    private TextView serialNum;
    private TextView description;
    EditText serialNumEdit;
    EditText  descriptionEdit;
    private Util utilFunc;
    private String equipmentType;
    private boolean serialClicked =false;
    private boolean descriptclicked =false;



    public DataFrag() {

    }

    public static DataFrag newInstance(String param1, String param2) {
        DataFrag fragment = new DataFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ringNum = getArguments().getString(ARG_PARAM1);
            equipment = getArguments().getString(ARG_PARAM2);
        }
        switch(equipment){
            case "TunnelLight":
                equipmentType = "TunnelLight";
                break;
            case "Panel":
                equipmentType = "Panel";
                break;
            case "EmergencyPhone":
                equipmentType = "EmergencyPhone";
                break;
        }
        utilFunc = new Util();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        img = (ImageView) v.findViewById(R.id.dataFragImage);
        img.setImageResource(R.drawable.tnl_img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Matrix matrix = img.getDisplayMatrix();
                img.setImageBitmap( bitmap, matrix );
                */
            }
        });
        final ViewSwitcher serialNumSwitcher = (ViewSwitcher) v.findViewById(R.id.serialNumSwitcher);
        final ViewSwitcher descriptionSwitcher = (ViewSwitcher) v.findViewById(R.id.descriptionSwitcher);
        serialNum = (TextView) serialNumSwitcher.findViewById(R.id.serialNumTitle);
        serialNumEdit = (EditText) serialNumSwitcher.findViewById(R.id.serialNumEdit);
        descriptionEdit = (EditText) descriptionSwitcher.findViewById(R.id.equipmentEdit);
        description = (TextView) descriptionSwitcher.findViewById(R.id.equipmentDescription);
        fabProgressCircle = (FABProgressCircle) v.findViewById(R.id.fabProgressCircleDataFrag);
        fabProgressCircle.setVisibility(View.GONE);
        //serialNum.setText("A253");
        //description.setText("The Firebase library must be initialized once with an Android context. " +
        //        "This must happen before any Firebase app reference is created or used.");
        retrieveData(ringNum);
        serialNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                serialClicked = true;
                fabProgressCircle.setVisibility(View.VISIBLE);
                serialNumSwitcher.showNext();

                serialNumEdit.setText(serialNum.getText().toString());
            }
        });
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descriptclicked = true;
                fabProgressCircle.setVisibility(View.VISIBLE);
                descriptionSwitcher.showNext();
                descriptionEdit.setText(description.getText().toString());
            }
        });


        fabProgressCircle.attachListener(this);

        v.findViewById(R.id.fabDataFrag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabProgressCircle.show();
                Boolean firstWrite = false;
                Boolean secondWrite = false;
                //utilFunc.writeToDatabase(ringNum + "/" + equipmentType, "serialNum", "", null);
                if(serialClicked){
                    firstWrite = utilFunc.writeToDatabase("Ring/" + ringNum + "/" + equipmentType, "serialNum", serialNumEdit.getText().toString());
                    utilFunc.writeLog(serialNumEdit.getText().toString(),"DataFrag");
                }

                if(descriptclicked){
                    secondWrite = utilFunc.writeToDatabase("Ring/" +ringNum + "/" + equipmentType,"description",descriptionEdit.getText().toString());
                }

                if(serialClicked && descriptclicked){
                    if(!firstWrite || !secondWrite){
                        fabProgressCircle.hide();
                    } else if(firstWrite && secondWrite){
                        fabProgressCircle.beginFinalAnimation();
                    }
                } else if(serialClicked && !descriptclicked) {

                    if(firstWrite){
                        fabProgressCircle.beginFinalAnimation();
                    } else {
                        fabProgressCircle.hide();
                    }
                } else {

                    if(secondWrite){
                        fabProgressCircle.beginFinalAnimation();
                    } else {
                        fabProgressCircle.hide();
                    }
                }


            }
        });

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFABProgressAnimationEnd() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public int dp2px(int dp){

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void retrieveData(String ring){

        Firebase ref = new Firebase("https://sizzling-fire-1548.firebaseio.com/Ring/" + ring );

        ValueEventListener ringListener = new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for( DataSnapshot postDataSnapShot : dataSnapshot.getChildren()){


                    utilFunc.writeLog(postDataSnapShot.getKey(),"Data Frag");

                    if(postDataSnapShot.getKey() == equipmentType){
                        equipmentModel model = postDataSnapShot.getValue(equipmentModel.class);
                        utilFunc.writeLog(model.getSerialNum(),"Data Frag");
                        serialNum.setText(model.getSerialNum());
                        description.setText(model.getDescription());

                    }

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        ref.addListenerForSingleValueEvent(ringListener);
    }

}

class equipmentModel{

    String description;
    String serialNum;
    String updated;

    equipmentModel(){

    }


    @JsonProperty("description")
    public String getDescription(){
        return description;
    }

    @JsonProperty("serialNum")
    public String getSerialNum(){
        return serialNum;
    }

    @JsonProperty("updated")
    public String getUpdated(){
        return updated;
    }





}
