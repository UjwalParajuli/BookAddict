package com.example.bookaddict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class ChangePasswordActivity extends AppCompatActivity {
    private User user;
    private EditText edit_text_old_password, edit_text_new_password;
    private Button button_update_password;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        user = SharedPrefManager.getInstance(this).getUser();

        edit_text_old_password = findViewById(R.id.edit_text_old_password);
        edit_text_new_password = findViewById(R.id.edit_text_new_password);
        button_update_password = findViewById(R.id.button_update_password);

        button_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

        edit_text_old_password.addTextChangedListener(oldPasswordTextWatcher);
        edit_text_new_password.addTextChangedListener(finalPasswordTextWatcher);
    }

    private TextWatcher oldPasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pass = edit_text_old_password.getText().toString().trim();
            String pass2 = user.getPassword();
            if (!pass.equals(pass2)){
                edit_text_old_password.setError("Please enter the correct old password");
                button_update_password.setEnabled(false);
            }
            else {
                edit_text_old_password.setError(null);
                button_update_password.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher finalPasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pass = edit_text_new_password.getText().toString().trim();
            if (!PASSWORD_PATTERN.matcher(pass).matches()){
                edit_text_new_password.setError("Please use one uppercase, lowercase, digit, special character and must be 6 character long");
                button_update_password.setEnabled(false);

            }
            else {
                edit_text_new_password.setError(null);
                button_update_password.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void updatePassword(){
        final String new_password, old_password;
        boolean error = false;
        String url = "http://192.168.1.65:81/bookaddict/edit_password.php";
        new_password = edit_text_new_password.getText().toString().trim();
        old_password = edit_text_old_password.getText().toString().trim();

        if (new_password.isEmpty()){
            edit_text_new_password.setError("Please fill this field");
            error = true;
        }
        if (old_password.isEmpty()){
            edit_text_old_password.setError("Please fill this field");
            error = true;
        }

        if (!error) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final RequestQueue requestQueue = Volley.newRequestQueue(ChangePasswordActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (response.trim().equals("success")) {
                        Toast.makeText(ChangePasswordActivity.this, "Success! Password Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else if (response.trim().equals("dbError")) {
                        Toast.makeText(ChangePasswordActivity.this, "Error while inserting", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("dbError2")) {
                        Toast.makeText(ChangePasswordActivity.this, "Error while fetching", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(ChangePasswordActivity.this, ErrorUtils.getVolleyError(error), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("new_password", new_password);
                    params.put("user_id", String.valueOf(user.getUser_id()));
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }

    }
}