package com.example.user.mmc_gamuda;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;
import com.shaded.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shaded.fasterxml.jackson.annotation.JsonProperty;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class RingFrag extends Fragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listview;
    private View root;
    int nextNum = 1;
    private Util utilFunc;

    DropDownMenu dropDown;
    String[] realData;
    String[] secondDrop;
    String actionBarTitle;


    String[] loading = new String[] {

            "Loading"
    };

    String[] noEquipment = new String[] {
            "No Equipment"
    };

    String[] firstTitle = new String[] {

            "Select Ring Number",

    };

    String[] secondTitle = new String[] {
            "Select Equipment"
    };

    String[] equipmentList = new String[] {
            "Tunnel Light",
            "Panel",
            "Emergency Phone"
    };

    int ringIndex;


    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaButton;
    private RapidFloatingActionHelper  rfabHelper;

    private OnFragmentInteractionListener mListener;

    List<RFACLabelItem> items;
    RapidFloatingActionContentLabelList rfaContent;


    public RingFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        utilFunc = new Util();
        mListener.ringFragUpdateActionBar("Tunnel Database");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            outputDropDown();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ring, container, false);
    }


    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){

        root =v;
        outputDropDown();


        rfaButton = (RapidFloatingActionButton) v.findViewById(R.id.activity_main_rfab);
        //rfaButton.set
        rfaLayout = (RapidFloatingActionLayout ) v.findViewById(R.id.activity_main_rfal);
        rfaContent = new RapidFloatingActionContentLabelList(getActivity().getApplicationContext());
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("Add Ring")
                        .setLabelSizeSp(20)
                        .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("Add Tunnel Light")
                        .setLabelSizeSp(20)
                        .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("Add Panel")
                        .setLabelSizeSp(20)
                        .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("Add Emergency Phone")
                        .setLabelSizeSp(20)
                        .setWrapper(0)
        );


        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(getActivity().getApplicationContext(), 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(getActivity().getApplicationContext(), 5));

        rfabHelper = new RapidFloatingActionHelper(
                getActivity().getApplicationContext(),
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();

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
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        rfabHelper.toggleContent();


        switch(position){
            case 0:
                if(nextNum==1){
                    addRing(nextNum);
                    nextNum = nextNum +1;
                }

                Firebase ref = new Firebase("https://sizzling-fire-1548.firebaseio.com/Ring");
                Query queryRef = ref.orderByKey().limitToLast(1);
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            nextNum = Integer.parseInt(postSnapshot.getKey().toString().replaceAll("\\D+", "")) + 1;
                            addRing(nextNum);
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                utilFunc.writeLog("Passed By", "Current");


                break;
            case 1:

                //inputDropDown(root);
                EquipmetnInputFrag tnlight = new EquipmetnInputFrag().newInstance("Tunnel Light",null);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_bottom_in, R.anim.slide_bottom_in).replace(R.id.ringFrag, tnlight).addToBackStack(null).commit();

                break;
            case 2:
                EquipmetnInputFrag panel = new EquipmetnInputFrag().newInstance("Panel",null);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_bottom_in, R.anim.slide_bottom_in).replace(R.id.ringFrag, panel).addToBackStack(null).commit();
                break;
            case 3:
                EquipmetnInputFrag emergencyPhone = new EquipmetnInputFrag().newInstance("Emergency Phone",null);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_bottom_in, R.anim.slide_bottom_in).replace(R.id.ringFrag, emergencyPhone).addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {

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
        public void ringFragUpdateActionBar(String actionBarTItle);
        public String ringFragGetActionbar();
        public void enableUpBtn();
}


    public void outputDropDown(){

        dropDown = (DropDownMenu) root.findViewById(R.id.menu);
        dropDown.setDefaultMenuTitle(firstTitle);
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
        //dropDown.setmDownArrow(R.drawable.arrow_down);

        dropDown.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            //Menu展开的list点击事件  RowIndex：list的索引  ColumnIndex：menu的索引
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {

                ringIndex = Integer.valueOf(realData[RowIndex]);

                mListener.enableUpBtn();
                utilFunc.writeLog(String.valueOf(ringIndex), "Second Drop");

                actionBarTitle = "Ring" +realData[RowIndex];
                mListener.ringFragUpdateActionBar("Ring " + realData[RowIndex]);

                Firebase ref = new Firebase("https://sizzling-fire-1548.firebaseio.com/Ring/" + ringIndex);

                ValueEventListener ringListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int counter = 0;
                        int size = 0;

                        //ringModel model = dataSnapshot.getValue(ringModel.class);

                        if(dataSnapshot.hasChild("TunnelLight")){
                            size = size + 1;

                        }
                        if(dataSnapshot.hasChild("Panel")){
                            size = size + 1;

                        }
                        if(dataSnapshot.hasChild("EmergencyPhone")){
                            size = size + 1;

                        }

                        if(size != 0){

                            secondDrop  = new String[size];

                            if(dataSnapshot.hasChild("TunnelLight")){
                                secondDrop[counter] = "Tunnel Light";
                                counter = counter + 1;

                            }
                            if(dataSnapshot.hasChild("Panel")){
                                secondDrop[counter] = "Panel";
                                counter = counter + 1;

                            }
                            if(dataSnapshot.hasChild("EmergencyPhone")){
                                secondDrop[counter] = "Emergency Phone";

                            }

                            dropDown.setMenuSelectedListener(new OnMenuSelectedListener() {
                                @Override
                                //Menu展开的list点击事件  RowIndex：list的索引  ColumnIndex：menu的索引
                                public void onSelected(View listview, int RowIndex, int ColumnIndex) {

                                    mListener.ringFragUpdateActionBar(mListener.ringFragGetActionbar() + "-" + secondDrop[RowIndex]);
                                    DataFrag data = new DataFrag().newInstance(String.valueOf(ringIndex),secondDrop[RowIndex].toString().trim().replaceAll("\\s+", ""));
                                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_bottom_in, R.anim.slide_bottom_in).replace(R.id.ringFrag, data).addToBackStack(null).commit();
                                }
                            });

                            List<String[]> drop = new ArrayList<>();
                            drop.add(secondDrop);
                            dropDown.setDefaultMenuTitle(secondTitle);
                            dropDown.setmMenuItems(drop);


                        } else {

                            dropDown.setMenuSelectedListener(new OnMenuSelectedListener() {
                                @Override
                                //Menu展开的list点击事件  RowIndex：list的索引  ColumnIndex：menu的索引
                                public void onSelected(View listview, int RowIndex, int ColumnIndex) {


                                }
                            });
                            List<String[]> drop = new ArrayList<>();
                            drop.add(noEquipment);
                            dropDown.setDefaultMenuTitle(secondTitle);
                            dropDown.setmMenuItems(drop);

                        }


                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                };
                ref.addValueEventListener(ringListener);


            }
        });


        Firebase ref = new Firebase("https://sizzling-fire-1548.firebaseio.com/Ring");

        ValueEventListener ringListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int counter = 0;
                int size = 0;

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    if(postSnapshot.hasChild("TunnelLight")||postSnapshot.hasChild("Panel")||postSnapshot.hasChild("EmergencyPhone")){
                        size = size + 1;
                    }
                }

                realData  = new String[size];

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    //ringModel model = postSnapshot.getValue(ringModel.class);

                    if(postSnapshot.hasChild("TunnelLight")||postSnapshot.hasChild("Panel")||postSnapshot.hasChild("EmergencyPhone")){
                        realData[counter] = postSnapshot.getKey();
                        counter = counter +1;
                    }

                    //options.add(postSnapshot.getKey());

                }

                List<String[]> dropItems = new ArrayList<>();
                dropItems.add(realData);
                dropDown.setmMenuItems(dropItems);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        ref.addValueEventListener(ringListener);
    }

private void addRing(int ringNum){
    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String strDate = sdf.format(c.getTime());

    utilFunc.writeToDatabase("/Ring/" + ringNum , "Created" , strDate);

}


}



