package com.example.user.mmc_gamuda;





import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;


import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
//import com.facebook.drawee.backends.pipeline.Fresco;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.File;
import java.util.Calendar;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,FeedFrag.OnFragmentInteractionListener, EquipmetnInputFrag.OnFragmentInteractionListener, RingFrag.OnFragmentInteractionListener,DataFrag.OnFragmentInteractionListener, reportFrag.OnFragmentInteractionListener, LuxFrag.OnFragmentInteractionListener, normalFrag.OnFragmentInteractionListener {

    ViewPager tabView;
    private FragmentManager mFragmentManager;
    File imgPath;
    MyApplication app;
    SharedPreferences pref;
    Util util;
    int accessLevel = 1;
    MyAdapter mMyAdapter;
    String[] actionBarTitle = {
            "Report Issue",
            "Equipment Data",
            "Feed"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         pref = getSharedPreferences("MMC-Gamuda", Context.MODE_PRIVATE);
        if(pref.getBoolean("authState",false)){
            accessLevel = 3;
        }
        app = (MyApplication) getApplicationContext();
        util = new Util();
        //Fresco.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        tabView = (ViewPager) findViewById(R.id.pager);
        tabView.addOnPageChangeListener(this);
        mFragmentManager = getSupportFragmentManager();
         mMyAdapter = new MyAdapter(mFragmentManager, accessLevel, pref);
        tabView.setAdapter(mMyAdapter);

        Intent intent = new Intent(this, firebaseBg.class);
        PendingIntent pintent = PendingIntent
                .getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        Random r = new Random();
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + r.nextInt(600000-300000) + 300000,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pintent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if(!pref.getBoolean("authState", false)){
            menu.add(0, 123, Menu.NONE, "Sign In");
        } else if (pref.getBoolean("authState", false)){
            menu.add(0, 321, Menu.NONE, "Sign Out");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == 123) {
            Intent i = new Intent(MainActivity.this, SignIn.class);
            startActivity(i);
            mMyAdapter.setCount(3);
            mMyAdapter.notifyDataSetChanged();
            tabView.invalidate();
        }

        if (id == 321) {
            mMyAdapter.setCount(1);
            mMyAdapter.notifyDataSetChanged();
            tabView.invalidate();
            SharedPreferences pref = getSharedPreferences("MMC-Gamuda", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("authState",false);
            editor.commit();

        }

        if (id ==android.R.id.home) {

            Intent intent = new Intent("custom-event-name");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            getSupportFragmentManager().popBackStack();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String title) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public CharSequence equimentFragGetTile() {

        return getSupportActionBar().getTitle();
    }


    @Override
    public void ringFragUpdateActionBar(String actionBarTItle) {

    }

    @Override
    public String ringFragGetActionbar() {
       return getSupportActionBar().getTitle().toString();
    }

    @Override
    public void enableUpBtn() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void LuxFragCaptureImg(File path) {

        dispatchTakePictureIntent(path,1);

    }

    public void dispatchTakePictureIntent(File imgPath, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


            if (imgPath != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(imgPath));
                startActivityForResult(takePictureIntent, requestCode);
                this.imgPath = imgPath;
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            LuxFrag frag = (LuxFrag) getSupportFragmentManager().findFragmentById(R.id.reportFrag);
            if(frag != null){
                frag.setImage(imgPath);
                //Log.i("Capture Image", imgPath);
            }
        }
        if(requestCode == 2 && resultCode == Activity.RESULT_OK) {
            normalFrag frag = (normalFrag) getSupportFragmentManager().findFragmentById(R.id.reportFrag);
            if(frag != null){
                frag.setImage(imgPath);
                //Log.i("Capture Image", imgPath);
            }
        }
    }


    @Override
    public void normFragCapImg(File path) {
        dispatchTakePictureIntent(path,2);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTitle(actionBarTitle[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
