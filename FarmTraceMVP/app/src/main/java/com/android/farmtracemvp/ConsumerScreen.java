package com.android.farmtracemvp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class ConsumerScreen extends AppCompatActivity {

    public String uniqueID = null;
    public String username_farmer = null;
    public String role_farmer = null;
    public String product = null;
    public String quantity = null;
    public String sentToShipper = null;
    public String username_shipper = null;
    public String role_shipper = null;
    public String certification = null;
    public String sentToRetailer = null;
    public String username_retailer = null;
    public String role_retailer = null;
    public String receivedFrom = null;

    public String urlTest = null;
    public String urlFinal = null;

    public JSONArray serverJsonArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_screen);

        TextView textView1 = (TextView) findViewById(R.id.textView_consumer);
        textView1.setText("Please mine transactions before display!");
    }

    //calls a method and passes url to it
    public void openBrowser(View view) {
        goToUrl("http://pchain.eastus.cloudapp.azure.com:8080/PantherChain/tranmine.jsp");
        TextView textView = (TextView) findViewById(R.id.textView_consumer);
        textView.setText("Enter UID if all transactions have been mined!");
    }

    //launches internet browser in device
    public void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    //Takes UID; makes a GET HTTP request with UID as parameter;
    public void getData(View view) {
        //Getting UID from editText to String
        EditText editText = (EditText) findViewById(R.id.editText_uid_consumer);
        uniqueID = editText.getText().toString();


        //Makes a HTTP request (GET) and stores the JSON response in a string
        final RequestQueue queue = Volley.newRequestQueue(this);
        urlTest = "http://pchain.eastus.cloudapp.azure.com:8080/PantherChain/searchuid?uid=";
        urlFinal = urlTest + uniqueID;
        String log_tag = "webLink";
        Log.d(log_tag, urlFinal);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlFinal, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        serverJsonArray = response;
                        queue.stop();
                        Toast.makeText(ConsumerScreen.this, "Search uid ok!", LENGTH_SHORT).show();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        queue.stop();
                        Toast.makeText(ConsumerScreen.this, "Search uid failed!", LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

    //displays JSON response in texView
    public void displayTX(View view) {
        String log_tag = "jsonValue";

        try {
            JSONObject json1 = serverJsonArray.getJSONObject(0);
            JSONObject sample = json1.getJSONObject("internaldata");
            Log.d(log_tag, sample.toString());

            username_farmer = sample.getString("username_f");
            role_farmer = sample.getString("role_f");
            product = sample.getString("product");
            quantity = sample.getString("quantity");
            sentToShipper = sample.getString("sentToShipper");

            json1 = serverJsonArray.getJSONObject(1);
            sample = json1.getJSONObject("internaldata");
            Log.d(log_tag, sample.toString());

            username_shipper = sample.getString("username_s");
            role_shipper = sample.getString("role_s");
            certification = sample.getString("certification");
            sentToRetailer = sample.getString("sentToRetailer");

            json1 = serverJsonArray.getJSONObject(2);
            sample = json1.getJSONObject("internaldata");
            Log.d(log_tag, sample.toString());

            username_retailer = sample.getString("username_r");
            role_retailer = sample.getString("role_r");
            receivedFrom = sample.getString("receivedFrom");

            String displayJourney = username_farmer + " " + role_farmer + "\n" + product + " " + quantity + " " + sentToShipper + "\n" + username_shipper + " " + role_shipper + "\n" + certification + " " + sentToRetailer + "\n" + username_retailer + " " + role_retailer + "\n" + receivedFrom;
            TextView textView2 = (TextView) findViewById(R.id.textView_display_journey);
            textView2.setText(displayJourney);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
