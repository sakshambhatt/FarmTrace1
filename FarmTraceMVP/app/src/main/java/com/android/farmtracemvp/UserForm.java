package com.android.farmtracemvp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UserForm extends AppCompatActivity {

    SharedPreferences lolPreferences;
    private String sharedPrefFile =
            "com.android.farmtracemvp";
    private String role = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);

        //get string from sharedpreferences
        lolPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        role = lolPreferences.getString("role_key", null);
        TextView textView = (TextView) findViewById(R.id.textView_user_role);
        textView.setText("Pssst! You are a "+role);
    }

    //Opens Farmer Screen
    public void openFarmerScreen(View view) {
        Intent intent = new Intent(this, FarmerScreen.class);
        startActivity(intent);
    }

    //Opens Shipper Screen
    public void openShipperScreen(View view) {
        Intent intent = new Intent(this, ShipperScreen.class);
        startActivity(intent);
    }

    //Opens Retailer Screen
    public void openRetailerScreen(View view) {
        Intent intent = new Intent(this, RetailerScreen.class);
        startActivity(intent);
    }
}
