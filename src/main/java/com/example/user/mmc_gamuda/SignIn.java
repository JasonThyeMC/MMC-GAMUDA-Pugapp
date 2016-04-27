package com.example.user.mmc_gamuda;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SignIn extends AppCompatActivity {

    EditText userName;
    EditText password;
    Button signInBtn;
    TextView response;
    ImageView signInImg;
    MyApplication app;
    Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
         app = (MyApplication) getApplicationContext();
        util = new Util();
        userName = (EditText) findViewById(R.id.userNameEdit);
        password = (EditText) findViewById(R.id.passWordEdit);
        signInBtn = (Button) findViewById(R.id.signInBtn);
        signInImg = (ImageView) findViewById(R.id.loginImg);
        signInImg.setImageResource(R.drawable.mmcgamudah);
        response = (TextView) findViewById(R.id.signInResponse);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("MMC-Gamuda", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("authState", true);
                editor.commit();
                finish();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }
}
