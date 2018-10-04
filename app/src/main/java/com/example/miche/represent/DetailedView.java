package com.example.miche.represent;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.miche.represent.RecyclerViewAdapter.ID;
import static com.example.miche.represent.RecyclerViewAdapter.IMAGEURL;
import static com.example.miche.represent.RecyclerViewAdapter.NAME;
import static com.example.miche.represent.RecyclerViewAdapter.PARTY;
import static com.example.miche.represent.RecyclerViewAdapter.TYPE;

public class DetailedView extends AppCompatActivity {
    public String commitUrl;
    public String billsUrl;
    public String buttonUrl;
    TextView mTextView;
    private ArrayList<String> mCommittees = new ArrayList<>();
    private ArrayList<String> mBills = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    RecyclerViewDetailed adapter;
    RecyclerViewDetailed adapter2;
    //buttons
    //http://www.stickpng.com/assets/images/58afdad6829958a978a4a693.png red
    //https://i0.wp.com/newreality.co.za/wp-content/uploads/2017/04/agency-4-slider-blue-circle.png blue

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        Typeface reg = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.ttf");

        Intent in = getIntent();
        String id = in.getStringExtra(ID);
        String type = in.getStringExtra(TYPE);
        String name = in.getStringExtra(NAME);
        String party = in.getStringExtra(PARTY);
        String imageURL = in.getStringExtra(IMAGEURL);
        CircleImageView im = findViewById(R.id.repImage);
        CircleImageView bullet = findViewById(R.id.bullet);

        if (party.equals("Democrat")) {
            buttonUrl = "https://i0.wp.com/newreality.co.za/wp-content/uploads/2017/04/agency-4-slider-blue-circle.png";
        } else {
            buttonUrl = "https://upload.wikimedia.org/wikipedia/commons/9/9e/WX_circle_red.png";
        }

        //set starting text
        TextView mainName = findViewById(R.id.detailedname);
        mainName.setTypeface(reg);
        mainName.setText(type + "\n" + name);

        mTextView = (TextView)findViewById(R.id.text); //for recyclerview

        TextView mainParty = findViewById(R.id.detailedparty);
        mainParty.setTypeface(light);
        mainParty.setText(party);

        TextView commit = findViewById(R.id.committees);
        commit.setText("Committees");
        commit.setTypeface(reg);

        TextView bills = findViewById(R.id.recentBills);
        bills.setTypeface(reg);
        bills.setText("Recent Bills Sponsored");

        //set main images
        Glide.with(this)
                .asBitmap()
                .load(imageURL)
                .into(im);

        //set bulletpoint
        Glide.with(this)
                .asBitmap()
                .load(buttonUrl)
                .into(bullet);

        commitUrl = "https://api.propublica.org/congress/v1/members/" + id + ".json";
        billsUrl = "https://api.propublica.org/congress/v1/members/" + id + "/bills/introduced.json";

        parseCommittee();
    }

    public void parseCommittee() {
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, commitUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray arr = obj.getJSONArray("results");
                            JSONObject roleOBJ = arr.getJSONObject(0);
                            JSONArray roles = roleOBJ.getJSONArray("roles");
                            JSONObject commitOBJ = roles.getJSONObject(0);
                            JSONArray committees = commitOBJ.getJSONArray("committees");
                            for (int i = 0; i < committees.length(); i++) {
                                JSONObject eachCom = committees.getJSONObject(i);
                                String c = eachCom.getString("name");
                                mCommittees.add(c);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        parseBills();
                        cinitImageBitMaps();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        })

        {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap headers = new HashMap();
            headers.put("X-API-Key", "kAV0W2yg3wjQqOvbcfA00XiY5zVjyXDHAMkzKkO8");
            return headers;
        }};
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void parseBills() {
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, billsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray arr = obj.getJSONArray("results");
                            JSONObject billOBJ = arr.getJSONObject(0);
                            JSONArray bills = billOBJ.getJSONArray("bills");
                            for (int i = 0; i < bills.length(); i++) {
                                JSONObject eachCom = bills.getJSONObject(i);
                                String title = eachCom.getString("title");
                                mBills.add(title);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        binitImageBitMaps();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("X-API-Key", "kAV0W2yg3wjQqOvbcfA00XiY5zVjyXDHAMkzKkO8");
                return headers;
            }};
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void cinitImageBitMaps() {
        for (int i = 0; i < mCommittees.size(); i++) {
            mImageUrls.add(buttonUrl);
        }
        commitRecycle();
    }

    private void binitImageBitMaps() {
        for (int i = 0; i < mCommittees.size(); i++) {
            mImageUrls.add(buttonUrl);
        }
        billsRecycle();
    }

    private void billsRecycle() {
        RecyclerView bills = findViewById(R.id.billsRecycle);
        adapter = new RecyclerViewDetailed(mBills, mImageUrls, this);
        bills.setAdapter(adapter);
        bills.setLayoutManager(new LinearLayoutManager(this));
    }

    private void commitRecycle() {
        RecyclerView commit = findViewById(R.id.committeeRecycle);
        adapter2 = new RecyclerViewDetailed(mCommittees, mImageUrls, this);
        commit.setAdapter(adapter2);
        commit.setLayoutManager(new LinearLayoutManager(this));
    }
}
