package com.example.user.mmc_gamuda;

import android.app.Activity;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import android.os.CountDownTimer;
//import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link normalFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link normalFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class normalFrag extends Fragment implements FABProgressListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //SimpleDraweeView draweeView;
    ImageView img;
    String ringNumber;
    String equipmentType;
    EditText description;
    private FABProgressCircle fabProgressCircle;
    Util utilFunc;
    private OnFragmentInteractionListener mListener;
    String strDate;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment normalFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static normalFrag newInstance(String param1, String param2) {
        normalFrag fragment = new normalFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public normalFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ringNumber = getArguments().getString(ARG_PARAM1);
            equipmentType = getArguments().getString(ARG_PARAM2);
        }
        utilFunc = new Util();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        strDate = sdf.format(c.getTime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_normal, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        //draweeView = (SimpleDraweeView) v.findViewById(R.id.normFragImg);
        img = (ImageView) v.findViewById(R.id.normFragImg);
        description = (EditText) v.findViewById(R.id.description);
        fabProgressCircle = (FABProgressCircle) v.findViewById(R.id.fabProgressCircle);
        fabProgressCircle.attachListener(this);

        v.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabProgressCircle.show();
                String now = strDate;
                if(utilFunc.writeToDatabase("Issues/" + now,"ringNum", ringNumber)
                && utilFunc.writeToDatabase("Issues/" + now, "description", description.getText().toString())
                && utilFunc.writeToDatabase("Issues/" + now, "equipment", equipmentType)){

                    fabProgressCircle.beginFinalAnimation();
                } else {
                    fabProgressCircle.hide();
                }


            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File photoPath = null;
                try {
                    photoPath = utilFunc.createImageFile();
                } catch (IOException io){
                    Log.e("Capture Img", io.toString());
                }
                if(photoPath != null){

                    mListener.normFragCapImg(photoPath);

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
        /*Snackbar.make(fabProgressCircle, "Uploaded", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();*/
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
        public void normFragCapImg(File path);
    }

    public void setImage(File path){
        Uri uri = Uri.fromFile(path);
        //draweeView.setImageURI(uri);
        img.setImageURI(uri);
    }

}
