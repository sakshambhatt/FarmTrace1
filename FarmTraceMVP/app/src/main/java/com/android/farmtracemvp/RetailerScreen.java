package com.android.farmtracemvp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class RetailerScreen extends AppCompatActivity {

    private SharedPreferences lolPreferences;
    private String sharedPrefFile =
            "com.android.farmtracemvp";

    private String username = null;
    private String role = null;
    private String public_key_value = null;
    private String uniqueId = null;
    private String receivedFrom = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_screen);

        //getting strings from sharedPreferences and setting a textView
        lolPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        username = lolPreferences.getString("username_key", null);
        role = lolPreferences.getString("role_key", null);
        public_key_value = lolPreferences.getString("public_key", null);
        String pkey = public_key_value.substring(public_key_value.length() - 5);
        TextView textView1 = (TextView) findViewById(R.id.textView_welcome_retailer);
        textView1.setText("Welcome! "+role+" "+pkey);
    }

    public void saveData(View view) {

        //Extract editText data in String variables
        EditText editText1 = (EditText) findViewById(R.id.editText_uid_retailer);
        uniqueId = editText1.getText().toString();
        EditText editText2 = (EditText) findViewById(R.id.editText_received_from);
        receivedFrom = editText2.getText().toString();

        Toast.makeText(RetailerScreen.this, "data saved\n" + uniqueId + "\n" + receivedFrom, Toast.LENGTH_SHORT).show();
    }

    //makes POST request and passes json and public key as parameters
    public void add2Chain(View view) {
        String url = "http://pchain.eastus.cloudapp.azure.com:8080/PantherChain/addJson";
        final String json = "{\"uid\":\"" +uniqueId+ "\",\"username_r\":\"" +username+ "\",\"role_r\":\"" +role+ "\",\"receivedFrom\":\"" +receivedFrom+ "\"}";
        String log_tag = "json";
        Log.d(log_tag, json);

        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        //response
                        Toast.makeText(RetailerScreen.this, response, Toast.LENGTH_SHORT).show();
                        String log_tag = "server_response";
                        Log.d(log_tag, response);
                        queue.stop();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(RetailerScreen.this, "addJSON failed!", Toast.LENGTH_SHORT).show();
                        queue.stop();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("jsonData", json);
                params.put("publicKey", public_key_value);
                return params;
            }
        };
        queue.add(postRequest);
    }
}
