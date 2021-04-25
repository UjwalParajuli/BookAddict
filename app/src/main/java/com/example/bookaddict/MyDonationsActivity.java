package com.example.bookaddict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookaddict.adapters.DonationAdapter;
import com.example.bookaddict.models.DonationModel;
import com.example.bookaddict.models.User;
import com.example.bookaddict.utils.ErrorUtils;
import com.example.bookaddict.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyDonationsActivity extends AppCompatActivity {
    private TextView text_view_heading_donation, text_view_not_found_donation;
    private RecyclerView recycler_view_donations;
    private ArrayList<DonationModel> donationModelArrayList;
    private DonationAdapter donationAdapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donations);

        user = SharedPrefManager.getInstance(this).getUser();

        text_view_heading_donation = findViewById(R.id.text_view_heading_donation);
        text_view_not_found_donation = findViewById(R.id.text_view_not_found_donation);
        recycler_view_donations = findViewById(R.id.recycler_view_donations);

        donationModelArrayList = new ArrayList<>();
        donationAdapter = new DonationAdapter(donationModelArrayList, MyDonationsActivity.this);

        getDonations();

    }

    private void getDonations(){
        String url = "http://192.168.1.65:81/bookaddict/donations.php";

        final RequestQueue requestQueue = Volley.newRequestQueue(MyDonationsActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("not_found")) {
                    text_view_heading_donation.setVisibility(View.GONE);
                    recycler_view_donations.setVisibility(View.GONE);
                    text_view_not_found_donation.setVisibility(View.VISIBLE);
                }
                else{
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonResponse;

                        for (int i = 0; i < jsonArray.length(); i++){
                            jsonResponse = jsonArray.getJSONObject(i);
                            int amount = jsonResponse.getInt("amount");
                            String donation_date = jsonResponse.getString("date_of_donation");

                            String _amount = String.valueOf(amount);
                            DonationModel donationModel = new DonationModel(_amount, donation_date);
                            donationModelArrayList.add(donationModel);
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyDonationsActivity.this, LinearLayoutManager.VERTICAL, false);
                        recycler_view_donations.setLayoutManager(linearLayoutManager);
                        recycler_view_donations.setAdapter(donationAdapter);
                        //recycler_view_futsals.addItemDecoration(new SpacesItemDecoration(20));
                        donationAdapter.notifyDataSetChanged();

                    }
                    catch (JSONException e) {
                        Toast.makeText(MyDonationsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyDonationsActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user.getUser_id()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}