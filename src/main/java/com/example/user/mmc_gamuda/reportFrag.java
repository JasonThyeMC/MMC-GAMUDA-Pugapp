package com.example.user.mmc_gamuda;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link reportFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link reportFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class reportFrag extends Fragment implements FABProgressListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String[] issueType = {
            "Tunnel Light",
            "Switch Panel",
            "Emergency Phone"
    };

    String[] ringNum = {
      "R122","R123", "R124", "R125"
    };

    String[] defaultTitle = new String[] {
            "Select Issue",
            "Select Ring Number"
    };

    String[] realData;

    private OnFragmentInteractionListener mListener;
    private ImageView img;

    int issueindex = -1;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment reportFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static reportFrag newInstance(String param1, String param2) {
        reportFrag fragment = new reportFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public reportFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);

    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){


        final DropDownMenu dropDown = (DropDownMenu) v.findViewById(R.id.menu);
        dropDown.setDefaultMenuTitle(defaultTitle);
        dropDown.setmMenuCount(2);
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


                //Toast.makeText(getActivity().getApplicationContext(), String.valueOf(ColumnIndex), Toast.LENGTH_SHORT).show();

                if (ColumnIndex == 0) {
                    issueindex = RowIndex;

                } else if (ColumnIndex == 1) {
                    switch (issueindex) {
                        case 0:
                            LuxFrag luxfrag = new LuxFrag().newInstance(realData[RowIndex].toString(),issueType[issueindex].toString());
                            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_bottom_in, R.anim.slide_bottom_in).replace(R.id.reportFrag, luxfrag).commit();
                            break;
                        case 1:
                            normalFrag normal = new normalFrag().newInstance(realData[RowIndex].toString(),issueType[issueindex].toString());
                            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_bottom_in, R.anim.slide_bottom_in).replace(R.id.reportFrag, normal).commit();
                            break;

                        case 2:

                            break;
                    }

                }
            }
        });

        Firebase ref = new Firebase("https://sizzling-fire-1548.firebaseio.com/Ring");

        ValueEventListener ringListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                realData  = new String[(int) dataSnapshot.getChildrenCount()];
                List<String> options = new ArrayList<String>();
                int counter = 0;


                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    //ringModel model = postSnapshot.getValue(ringModel.class);
                        realData[counter] = postSnapshot.getKey();
                        counter = counter +1;

                    //options.add(postSnapshot.getKey());
                }

                List<String[]> dropItems = new ArrayList<>();
                dropItems.add(issueType);
                dropItems.add(realData);
                dropDown.setmMenuItems(dropItems);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        ref.addValueEventListener(ringListener);

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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
