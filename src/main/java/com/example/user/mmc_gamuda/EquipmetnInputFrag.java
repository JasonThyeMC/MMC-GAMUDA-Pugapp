package com.example.user.mmc_gamuda;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class EquipmetnInputFrag extends Fragment implements FABProgressListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText serialNum;
    private EditText description;
    private ImageView equipmentImg;
    private FABProgressCircle fabProgressCircle;
    private OnFragmentInteractionListener mListener;
    private ScrollView mScroll;
    DropDownMenu dropDown;
    String[] dummy = {
            "walala"
    };
    String[] inputData;
    String[] title = new String[] {
            "Select Ring"
    };
    private String selectedRing;
    String actionBarTitle;
    Util utilFunc;
    SimpleDateFormat sdf;
    Calendar c;



    // TODO: Rename and change types and number of parameters
    public EquipmetnInputFrag() {

    }

    public static EquipmetnInputFrag newInstance(String param1, String param2) {
        EquipmetnInputFrag fragment = new EquipmetnInputFrag();
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
            actionBarTitle = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mListener.onFragmentInteraction(actionBarTitle);
        utilFunc = new Util();
        c = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_equipmetn_input, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        inputDropDown(v);
        //mScroll = (ScrollView) v.findViewById(R.id.my_scrollview);
        serialNum = (EditText) v.findViewById(R.id.addSerialNum);
        description = (EditText) v.findViewById(R.id.addDescription);
        equipmentImg = (ImageView) v.findViewById(R.id.addEquipmentImg);
        equipmentImg.setImageResource(R.drawable.tnl_img);
        equipmentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
/*
        serialNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScroll.post(new Runnable() {
                    public void run() {
                        mScroll.scrollTo(0, serialNum.getTop());
                    }
                });

            }
        });*/

        fabProgressCircle = (FABProgressCircle) v.findViewById(R.id.equipmentFAB);
        fabProgressCircle.attachListener(this);

        v.findViewById(R.id.equipmentfabgoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabProgressCircle.show();

                if(actionBarTitle == "Tunnel Light"){

                    if(utilFunc.writeToDatabase("Ring/" + selectedRing + "/TunnelLight", "updated", sdf.format(c.getTime()))
                    &&utilFunc.writeToDatabase("Ring/" + selectedRing + "/TunnelLight", "serialNum", serialNum.getText().toString())
                    &&utilFunc.writeToDatabase("Ring/" + selectedRing + "/TunnelLight", "description", description.getText().toString())){
                        fabProgressCircle.beginFinalAnimation();
                    } else {
                        fabProgressCircle.hide();
                    }


                } else if(actionBarTitle == "Panel") {

                    utilFunc.writeToDatabase("Ring/" + selectedRing + "/Panel" ,"updated", sdf.format(c.getTime()));
                    utilFunc.writeToDatabase("Ring/" + selectedRing + "/Panel", "serialNum", serialNum.getText().toString());
                    utilFunc.writeToDatabase("Ring/" + selectedRing + "/Panel", "description", description.getText().toString());

                } else if (actionBarTitle == "Emergency Phone"){

                    utilFunc.writeToDatabase("Ring/" + selectedRing  + "/EmergencyPhone" ,"updated", sdf.format(c.getTime()));
                    utilFunc.writeToDatabase("Ring/" + selectedRing + "/EmergencyPhone", "serialNum", serialNum.getText().toString());
                    utilFunc.writeToDatabase("Ring/" + selectedRing + "/EmergencyPhone", "description", description.getText().toString());

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
        public void onFragmentInteraction(String title);
        public CharSequence equimentFragGetTile();
    }


    public void inputDropDown(View v){


        dropDown = (DropDownMenu) v.findViewById(R.id.inputDrop);
        dropDown.setDefaultMenuTitle(title);
        dropDown.setmMenuCount(1);
        dropDown.setShowCheck(true);//是否显示展开list的选中项
        dropDown.setmMenuTitleTextSize(16);//Menu的文字大小
        dropDown.setmMenuTitleTextColor(Color.BLACK);//Menu的文字颜色
        dropDown.setmMenuListTextSize(16);//Menu展开list的文字大小
        dropDown.setmMenuListTextColor(Color.BLACK);//Menu展开list的文字颜色
        dropDown.setmMenuBackColor(Color.parseColor("#eeeeee"));//Menu的背景颜色
        dropDown.setmMenuPressedBackColor(Color.WHITE);//Menu按下的背景颜色
        dropDown.setmMenuPressedTitleTextColor(Color.parseColor("#777777"));
        dropDown.setmMenuListSelectorRes(R.color.white);
        dropDown.setmArrowMarginTitle(30);
        dropDown.setmUpArrow(R.drawable.arrow_up);


        Firebase ref = new Firebase("https://sizzling-fire-1548.firebaseio.com/Ring");

        ValueEventListener ringListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                utilFunc.writeLog(dataSnapshot.toString(),"equipDrop");
                int counter = 0;
                int size = 0;

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    if(!postSnapshot.hasChild(actionBarTitle.replaceAll("\\s+", ""))){
                        //utilFunc.writeLog(postSnapshot.toString(), "equipDrop2");
                        size = size + 1;
                    }

                }

                inputData = new String[size];

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    if(!postSnapshot.hasChild(actionBarTitle.replaceAll("\\s+", ""))){
                        inputData[counter] = postSnapshot.getKey();
                        counter = counter +1;
                    }

                }

                dropDown.setMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    //Menu展开的list点击事件  RowIndex：list的索引  ColumnIndex：menu的索引
                    public void onSelected(View listview, int RowIndex, int ColumnIndex) {

                        selectedRing = inputData[RowIndex];
                    }

                });

                List<String[]> dropItems = new ArrayList<>();
                dropItems.add(inputData);
                dropDown.setmMenuItems(dropItems);

                /*
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        ringModel model = postSnapshot.getValue(ringModel.class);
                        Log.i("Over here", postSnapshot.getKey() + " " + model.gettnlight());

                        if(model.get(mListener.equimentFragGetTile().toString()) != true){
                            size = size + 1;
                        }

                        inputData  = new String[size];


                    }

                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        ringModel model = postSnapshot.getValue(ringModel.class);
                        if(model.get(mListener.equimentFragGetTile().toString()) != true){

                            inputData[counter] = postSnapshot.getKey();
                            counter = counter +1;

                        }
                    }

                    List<String[]> dropItems = new ArrayList<>();
                    dropItems.add(inputData);
                    dropDown.setmMenuItems(dropItems);

                    */


            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        ref.addValueEventListener(ringListener);



    }
}


