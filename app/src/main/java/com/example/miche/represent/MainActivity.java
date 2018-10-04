package com.example.miche.represent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationClient;
    public static final String TYPE = "it's the type";
    public static final String ZIPCODE = "zipzip";
    public static final String GEOCOORD = "geo";
    private String url;
    public Location loc;
    public String inputZip;
    public TextView mTextView;
    public View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView main = (TextView) findViewById(R.id.maintext);
        Typeface semiBold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");
        main.setTypeface(semiBold);
        main.setText("Let's get you the facts.");

        Button currL = (Button) findViewById(R.id.currLocation);
        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        currL.setTypeface(regular);
        currL.setText("Select Current Location");
        currL.setTransformationMethod(null);

        EditText zip = (EditText) findViewById(R.id.zipCode);
        zip.setTypeface(regular);
        zip.setText("Zip Code");
        zip.setTransformationMethod(null);

        TextView enter = (TextView) findViewById(R.id.pleaseEnter);
        enter.setTypeface(regular);
        enter.setText("Please enter your");
        enter.setTransformationMethod(null);

        TextView or = (TextView) findViewById(R.id.or);
        or.setTypeface(regular);
        or.setText("-or-");
        or.setTransformationMethod(null);

        TextView rep = (TextView) findViewById(R.id.represent);
        Typeface repReg = Typeface.createFromAsset(getAssets(), "fonts/Righteous-Regular.ttf");
        rep.setTypeface(repReg);
        rep.setText("Represent!");

        Log.d("CREATION", "This is working");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
        }

        Log.d("LOCATION", "got here.");
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            loc = location;// Logic to handle location object
                            Log.d("GOTLOCATION", "got here.");
                            System.out.println(loc.getLatitude());
                        }
                    }
                });
    }
    //https://api.geocod.io/v1.3/geocode?q=94720&fields=cd&api_key=**

    public void getRepsbyZip(View v1) {
        Intent zipRep = new Intent(this, Representatives.class);
        EditText zip = (EditText)findViewById(R.id.zipCode);
        inputZip = zip.getText().toString();
        zipRep.putExtra(ZIPCODE, inputZip);
        zipRep.putExtra(TYPE, "zero");
        startActivity(zipRep);
    }

    public void getRepsbyLoc(View v2) {
        Intent locRep = new Intent(this, Representatives.class);
        locRep.putExtra(TYPE, "one");
        String latLon = Double.toString(loc.getLatitude()) + "," + Double.toString(loc.getLongitude());
        locRep.putExtra(GEOCOORD, latLon);
        startActivity(locRep);
    }

    public int num;
    public void randomLoc(View v3) {
        Random rand = new Random();
        num = rand.nextInt(90000) + 10000;
        url = "https://api.geocod.io/v1.3/geocode?q=" + Integer.toString(num)
                + "&fields=cd&api_key=275763c565b5b9bf675bb6030222990b93552b5";
        randomParse();
    }

    public void randomParse() {
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray arr = obj.getJSONArray("results");
                            if (arr.length() == 0) {
                                randomLoc(v);
                            } else {
                                Intent random = new Intent(getApplicationContext(), Representatives.class);
                                random.putExtra(ZIPCODE, Integer.toString(num));
                                random.putExtra(TYPE, "zero");
                                startActivity(random);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        }
    }
