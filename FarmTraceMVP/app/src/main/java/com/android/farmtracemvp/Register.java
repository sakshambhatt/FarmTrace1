package com.android.farmtracemvp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile =
            "com.android.farmtracemvp";
    private static final int CREATE_FILE = 1;

    private String username_value;
    private String role_value;
    private String keysvalues = null;
    private String public_key_value = null;
    private String private_key_value = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialize sharedPreferences and appropriate variables
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        username_value = mPreferences.getString("username_key", null);
        role_value = mPreferences.getString("role_key", null);
        public_key_value = mPreferences.getString("public_key", null);
        private_key_value = mPreferences.getString("private_key", null);
    }

    //Sends HTTP request and stores in a String variable
    public void registerGetkeys(View view) {

        //Sending HTTP request (GET) to URL, extract JSONObject to string
        final RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://pchain.eastus.cloudapp.azure.com:8080/PantherChain/getKey";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        keysvalues = response.toString();
                        Toast.makeText(Register.this, keysvalues, Toast.LENGTH_SHORT).show();
                        queue.stop();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String log_tag = "getKeyerror";
                        Log.d(log_tag, String.valueOf(error));
                        queue.stop();
                        Toast.makeText(Register.this, "GetKey failed!", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    //Saves user data from EditText and Spinner
    public void saveUserData(View view) {

        //Convert EditText and Spinner to String variables
        EditText editText;
        editText = (EditText) findViewById(R.id.editText_username);
        username_value = editText.getText().toString();
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner_roles);
        role_value = mySpinner.getSelectedItem().toString();

        //Save data to SharedPreferences
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("username_key", username_value);
        editor.putString("role_key", role_value);
        editor.commit();

        String log_tag = "shared_preferences";
        Log.d(log_tag, "usr data stored");
        Toast.makeText(Register.this, username_value + "\n" + role_value + "\nsaved", Toast.LENGTH_SHORT).show();
    }

    //Separates keys stores them to sharedPreferences and goes to main screen
    public void gotoMainScreen(View view) {

        //convert keys to JSON; separate private and public keys to store them;
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(keysvalues);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            public_key_value = jObject.getString("publicKey");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            private_key_value = jObject.getString("privateKey");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Saving data in sharedPreferences
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("public_key", public_key_value);
        preferencesEditor.putString("private_key", private_key_value);
        preferencesEditor.commit();
        String log_tag = "shared_preferences";
        Log.d(log_tag, "keys stored");

        //Goes to mainScreen
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
