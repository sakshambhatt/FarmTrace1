package com.android.farmtracemvp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String role;
    private String username;

    private SharedPreferences lolPreferences;
    private String sharedPrefFile =
            "com.android.farmtracemvp";

    private String publicKey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textView_status);
        textView.setText("Please verify registration before login");
    }

    //Method displays a toast (giving credit to logo creator) when image is tapped
    public void showToast(View view) {

        Toast toast = Toast.makeText(this, R.string.toast_message,
                Toast.LENGTH_LONG);
        toast.show();
    }

    //method starts the Register activity
    public void registerUser(View view) {

        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    //method verifies whether user has already registered or not
    public void verifyRegister(View view) {

        //getting values from sharedPreferences
        lolPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        publicKey = lolPreferences.getString("public_key", null);
        username = lolPreferences.getString("username_key", null);
        role = lolPreferences.getString("role_key", null);

        if(publicKey == null || username == null || role == null) {
            TextView textView = (TextView) findViewById(R.id.textView_status);
            textView.setText("Please Register first!");
        }
        else {
            //setting textview to display public key, username and role
            TextView textView = (TextView) findViewById(R.id.textView_status);
            String pkey = publicKey.substring(publicKey.length() - 5);
            textView.setText(pkey + "\n" + username + "\n" + role);
        }
    }

    //opens login choice screen
    public void openUserForm(View view) {
        Intent intent = new Intent(this, UserForm.class);
        startActivity(intent);
    }

    //opens consumer screen
    public void openConsumerScreen(View view) {
        Intent intent = new Intent(this, ConsumerScreen.class);
        startActivity(intent);
    }
}

