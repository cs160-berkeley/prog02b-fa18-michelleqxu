package com.example.miche.represent;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Representatives extends AppCompatActivity {
    public TextView mTextView;
    //private HashMap<String, String> ids;
    public String url;
    //recyclerview
    private ArrayList<String> mParty = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mWebsites = new ArrayList<>();
    private ArrayList<String> mEmails = new ArrayList<>();
    private ArrayList<String> mType = new ArrayList<>();
    RecyclerViewAdapter adapter;
    //propublica ids
    private ArrayList<String> ids = new ArrayList<>();
    //lock
    private Object lock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representatives);

        TextView main = (TextView)findViewById(R.id.header);
        Typeface semiBold = Typeface.createFromAsset(getAssets(),  "fonts/Montserrat-SemiBold.ttf");
        main.setTypeface(semiBold);
        main.setText("Here are your legislators.");

        TextView rep = (TextView) findViewById(R.id.represent1);
        Typeface repReg = Typeface.createFromAsset(getAssets(), "fonts/Righteous-Regular.ttf");
        rep.setTypeface(repReg);
        rep.setText("Represent!");
        mTextView = (TextView) findViewById(R.id.text);
        //ids = new HashMap<>();
        Intent intent = getIntent();
        String type = intent.getStringExtra(MainActivity.TYPE);
        if (type.equals("zero")) {
            url = "https://api.geocod.io/v1.3/geocode?q=" + intent.getStringExtra(MainActivity.ZIPCODE)
                    + "&fields=cd&api_key=275763c565b5b9bf675bb6030222990b93552b5";
        } else {
            url = "https://api.geocod.io/v1.3/reverse?q=" + intent.getStringExtra(MainActivity.GEOCOORD)
                    + "&fields=cd&api_key=275763c565b5b9bf675bb6030222990b93552b5";
        }
        rep();
        System.out.println(ids);
    }

    public void rep() {
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
                            JSONObject obj1 = arr.getJSONObject(0);
                            JSONObject field = obj1.getJSONObject("fields");
                            JSONArray conDist = field.getJSONArray("congressional_districts");
                            for (int i = 0; i < conDist.length(); i++) {
                                JSONArray currLeg = conDist.getJSONObject(i).getJSONArray("current_legislators");
                                for (int j = 0; j < currLeg.length(); j++) {
                                    JSONObject reps = currLeg.getJSONObject(j);
                                    String type = reps.getString("type");
                                    JSONObject bio = reps.getJSONObject("bio");
                                    String name = bio.getString("first_name") + " " + bio.getString("last_name");
                                    String party = bio.getString("party");
                                    JSONObject contact = reps.getJSONObject("contact");
                                    String web = contact.getString("url");
                                    String email = contact.getString("contact_form");
                                    JSONObject refs = reps.getJSONObject("references");
                                    String id = refs.getString("bioguide_id");

                                    String emailFin = "<a href=\"" + email + "\">Email</a>";
                                    String webFin = "<a href=\"" + web + "\">Website</a>";

                                    //Log.d("INSIDE", "calling");
                                    if (!mNames.contains(name)) {
                                        mParty.add(party);
                                        mEmails.add(emailFin);
                                        mNames.add(name);
                                        mType.add(type);
                                        mWebsites.add(webFin);
                                        ids.add(id);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initImageBitMaps();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        //Log.d("INITCALL", "calling");
    }

    //http://bioguide.congress.gov/bioguide/photo/[member_id_first_letter]/[member_id].jpg
    private void initImageBitMaps() {
        for (int i = 0; i < ids.size(); i++) {
            String image = "http://bioguide.congress.gov/bioguide/photo/" + Character.toString(ids.get(i).charAt(0))
                    + "/" + ids.get(i) + ".jpg";
            mImageUrls.add(image);
        }

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycle);
        adapter = new RecyclerViewAdapter(mType, mImageUrls, mNames, mWebsites, mEmails, mParty, ids, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
