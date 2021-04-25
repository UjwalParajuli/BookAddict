package com.example.bookaddict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookaddict.models.User;
import com.example.bookaddict.utils.ErrorUtils;
import com.example.bookaddict.utils.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity {
    private User user;
    private EditText edit_text_full_name_edit_profile, edit_text_phone_edit_profile, edit_text_email_edit_profile;
    private Button button_update_profile;
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9]{10,13}$");
    private int old_user_id;
    private String old_email, old_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        user = SharedPrefManager.getInstance(this).getUser();
        old_user_id = user.getUser_id();
        old_email = user.getEmail();
        old_password = user.getPassword();

        edit_text_full_name_edit_profile = findViewById(R.id.edit_text_full_name_edit_profile);
        edit_text_phone_edit_profile = findViewById(R.id.edit_text_phone_edit_profile);
        edit_text_email_edit_profile = findViewById(R.id.edit_text_email_edit_profile);
        button_update_profile = findViewById(R.id.button_update_profile);

        edit_text_full_name_edit_profile.setText(user.getFull_name());
        edit_text_phone_edit_profile.setText(user.getPhone());
        edit_text_email_edit_profile.setText(user.getEmail());

        button_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        edit_text_full_name_edit_profile.addTextChangedListener(nameTextWatcher);
        edit_text_email_edit_profile.addTextChangedListener(emailTextWatcher);
        edit_text_phone_edit_profile.addTextChangedListener(phoneTextWatcher);

    }

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String full_name = edit_text_full_name_edit_profile.getText().toString().trim();
            if (!full_name.matches("[a-zA-Z\\s]+")){
                edit_text_full_name_edit_profile.setError("Please enter your valid name without digits and special characters");
                button_update_profile.setEnabled(false);
            }
            else {
                edit_text_full_name_edit_profile.setError(null);
                button_update_profile.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String email = edit_text_email_edit_profile.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edit_text_email_edit_profile.setError("Please enter a valid email address");
                button_update_profile.setEnabled(false);
            }
            else {
                edit_text_email_edit_profile.setError(null);
                button_update_profile.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String phone = edit_text_phone_edit_profile.getText().toString().trim();
            if (!PHONE_PATTERN.matcher(phone).matches()){
                edit_text_phone_edit_profile.setError("Please enter valid 10 digit number");
                button_update_profile.setEnabled(false);
            }
            else {
                edit_text_phone_edit_profile.setError(null);
                button_update_profile.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void updateProfile(){
        final String full_name, new_email, phone;
        boolean error = false;
        String url = "http://192.168.1.65:81/bookaddict/edit_profile.php";
        full_name = edit_text_full_name_edit_profile.getText().toString().trim();
        new_email = edit_text_email_edit_profile.getText().toString().trim();
        phone = edit_text_phone_edit_profile.getText().toString().trim();

        if (full_name.isEmpty()){
            edit_text_full_name_edit_profile.setError("Please fill this field");
            error = true;
        }
        if (!full_name.matches("[a-zA-Z\\s]+")){
            edit_text_full_name_edit_profile.setError("Please enter your name properly");
            error = true;
        }
        if (new_email.isEmpty()){
            edit_text_email_edit_profile.setError("Please fill this field");
            error = true;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(new_email).matches()) {
            edit_text_email_edit_profile.setError("Please enter a valid email address");
            error = true;
        }
        if (phone.isEmpty()){
            edit_text_phone_edit_profile.setError("Please fill this field");
            error = true;
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            edit_text_phone_edit_profile.setError("Please enter valid phone number");
            error = true;
        }

        if (!error) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(EditProfileActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("success")) {
                        Toast.makeText(EditProfileActivity.this, "Success! Profile Updated", Toast.LENGTH_SHORT).show();

                        User user = new User(old_user_id, full_name, new_email, phone, old_password);
                        SharedPrefManager.getInstance(EditProfileActivity.this).userLogin(user);
                        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (response.trim().equals("dbError")) {
                        Toast.makeText(EditProfileActivity.this, "Error while inserting", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("dbError2")) {
                        Toast.makeText(EditProfileActivity.this, "Error while fetching", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("email_taken")) {
                        Toast.makeText(EditProfileActivity.this, "Sorry! This email is already registered", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(EditProfileActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("full_name", full_name);
                    params.put("new_email", new_email);
                    params.put("old_email", old_email);
                    params.put("user_id", String.valueOf(user.getUser_id()));
                    params.put("phone", phone);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }
}