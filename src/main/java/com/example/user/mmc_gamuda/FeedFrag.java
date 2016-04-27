package com.example.user.mmc_gamuda;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.shaded.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final String TAG = MainActivity.class.getSimpleName();
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private Util utilFunc;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFrag newInstance(String param1, String param2) {
        FeedFrag fragment = new FeedFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FeedFrag() {
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){

        listView = (ListView) v.findViewById(R.id.feedList);

        feedItems = new ArrayList<FeedItem>();

        FeedItem item = new FeedItem("Issues-", "dummy_image_link",
                "test", null,"2015-09-20 22:29:22" , null);
        FeedItem item2 = new FeedItem("Issues-", "dummy_image_link",
                "test", null, "2015-09-21 09:01:26" , null);
        FeedItem item3 = new FeedItem("Issues-", "dummy_image_link",
                "test", null, "2015-09-22 22:57:01" , null);

        feedItems.add(item);
        feedItems.add(item2);
        feedItems.add(item3);


        listAdapter = new FeedListAdapter(getActivity(), feedItems , getActivity().getApplicationContext());

        listView.setAdapter(listAdapter);
        retrieveData();

    }

    private void retrieveData() {

        Firebase ref = new Firebase("https://sizzling-fire-1548.firebaseio.com/Issues/");
        utilFunc.writeLog(String.valueOf(feedItems.size()), "FeedFrag");
        ValueEventListener ringListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for( DataSnapshot postDataSnapShot : dataSnapshot.getChildren()){

                        Issues model = postDataSnapShot.getValue(Issues.class);
                        utilFunc.writeLog(postDataSnapShot.getKey(), "Feed Frag");

                    FeedItem item = new FeedItem("Issues-" + model.getRingNum() + "-" + model.getEquipment(), "dummy_image_link",
                            model.getDescription(), null, postDataSnapShot.getKey() , null);
                    feedItems.add(item);
                }

                listAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        utilFunc.writeLog(String.valueOf(feedItems.size()), "FeedFrag2");
        ref.addValueEventListener(ringListener);
        utilFunc.writeLog(String.valueOf(feedItems.size()), "FeedFrag3");
    }

}

class Issues{

    String description;
    String ringNum;
    String equipment;

    Issues(){

    }

    @JsonProperty("description")
    public String getDescription(){
        return description;
    }

    @JsonProperty("ringNum")
    public String getRingNum(){
        return ringNum;
    }

    @JsonProperty("equipment")
    public String getEquipment(){
        return equipment;
    }

}
