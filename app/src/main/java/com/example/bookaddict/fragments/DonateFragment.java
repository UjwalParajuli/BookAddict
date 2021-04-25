package com.example.bookaddict.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookaddict.R;
import com.example.bookaddict.models.User;
import com.example.bookaddict.utils.ErrorUtils;
import com.example.bookaddict.utils.SharedPrefManager;
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.checkout.helper.OnCheckOutListener;
import com.khalti.widget.KhaltiButton;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class DonateFragment extends Fragment {
    private KhaltiButton button_open_khalti;
    private EditText edit_text_amount;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donate, container, false);

        user = SharedPrefManager.getInstance(getContext()).getUser();

        button_open_khalti = view.findViewById(R.id.button_open_khalti);
        edit_text_amount = view.findViewById(R.id.edit_text_amount);

        button_open_khalti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = false;
                if (edit_text_amount.getText().toString().trim().isEmpty()){
                    error = true;
                    edit_text_amount.setError("Please enter the amount");
                }
                if (!error) {
                    proceed();
                }
            }
        });

        return view;
    }

    private void proceed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to donate?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openKhalti();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void openKhalti(){
        String amount = edit_text_amount.getText().toString().trim();
        long _amount = Long.parseLong(amount);
        openKhaltiApp(_amount);
    }

    private void openKhaltiApp(long amount){
        amount *= 100;
        Config.Builder builder = new Config.Builder("test_public_key_6239eac0ae384e8a874a3514b2f294a8", "Donation", user.getFull_name(),
                amount, new OnCheckOutListener() {

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("success", data.toString());
                donate();

            }

            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.e("hello", errorMap.toString());
                Toast.makeText(getContext(), "Khalti Error", Toast.LENGTH_SHORT).show();

            }

        });

        Config config = builder.build();

        KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(getContext(), config);
        khaltiCheckOut.show();

    }

    private void donate(){
        String url = "http://192.168.1.65:81/bookaddict/donate.php";
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.trim().equals("success")) {
                    Toast.makeText(getContext(), "Thank you for your donation", Toast.LENGTH_SHORT).show();
                } else if (response.trim().equals("error")) {
                    Toast.makeText(getContext(), "Error while inserting", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(getContext(), ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user.getUser_id()));
                params.put("amount", edit_text_amount.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
