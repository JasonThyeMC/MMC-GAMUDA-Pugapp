package com.example.user.mmc_gamuda;

import android.app.Activity;


import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
//import android.support.;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

//import com.facebook.drawee.view.SimpleDraweeView;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class LuxFrag extends Fragment implements SensorEventListener , FABProgressListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView lux;
    private EditText ringNum;
    private EditText description;
    private FABProgressCircle fabProgressCircle;
    //SimpleDraweeView draweeView;
    ImageView img;
    private OnFragmentInteractionListener mListener;
    Uri uri;
    Util utilFunc;
    String ringNumber;
    String equipmentType;
    ViewSwitcher luximgSwtich;
    byte[] inputData;
    private File imgFile;



    public LuxFrag() {

    }

    public static LuxFrag newInstance(String param1, String param2) {
        LuxFrag fragment = new LuxFrag();
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
            ringNumber = getArguments().getString(ARG_PARAM1);
            equipmentType = getArguments().getString(ARG_PARAM2);

        }

        SensorManager mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
        utilFunc = new Util();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lux, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        luximgSwtich = (ViewSwitcher) v.findViewById(R.id.luxViewSwitcher);
        lux = (TextView) luximgSwtich.findViewById(R.id.luxData);
        //draweeView = (SimpleDraweeView) v.findViewById(R.id.luxImg);
        img = (ImageView) v.findViewById(R.id.luxImg);
        description = (EditText) v.findViewById(R.id.description);
        fabProgressCircle = (FABProgressCircle) v.findViewById(R.id.fabProgressCircle);
        fabProgressCircle.attachListener(this);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String strDate = sdf.format(c.getTime());

        v.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabProgressCircle.show();

                String currentTime = strDate;
                utilFunc.uploadImg(getActivity().getApplicationContext(),currentTime, imgFile);
                if(utilFunc.writeToDatabase("Issues/" + currentTime, "ringNum", ringNumber)
                        && utilFunc.writeToDatabase("Issues/" + currentTime, "description", description.getText().toString())
                        && utilFunc.writeToDatabase("Issues/" + currentTime, "equipment", equipmentType)){

                    fabProgressCircle.beginFinalAnimation();

                } else {
                    fabProgressCircle.hide();
                }
                InputStream iStream;
                try {
                    iStream = getActivity().getContentResolver().openInputStream(uri);
                    byte[] inputData = utilFunc.getBytes(iStream);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(inputData != null){
                    //utilFunc.writeToGCS(strDate,inputData);
                    utilFunc.writeLog("Image upload", "LuxFrag");
                }

            }
        });
        lux.setText("0");
        lux.setBackgroundColor(Color.parseColor("#27d68b"));

        luximgSwtich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File photoPath = null;

                try {
                    photoPath = utilFunc.createImageFile();
                } catch (IOException io){
                    Log.e("Capture Img", io.toString());
                }
                if(photoPath != null){

                    mListener.LuxFragCaptureImg(photoPath);

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
    public void onSensorChanged(SensorEvent event) {
        lux.setText(String.valueOf(event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
        public void LuxFragCaptureImg(File path);

    }

    public void setImage(File path){
        imgFile = path;
        luximgSwtich.showNext();
        uri = Uri.fromFile(path);
        //draweeView.setImageURI(uri);
        img.setImageURI(uri);
    }


}
